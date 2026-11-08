package com.dinoco.oficina.catalogoservico.infrastructure.web.mapper;


import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import com.dinoco.oficina.catalogoservico.infrastructure.web.dto.ServicoRequestDto;
import com.dinoco.oficina.catalogoservico.infrastructure.web.dto.ServicoResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ServicoWebMapper {

    //Converte do DTO da Web para o Input do Core
    CriarServicoCommand toInput(ServicoRequestDto dto);

    //Converte do Output do Core para o DTO da Web
    ServicoResponseDto toResponse(CriarServicoOutput output);

    //Converte do DTO da Web para o Input do Core
    AtualizarServicoCommand toAtualizarCommand(Long id, ServicoRequestDto dto);

    //Converte do Output do Core para o DTO da Web
    ServicoResponseDto toAtualizarResponse(AtualizarServicoOutput output);

    DesativarServicoCommand toDesativarCommand(Long id);

    ServicoResponseDto toQueryResponse(BuscarServicoPorIdOutput output);
}