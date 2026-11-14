# Catálogo de Serviços — Oficina Mecânica Dinoco

Microsserviço da **Oficina Mecânica Dinoco** responsável pelo cadastro, pela consulta, pela atualização e pela desativação dos serviços oferecidos pela oficina. O MongoDB armazena o catálogo em `servicos`, com IDs numéricos gerados por um contador atômico. Os preços usam Decimal128 e a descrição tem um índice único que ignora diferenças de maiúsculas e minúsculas.

O núcleo consulta o preço ao incluir o serviço na ordem de serviço (OS). O valor cobrado fica registrado no item da OS, e as métricas consultam a descrição e o tempo estimado em lote. Serviços desativados continuam disponíveis para consultas históricas.

## Tecnologias

- Java 21, Maven Wrapper e Spring Boot 4.0.6.
- Spring Web MVC para a API HTTP, Spring Security para a chave de API e Spring Boot Actuator para as verificações de saúde.
- MongoDB 8 e Spring Data MongoDB para persistência.
- Springdoc OpenAPI para documentação da API, JUnit 5 e REST Assured para testes.
- Docker e Docker Compose para execução local.

## Execução local

Instale o Docker com o Compose, inicie o Docker e execute os comandos na raiz do repositório.

```bash
docker network inspect dinoco-local >/dev/null 2>&1 || docker network create dinoco-local
docker compose up --build -d
```

No PowerShell, use a variante abaixo para criar a rede, quando necessário, e subir os contêineres.

```powershell
docker network inspect dinoco-local *> $null
if ($LASTEXITCODE -ne 0) { docker network create dinoco-local }
docker compose up --build -d
```

Confira os contêineres com `docker compose ps`. Acompanhe a inicialização com `docker compose logs -f api`. Para parar o ambiente, use `docker compose down`.

A API usa a porta `8083`. O MongoDB fica em `localhost:27018` para acesso da máquina local. Para executar a API pelo IntelliJ, inicie apenas o MongoDB com `docker compose up -d mongo` e use o perfil Spring `local`.

A rede `dinoco-local` permite que o núcleo em Docker acesse o catálogo em `http://catalogo-servico-api:8083`. Apenas a API do catálogo participa dessa rede.

| Variável | Uso |
| --- | --- |
| `MONGODB_URI` | Conexão com o MongoDB. O padrão local é `mongodb://localhost:27018/oficina_servicos_db`. |
| `CATALOGO_SERVICO_API_KEY` | Header `X-API-Key`. O perfil `local` fornece `servico-local-key`. |
| `SERVER_PORT` | Porta HTTP, por padrão `8083`. |

## Rotas da API

Envie o header `X-API-Key` nas requisições. Para conferir se a API e o MongoDB estão prontos, consulte `GET http://localhost:8083/actuator/health/readiness` com esse header.

| Método | Rota | Operação |
| --- | --- | --- |
| POST | `/api/servicos` | Cadastrar |
| GET | `/api/servicos/{id}` | Consultar |
| GET | `/api/servicos?ids=1,2` | Consultar até 100 IDs, incluindo desativados |
| PUT | `/api/servicos/{id}` | Alterar descrição, preço ou tempo estimado |
| DELETE | `/api/servicos/{id}` | Desativar sem excluir o documento |

Exemplo de cadastro no ambiente local.

```bash
curl -X POST http://localhost:8083/api/servicos \
  -H 'X-API-Key: servico-local-key' \
  -H 'Content-Type: application/json' \
  -d '{"descricao":"Troca de óleo","precoPadrao":100.00,"tempoEstimadoMinutos":30}'
```

A consulta em lote devolve apenas os documentos encontrados. As rotas, incluindo a documentação OpenAPI e o Actuator, usam a mesma API key.

Em ambiente vazio, cadastre os serviços pela API e utilize os IDs devolvidos ao montar as ordens de exemplo.

## Testes

O workflow inicia um MongoDB descartável e testa cadastro, alteração, desativação, consulta em lote e autenticação pela API HTTP. Para executar os testes localmente com o MongoDB do Compose, use um banco separado terminado em `_test`.

```bash
SERVICO_MONGO_INTEGRATION_TESTS=true \
MONGODB_URI=mongodb://localhost:27018/oficina_servicos_test \
sh mvnw verify
```

### Cobertura de Testes — Melhoria Futura

Foi criado no `pom.xml` o perfil Maven `cobertura-80`, que permite validar uma cobertura mínima de **80% das linhas de código**, utilizando o JaCoCo.

O perfil pode ser executado manualmente com o comando:

```bash
./mvnw clean verify -Pcobertura-80