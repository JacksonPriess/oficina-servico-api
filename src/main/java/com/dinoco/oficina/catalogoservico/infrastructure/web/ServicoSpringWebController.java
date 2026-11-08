package com.dinoco.oficina.catalogoservico.infrastructure.web;

import com.dinoco.oficina.catalogoservico.adapters.controllers.ServicoControllerClean;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.AtualizarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.CriarServicoOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.DesativarServicoCommand;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdOutput;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.BuscarServicoPorIdQuery;
import com.dinoco.oficina.catalogoservico.infrastructure.web.dto.ServicoRequestDto;
import com.dinoco.oficina.catalogoservico.infrastructure.web.dto.ServicoResponseDto;
import com.dinoco.oficina.catalogoservico.infrastructure.web.mapper.ServicoWebMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;

@Tag(name = "6. Serviços", description = "Cadastro de serviço")
@RestController
@RequestMapping("api/servicos")
public class ServicoSpringWebController {

    private final ServicoControllerClean controllerClean;
    private final ServicoWebMapper mapper;

    public ServicoSpringWebController(ServicoControllerClean controllerClean, ServicoWebMapper mapper) {
        this.controllerClean = controllerClean;
        this.mapper = mapper;
    }

    @Operation(summary = "Cadastrar serviço")
    @PostMapping
    public ResponseEntity<ServicoResponseDto> criar(@RequestBody @Valid ServicoRequestDto request) {
        CriarServicoCommand input = mapper.toInput(request);
        CriarServicoOutput output = controllerClean.criarServico(input);

        ServicoResponseDto response = mapper.toResponse(output);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @Operation(summary = "Atualizar serviço")
    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> atualizar(@PathVariable Long id, @RequestBody @Valid ServicoRequestDto request) {
        AtualizarServicoCommand command = mapper.toAtualizarCommand(id, request);
        AtualizarServicoOutput output = controllerClean.atualizarServico(command);
        ServicoResponseDto response = mapper.toAtualizarResponse(output);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Desativar serviço")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        DesativarServicoCommand command = mapper.toDesativarCommand(id);
        controllerClean.desativarServico(command);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Buscar serviço por código")
    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDto> buscarPorId(@PathVariable Long id) {
        BuscarServicoPorIdQuery query = new BuscarServicoPorIdQuery(id);
        BuscarServicoPorIdOutput output = controllerClean.buscarPorId(query);
        ServicoResponseDto response = mapper.toQueryResponse(output);
        return ResponseEntity.ok(response);
    }
}