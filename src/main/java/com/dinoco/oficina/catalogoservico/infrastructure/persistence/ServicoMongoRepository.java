package com.dinoco.oficina.catalogoservico.infrastructure.persistence;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface ServicoMongoRepository extends MongoRepository<ServicoDocument, Long> {

    boolean existsByDescricaoNormalizada(String descricaoNormalizada);
}
