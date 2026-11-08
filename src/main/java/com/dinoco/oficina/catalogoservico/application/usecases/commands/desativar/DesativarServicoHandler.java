package com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.domain.Servico;

public class DesativarServicoHandler implements DesativarServicoUseCase {

    private final ServicoCommandGateway servicoCommandGateway;

    public DesativarServicoHandler(ServicoCommandGateway servicoCommandGateway) {
        this.servicoCommandGateway = servicoCommandGateway;
    }

    @Override
    public void executar(DesativarServicoCommand command) {
        Servico servico = servicoCommandGateway.buscarParaAlteracao(command.id())
                .orElseThrow(() -> new IllegalArgumentException("Servico não encontrado."));
        servico.desativar();
        servicoCommandGateway.salvar(servico);
    }
}
