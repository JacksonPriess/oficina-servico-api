package com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoQueryGateway;

import java.util.List;

public class BuscarServicosPorIdsHandler {

    private final ServicoQueryGateway gateway;

    public BuscarServicosPorIdsHandler(ServicoQueryGateway gateway) {
        this.gateway = gateway;
    }

    public List<BuscarServicoPorIdOutput> executar(List<Long> ids) {
        if (ids.isEmpty() || ids.size() > 100 || ids.stream().anyMatch(id -> id == null || id <= 0)) {
            throw new IllegalArgumentException("Informe de 1 a 100 IDs de serviços positivos.");
        }
        return gateway.buscarDetalhesPorIds(ids.stream().distinct().toList());
    }
}
