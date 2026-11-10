#!/usr/bin/env bash
set -euo pipefail

: "${DOCKER_IMAGE:?Configure DOCKER_IMAGE}"
: "${CATALOGO_SERVICO_API_KEY:?Configure CATALOGO_SERVICO_API_KEY}"
: "${NEW_RELIC_LICENSE_KEY:?Configure NEW_RELIC_LICENSE_KEY}"
namespace="${K8S_NAMESPACE:-default}"

if [[ -z "${CATALOGO_SERVICO_API_KEY//[[:space:]]/}" ]]; then
  echo "CATALOGO_SERVICO_API_KEY nao pode conter apenas espacos." >&2
  exit 1
fi

mongo_uri_present=$(kubectl -n "$namespace" get secret catalogo-servico-mongodb \
  -o 'go-template={{if index .data "MONGODB_URI"}}ok{{end}}')
if [[ "$mongo_uri_present" != ok ]]; then
  echo "Secret catalogo-servico-mongodb sem MONGODB_URI." >&2
  exit 1
fi
kubectl -n "$namespace" rollout status statefulset/mongodb-servico --timeout=180s

# A mesma chave atende a API e o nucleo. Rotacao exige coordenar os dois deployments.
python3 - <<'PY' | kubectl -n "$namespace" apply --server-side -f -
import base64
import json
import os

print(json.dumps({
    "apiVersion": "v1",
    "kind": "Secret",
    "metadata": {"name": "catalogo-servico-integration-secret"},
    "type": "Opaque",
    "immutable": True,
    "data": {"CATALOGO_SERVICO_API_KEY": base64.b64encode(
        os.environ["CATALOGO_SERVICO_API_KEY"].encode()).decode()},
}))
PY

# A licenca fica em um Secret separado da chave compartilhada do catalogo.
python3 - <<'PYTHON' | kubectl -n "$namespace" apply --server-side -f -
import base64
import json
import os

license_key = os.environ["NEW_RELIC_LICENSE_KEY"]
if not license_key.strip():
    raise SystemExit("Configure NEW_RELIC_LICENSE_KEY antes do deploy.")
print(json.dumps({
    "apiVersion": "v1",
    "kind": "Secret",
    "metadata": {"name": "catalogo-servico-api-secret"},
    "type": "Opaque",
    "data": {"NEW_RELIC_LICENSE_KEY": base64.b64encode(license_key.encode()).decode()},
}))
PYTHON

kubectl -n "$namespace" apply -f k8s/configmap.yaml -f k8s/service.yaml
kubectl set image --local -f k8s/deployment.yaml catalogo-servico-api="$DOCKER_IMAGE" -o yaml \
  | kubectl -n "$namespace" apply -f -
kubectl -n "$namespace" rollout status deployment/catalogo-servico-api-deployment --timeout=900s
kubectl -n "$namespace" get pods,svc -l app=catalogo-servico-api
