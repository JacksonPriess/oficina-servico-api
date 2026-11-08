package com.dinoco.oficina.catalogoservico.adapters.controllers;

import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdQuery;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdUseCase;

/**
 * Orquestra commands e queries
 */
public class ServicoControllerClean {

    private final CriarServicoUseCase criarServicoUseCase;
    private final AtualizarServicoUseCase atualizarServicoUseCase;
    private final DesativarServicoUseCase desativarServicoUseCase;
    private final BuscarServicoPorIdUseCase buscarServicoPorIdUseCase;

    public ServicoControllerClean(CriarServicoUseCase criarServicoUseCase, AtualizarServicoUseCase atualizarServicoUseCase, DesativarServicoUseCase desativarServicoUseCase, BuscarServicoPorIdUseCase buscarServicoPorIdUseCase) {
        this.criarServicoUseCase = criarServicoUseCase;
        this.atualizarServicoUseCase = atualizarServicoUseCase;
        this.desativarServicoUseCase = desativarServicoUseCase;
        this.buscarServicoPorIdUseCase = buscarServicoPorIdUseCase;
    }

    public CriarServicoOutput criarServico(CriarServicoCommand command) {
        return criarServicoUseCase.executar(command);
    }

    public AtualizarServicoOutput atualizarServico(AtualizarServicoCommand command) {
        return atualizarServicoUseCase.executar(command);
    }

    public void desativarServico(DesativarServicoCommand command) {
        desativarServicoUseCase.executar(command);
    }

    public BuscarServicoPorIdOutput buscarPorId(BuscarServicoPorIdQuery query) {
        return buscarServicoPorIdUseCase.executar(query);
    }
}