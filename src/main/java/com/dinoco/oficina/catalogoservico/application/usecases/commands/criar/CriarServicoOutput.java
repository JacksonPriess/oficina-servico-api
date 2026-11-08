package com.dinoco.oficina.catalogoservico.application.usecases.commands.criar;

import java.math.BigDecimal;

public record CriarServicoOutput(
        Long id,
        String descricao,
        BigDecimal precoPadrao,
        Integer tempoEstimadoMinutos,
        Boolean ativo
) {}
