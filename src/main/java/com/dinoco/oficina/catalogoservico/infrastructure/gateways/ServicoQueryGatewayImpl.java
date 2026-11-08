package com.dinoco.oficina.catalogoservico.infrastructure.gateways;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoQueryGateway;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoDocument;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoMongoRepository;
import org.springframework.stereotype.Component;
import java.util.Optional;
import java.util.List;
import java.util.Locale;

@Component
public class ServicoQueryGatewayImpl implements ServicoQueryGateway {

    private final ServicoMongoRepository repository;

    public ServicoQueryGatewayImpl(ServicoMongoRepository repository) {
        this.repository = repository;
    }

    @Override
    public boolean existePorDescricao(String descricao) {
        return repository.existsByDescricaoNormalizada(descricao.toLowerCase(Locale.ROOT));
    }

    @Override
    public Optional<BuscarServicoPorIdOutput> buscarDetalhesPorId(Long id) {
        return repository.findById(id).map(this::mapearParaOutputVisual);
    }

    @Override
    public List<BuscarServicoPorIdOutput> buscarDetalhesPorIds(List<Long> ids) {
        return repository.findAllById(ids).stream().map(this::mapearParaOutputVisual).toList();
    }

    private BuscarServicoPorIdOutput mapearParaOutputVisual(ServicoDocument entity) {
        // Devolve o Record DTO que a UseCase/Query exige
        return new BuscarServicoPorIdOutput(
                entity.getId(),
                entity.getDescricao(),
                entity.getPrecoPadrao(),
                entity.getTempoEstimadoMinutos(),
                entity.getAtivo(),
                entity.getDataCriacao()
        );
    }
}