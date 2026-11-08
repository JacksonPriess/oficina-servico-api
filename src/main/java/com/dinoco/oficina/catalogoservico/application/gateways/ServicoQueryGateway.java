package com.dinoco.oficina.catalogoservico.application.gateways;

import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import java.util.Optional;
import java.util.List;

public interface ServicoQueryGateway {

    List<BuscarServicoPorIdOutput> buscarDetalhesPorIds(List<Long> ids);

    boolean existePorDescricao(String descricao);
    Optional<BuscarServicoPorIdOutput> buscarDetalhesPorId(Long id);
}