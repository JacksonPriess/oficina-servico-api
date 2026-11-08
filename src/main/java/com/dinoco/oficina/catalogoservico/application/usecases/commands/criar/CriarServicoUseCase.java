package com.dinoco.oficina.catalogoservico.application.usecases.commands.criar;

public interface CriarServicoUseCase {
    CriarServicoOutput executar(CriarServicoCommand input);
}