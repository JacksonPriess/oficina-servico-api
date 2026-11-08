package com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.domain.Servico;
import com.dinoco.oficina.exception.RecursoNaoEncontradoException;

public class AtualizarServicoHandler implements AtualizarServicoUseCase {

    private final ServicoCommandGateway servicoCommandGateway;

    // Injeta apenas o gateway de escrita (Command)
    public AtualizarServicoHandler(ServicoCommandGateway servicoCommandGateway) {
        this.servicoCommandGateway = servicoCommandGateway;
    }

    @Override
    public AtualizarServicoOutput executar(AtualizarServicoCommand command) {

        Servico servico = servicoCommandGateway.buscarParaAlteracao(command.id())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Servico não encontrado."));

        servico.atualizar(
                command.descricao(),
                command.precoPadrao(),
                command.tempoEstimadoMinutos()
        );

        Servico servicoSalvo = servicoCommandGateway.salvar(servico);
        return mapearParaOutput(servicoSalvo);
    }

    private AtualizarServicoOutput mapearParaOutput(Servico servico) {
        return new AtualizarServicoOutput(
                servico.getId(),
                servico.getDescricao(),
                servico.getPrecoPadrao(),
                servico.getTempoEstimadoMinutos(),
                servico.getAtivo()
        );
    }
}