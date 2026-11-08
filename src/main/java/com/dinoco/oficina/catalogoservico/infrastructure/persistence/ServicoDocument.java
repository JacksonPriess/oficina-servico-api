package com.dinoco.oficina.catalogoservico.infrastructure.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document("servicos")
@Getter
@Setter
public class ServicoDocument {

    @Id
    private Long id;

    private String descricao;

    @Indexed(name = "descricaoNormalizada_1", unique = true)
    private String descricaoNormalizada;

    @Field(targetType = FieldType.DECIMAL128)
    private BigDecimal precoPadrao;

    private Integer tempoEstimadoMinutos;
    private Boolean ativo;
    private LocalDateTime dataCriacao;
}
