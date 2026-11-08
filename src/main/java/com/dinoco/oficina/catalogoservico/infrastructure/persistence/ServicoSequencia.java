package com.dinoco.oficina.catalogoservico.infrastructure.persistence;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ServicoSequencia {

    private final MongoTemplate mongo;

    public ServicoSequencia(MongoTemplate mongo) {
        this.mongo = mongo;
    }

    public long proximoId() {
        var sequencia = mongo.findAndModify(Query.query(Criteria.where("_id").is("servicos")),
                new Update().inc("valor", 1L),
                FindAndModifyOptions.options().upsert(true).returnNew(true), Sequencia.class);
        return Objects.requireNonNull(sequencia).valor();
    }

    @Document("sequencias")
    record Sequencia(@Id String id, long valor) {}
}
