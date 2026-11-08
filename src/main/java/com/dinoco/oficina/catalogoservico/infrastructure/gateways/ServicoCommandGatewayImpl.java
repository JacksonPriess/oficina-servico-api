package com.dinoco.oficina.catalogoservico.infrastructure.gateways;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.domain.Servico;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoEntity;
import com.dinoco.oficina.catalogoservico.infrastructure.persistence.ServicoJpaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class ServicoCommandGatewayImpl implements ServicoCommandGateway {

    private final ServicoJpaRepository jpaRepository;

    public ServicoCommandGatewayImpl(ServicoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Servico salvar(Servico servicoDominio) {
        ServicoEntity entity = mapearParaEntity(servicoDominio);
        ServicoEntity salvo = jpaRepository.save(entity);
        return mapearParaDominio(salvo);
    }

    @Override
    public Optional<Servico> buscarParaAlteracao(Long id) {
        return jpaRepository.findById(id).map(this::mapearParaDominio);
    }

    private ServicoEntity mapearParaEntity(Servico dominio) {
        ServicoEntity entity = new ServicoEntity();
        entity.setId(dominio.getId());
        entity.setDescricao(dominio.getDescricao());
        entity.setPrecoPadrao(dominio.getPrecoPadrao());
        entity.setTempoEstimadoMinutos(dominio.getTempoEstimadoMinutos());
        entity.setAtivo(dominio.getAtivo());
        entity.setDataCriacao(dominio.getDataCriacao());
        return entity;
    }

    private Servico mapearParaDominio(ServicoEntity entity) {
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
