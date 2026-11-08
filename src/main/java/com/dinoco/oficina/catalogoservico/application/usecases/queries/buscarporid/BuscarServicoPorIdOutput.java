package com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record BuscarServicoPorIdOutput(
        Long id,
        String descricao,
        BigDecimal precoPadrao,
        Integer tempoEstimadoMinutos,
        Boolean ativo,
        LocalDateTime dataCriacao
) {}
