package com.dinoco.oficina;

import org.bson.types.Decimal128;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import tools.jackson.databind.json.JsonMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.UUID;

import static com.mongodb.client.model.Filters.eq;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = "spring.profiles.active=local")
@EnabledIfEnvironmentVariable(named = "SERVICO_MONGO_INTEGRATION_TESTS", matches = "true")
class CatalogoServicoApiApplicationTests {

    @Value("${local.server.port}")
    private int port;

    @Autowired
    private MongoTemplate mongo;

    private final HttpClient http = HttpClient.newHttpClient();
    private final JsonMapper json = JsonMapper.builder().build();

    @Test
    void cadastraAtualizaEDesativaMantendoServicoConsultavelEmLote() throws Exception {
        assertThat(mongo.getDb().getName()).endsWith("_test");
        var descricao = "Troca de óleo " + UUID.randomUUID();
        var criado = enviar("POST", "/api/servicos", dados(descricao, "100.00"));
        assertThat(criado.statusCode()).isEqualTo(201);
        var id = json.readTree(criado.body()).get("id").asLong();
        assertThat(id).isPositive();
        assertThat(mongo.getCollection("servicos").find(eq("_id", id)).first().get("precoPadrao"))
                .isInstanceOf(Decimal128.class);

        var duplicado = enviar("POST", "/api/servicos", dados(descricao.toUpperCase(java.util.Locale.ROOT), "100.00"));
        assertThat(duplicado.statusCode()).isEqualTo(400);
        assertThat(enviar("PUT", "/api/servicos/" + id, dados(descricao, "120.00")).statusCode()).isEqualTo(200);
        assertThat(enviar("DELETE", "/api/servicos/" + id, null).statusCode()).isEqualTo(204);
        var lote = enviar("GET", "/api/servicos?ids=" + id + "," + Long.MAX_VALUE, null);
        assertThat(lote.statusCode()).isEqualTo(200);
        var itens = json.readTree(lote.body());
        assertThat(itens.size()).isEqualTo(1);
        assertThat(itens.get(0).get("id").asLong()).isEqualTo(id);
        assertThat(itens.get(0).get("ativo").asBoolean()).isFalse();
        assertThat(itens.get(0).get("precoPadrao").decimalValue()).isEqualByComparingTo("120.00");
    }

    @Test
    void exigeApiKey() throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + "/api/servicos/1"))
                .GET().build();
        assertThat(http.send(request, HttpResponse.BodyHandlers.ofString()).statusCode()).isEqualTo(401);
    }

    private String dados(String descricao, String preco) {
        return "{\"descricao\":\"" + descricao + "\",\"precoPadrao\":" + preco + ",\"tempoEstimadoMinutos\":30}";
    }

    private HttpResponse<String> enviar(String metodo, String path, String body) throws Exception {
        var request = HttpRequest.newBuilder(URI.create("http://127.0.0.1:" + port + path))
                .header("X-API-Key", "servico-local-key")
                .header("Content-Type", "application/json")
                .method(metodo, body == null ? HttpRequest.BodyPublishers.noBody() : HttpRequest.BodyPublishers.ofString(body))
                .build();
        return http.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
