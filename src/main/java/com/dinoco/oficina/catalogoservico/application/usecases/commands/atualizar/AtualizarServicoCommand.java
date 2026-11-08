package com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar;

import java.math.BigDecimal;

public record AtualizarServicoCommand(
        Long id,
        String descricao,
        BigDecimal precoPadrao,
        Integer tempoEstimadoMinutos
) {}
