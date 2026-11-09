#!/usr/bin/env bash
set -euo pipefail

: "${DOCKER_IMAGE:?Configure DOCKER_IMAGE}"
: "${CATALOGO_SERVICO_API_KEY:?Configure CATALOGO_SERVICO_API_KEY}"
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

kubectl -n "$namespace" apply -f k8s/service.yaml
kubectl set image --local -f k8s/deployment.yaml catalogo-servico-api="$DOCKER_IMAGE" -o yaml \
  | kubectl -n "$namespace" apply -f -
kubectl -n "$namespace" rollout status deployment/catalogo-servico-api-deployment --timeout=900s
kubectl -n "$namespace" get pods,svc -l app=catalogo-servico-api
