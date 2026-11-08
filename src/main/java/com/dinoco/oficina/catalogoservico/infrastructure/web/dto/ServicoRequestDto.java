package com.dinoco.oficina.catalogoservico.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ServicoRequestDto(
        @NotBlank(message = "A descrição do serviço é obrigatória.")
        String descricao,

        @NotNull(message = "O preço padrão é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        BigDecimal precoPadrao,

        @NotNull(message = "O tempo estimado é obrigatório.")
        @PositiveOrZero(message = "O tempo estimado não pode ser negativo.")
        Integer tempoEstimadoMinutos
) {}
