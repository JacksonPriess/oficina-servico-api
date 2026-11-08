package com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoQueryGateway;
import com.dinoco.oficina.exception.RecursoNaoEncontradoException;

public class BuscarServicoPorIdHandler implements BuscarServicoPorIdUseCase {

    private final ServicoQueryGateway servicoQueryGateway;

    public BuscarServicoPorIdHandler(ServicoQueryGateway servicoQueryGateway) {
        this.servicoQueryGateway = servicoQueryGateway;
    }

    @Override
    public BuscarServicoPorIdOutput executar(BuscarServicoPorIdQuery query) {
        return servicoQueryGateway.buscarDetalhesPorId(query.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico não encontrado com ID: " + query.id()));
    }
}