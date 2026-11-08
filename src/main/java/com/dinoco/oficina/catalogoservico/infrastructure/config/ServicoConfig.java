package com.dinoco.oficina.catalogoservico.infrastructure.config;

import com.dinoco.oficina.catalogoservico.adapters.controllers.ServicoControllerClean;
import com.dinoco.oficina.catalogoservico.application.gateways.ServicoCommandGateway;
import com.dinoco.oficina.catalogoservico.application.gateways.ServicoQueryGateway;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoHandler;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoHandler;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoHandler;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoUseCase;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdHandler;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServicoConfig {

    @Bean
    public CriarServicoUseCase criarServicoUseCase(ServicoCommandGateway servicoCommandGateway, ServicoQueryGateway servicoQueryGateway) {
        return new CriarServicoHandler(servicoCommandGateway, servicoQueryGateway);
    }

    @Bean
    public AtualizarServicoUseCase atualizarServicoUseCase(ServicoCommandGateway commandGateway) {
        return new AtualizarServicoHandler(commandGateway);
    }

    @Bean
    public DesativarServicoUseCase desativarServicoUseCase(ServicoCommandGateway commandGateway) {
        return new DesativarServicoHandler(commandGateway);
    }

    @Bean
    public BuscarServicoPorIdUseCase buscarServicoPorIdUseCase(ServicoQueryGateway queryGateway) {
        return new BuscarServicoPorIdHandler(queryGateway);
    }

    @Bean
    public ServicoControllerClean servicoControllerClean(
            CriarServicoUseCase criarServicoUseCase,
            AtualizarServicoUseCase atualizarServicoUseCase,
            DesativarServicoUseCase desativarServicoUseCase,
            BuscarServicoPorIdUseCase buscarServicoPorIdUseCase) {
        return new ServicoControllerClean(criarServicoUseCase, atualizarServicoUseCase, desativarServicoUseCase, buscarServicoPorIdUseCase);
    }
}
