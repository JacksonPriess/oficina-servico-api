package com.dinoco.oficina.catalogoservico.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoJpaRepository extends JpaRepository<ServicoEntity, Long> {
    boolean existsByDescricaoIgnoreCase(String nome);
}