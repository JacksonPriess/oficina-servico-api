package com.dinoco.oficina.catalogoservico.infrastructure.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;

public record ServicoRequestDto(
        @NotBlank(message = "A descrição do serviço é obrigatória.")
        @Size(max = 255)
        String descricao,

        @NotNull(message = "O preço padrão é obrigatório.")
        @PositiveOrZero(message = "O preço não pode ser negativo.")
        @Digits(integer = 8, fraction = 2)
        BigDecimal precoPadrao,

        @NotNull(message = "O tempo estimado é obrigatório.")
        @PositiveOrZero(message = "O tempo estimado não pode ser negativo.")
        Integer tempoEstimadoMinutos
) {}
