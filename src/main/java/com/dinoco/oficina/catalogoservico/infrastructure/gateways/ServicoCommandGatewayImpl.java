package com.dinoco.oficina.catalogoservico.infrastructure.gateways;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.domain.Servico;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoDocument;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoMongoRepository;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoSequencia;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Component;
import java.util.Locale;
import java.util.Optional;

@Component
public class ServicoCommandGatewayImpl implements ServicoCommandGateway {

    private final ServicoMongoRepository repository;

    private final ServicoSequencia sequencia;

    public ServicoCommandGatewayImpl(ServicoMongoRepository repository, ServicoSequencia sequencia) {
        this.repository = repository;
        this.sequencia = sequencia;
    }

    @Override
    public Servico salvar(Servico servicoDominio) {
        ServicoDocument entity = mapearParaEntity(servicoDominio);
        boolean novo = entity.getId() == null;
        if (novo) entity.setId(sequencia.proximoId());
        try {
            return mapearParaDominio(novo ? repository.insert(entity) : repository.save(entity));
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Já existe um serviço com essa descrição ou identificador.", ex);
        }
    }

    @Override
    public Optional<Servico> buscarParaAlteracao(Long id) {
        return repository.findById(id).map(this::mapearParaDominio);
    }

    private ServicoDocument mapearParaEntity(Servico dominio) {
        ServicoDocument entity = new ServicoDocument();
        entity.setId(dominio.getId());
        entity.setDescricao(dominio.getDescricao());
        entity.setDescricaoNormalizada(dominio.getDescricao().toLowerCase(Locale.ROOT));
        entity.setPrecoPadrao(dominio.getPrecoPadrao());
        entity.setTempoEstimadoMinutos(dominio.getTempoEstimadoMinutos());
        entity.setAtivo(dominio.getAtivo());
        entity.setDataCriacao(dominio.getDataCriacao());
        return entity;
    }

    private Servico mapearParaDominio(ServicoDocument entity) {
        return new Servico(
                entity.getId(),
                entity.getDescricao(),
                entity.getPrecoPadrao(),
                entity.getTempoEstimadoMinutos(),
                entity.getAtivo(),
                entity.getDataCriacao()
        );
    }

}
