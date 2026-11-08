package com.dinoco.oficina.catalogoservico.application.usecases.commands.criar;

import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.application.gateways.ServicoQueryGateway;
import com.dinoco.oficina.catalogoservico.domain.Servico;

public class CriarServicoHandler implements CriarServicoUseCase {

    private final ServicoCommandGateway servicoCommandGateway;
    private final ServicoQueryGateway servicoQueryGateway;

    public CriarServicoHandler(ServicoCommandGateway servicoCommandGateway, ServicoQueryGateway servicoQueryGateway) {
        this.servicoCommandGateway = servicoCommandGateway;
        this.servicoQueryGateway = servicoQueryGateway;
    }

    @Override
    public CriarServicoOutput executar(CriarServicoCommand command) {

        if (servicoQueryGateway.existePorDescricao(command.descricao())) {
            throw new IllegalArgumentException("Servico já cadastrado com esta descrição.");
        }

        Servico novoServico = new Servico(
                command.descricao(),
                command.precoPadrao(),
                command.tempoEstimadoMinutos()
        );

        Servico servicoSalvo = servicoCommandGateway.salvar(novoServico);

        return mapearParaOutput(servicoSalvo);
    }

    private CriarServicoOutput mapearParaOutput(Servico servico) {

        return new CriarServicoOutput(
                servico.getId(),
                servico.getDescricao(),
                servico.getPrecoPadrao(),
                servico.getTempoEstimadoMinutos(),
                servico.getAtivo()
        );
    }
}