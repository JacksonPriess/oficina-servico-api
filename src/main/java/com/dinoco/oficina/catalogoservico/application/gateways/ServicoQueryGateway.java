package com.dinoco.oficina.catalogoservico.application.gateways;

import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import java.util.Optional;

public interface ServicoQueryGateway {

    boolean existePorDescricao(String descricao);
    Optional<BuscarServicoPorIdOutput> buscarDetalhesPorId(Long id);
}