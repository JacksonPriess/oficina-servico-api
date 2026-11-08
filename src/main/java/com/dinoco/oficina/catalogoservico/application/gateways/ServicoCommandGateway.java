package com.dinoco.oficina.catalogoservico.application.gateways;

import com.dinoco.oficina.catalogoservico.domain.Servico;
import java.util.Optional;

public interface ServicoCommandGateway {
    Servico salvar(Servico servico);
    Optional<Servico> buscarParaAlteracao(Long id);
}