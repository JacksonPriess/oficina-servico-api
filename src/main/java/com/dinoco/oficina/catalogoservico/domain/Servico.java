package com.dinoco.oficina.catalogoservico.domain;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Servico {

    private Long id;
    private String descricao;
    private BigDecimal precoPadrao;
    private Integer tempoEstimadoMinutos;
    private Boolean ativo;
    private final LocalDateTime dataCriacao;

    public Servico(String descricao, BigDecimal precoPadrao, Integer tempoEstimadoMinutos) {
        this.descricao = descricao;
        this.precoPadrao = precoPadrao;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.ativo = true;
        this.dataCriacao = LocalDateTime.now();
    }

    public Servico(Long id, String descricao, BigDecimal precoPadrao,
                   Integer tempoEstimadoMinutos, Boolean ativo, LocalDateTime dataCriacao) {
        this.id = id;
        this.descricao = descricao;
        this.precoPadrao = precoPadrao;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;

    }

    public void desativar() {
        this.ativo = false;
    }

    public void atualizar(String descricao, BigDecimal precoPadrao, Integer tempoEstimadoMinutos) {
        this.descricao = descricao;
        this.precoPadrao = precoPadrao;
        this.tempoEstimadoMinutos = tempoEstimadoMinutos;
    }

}
