package com.dinoco.oficina.catalogoservico.application.usecases.commands.criar;

import java.math.BigDecimal;

public record CriarServicoCommand(
        String descricao,
        BigDecimal precoPadrao,
        Integer tempoEstimadoMinutos
) {}
