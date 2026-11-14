package com.dinoco.oficina.catalogoservico.application;

import com.dinoco.oficina.catalogoservico.application.gateways.*;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.criar.*;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.atualizar.*;
import com.dinoco.oficina.catalogoservico.application.usecases.commands.desativar.*;
import com.dinoco.oficina.catalogoservico.application.usecases.queries.buscarporid.*;
import com.dinoco.oficina.catalogoservico.domain.Servico;
import com.dinoco.oficina.exception.RecursoNaoEncontradoException;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicoCasosDeUsoTest {

    private final ServicoCommandGateway escrita = mock(ServicoCommandGateway.class);
    private final ServicoQueryGateway consulta = mock(ServicoQueryGateway.class);

    @Test
    void deveCriarServicoAtivoEPersistirDadosInformados() {
        when(escrita.salvar(any())).thenAnswer(invocation -> {
            Servico s = invocation.getArgument(0);
            assertThat(s.getDescricao()).isEqualTo("Revisão");
            assertThat(s.getPrecoPadrao()).isEqualByComparingTo("150.50");
            assertThat(s.getTempoEstimadoMinutos()).isEqualTo(60);
            assertThat(s.getAtivo()).isTrue();
            assertThat(s.getDataCriacao()).isNotNull();
            s.setId(42L);
            return s;
        });
        var output = new CriarServicoHandler(escrita, consulta)
                .executar(new CriarServicoCommand("Revisão", new BigDecimal("150.50"), 60));
        assertThat(output).isEqualTo(new CriarServicoOutput(42L, "Revisão", new BigDecimal("150.50"), 60, true));
        verify(consulta).existePorDescricao("Revisão");
        verify(escrita).salvar(any());
    }

    @Test
    void deveRejeitarDescricaoDuplicadaSemSalvar() {
        when(consulta.existePorDescricao("Revisão")).thenReturn(true);
        assertThatThrownBy(() -> new CriarServicoHandler(escrita, consulta)
                .executar(new CriarServicoCommand("Revisão", BigDecimal.TEN, 60)))
                .isInstanceOf(IllegalArgumentException.class);
        verifyNoInteractions(escrita);
    }

    @Test
    void devePropagarFalhaNaCriacao() {
        var falha = new IllegalStateException("banco indisponível");
        when(escrita.salvar(any())).thenThrow(falha);
        assertThatThrownBy(() -> new CriarServicoHandler(escrita, consulta)
                .executar(new CriarServicoCommand("Revisão", BigDecimal.TEN, 60))).isSameAs(falha);
    }

    @Test
    void deveAtualizarPreservandoIdentidadeDataEInatividade() {
        var s = servico();
        s.desativar();
        var data = s.getDataCriacao();
        when(escrita.buscarParaAlteracao(1L)).thenReturn(Optional.of(s));
        when(escrita.salvar(s)).thenReturn(s);
        var output = new AtualizarServicoHandler(escrita)
                .executar(new AtualizarServicoCommand(1L, "Nova revisão", new BigDecimal("200"), 90));
        assertThat(output).isEqualTo(new AtualizarServicoOutput(1L, "Nova revisão", new BigDecimal("200"), 90, false));
        assertThat(s.getDataCriacao()).isEqualTo(data);
        verify(escrita).salvar(s);
    }

    @Test
    void deveRejeitarAtualizacaoInexistenteSemSalvar() {
        assertThatThrownBy(() -> new AtualizarServicoHandler(escrita)
                .executar(new AtualizarServicoCommand(99L, "Revisão", BigDecimal.TEN, 60)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
        verify(escrita, never()).salvar(any());
    }

    @Test
    void devePropagarFalhaAoSalvarAtualizacao() {
        var s = servico();
        when(escrita.buscarParaAlteracao(1L)).thenReturn(Optional.of(s));
        var falha = new IllegalStateException("falha");
        when(escrita.salvar(s)).thenThrow(falha);
        assertThatThrownBy(() -> new AtualizarServicoHandler(escrita)
                .executar(new AtualizarServicoCommand(1L, "Nova", BigDecimal.ONE, 10))).isSameAs(falha);
    }

    @Test
    void deveDesativarSemAlterarDadosCadastrais() {
        var s = servico();
        when(escrita.buscarParaAlteracao(1L)).thenReturn(Optional.of(s));
        new DesativarServicoHandler(escrita).executar(new DesativarServicoCommand(1L));
        assertThat(s.getAtivo()).isFalse();
        assertThat(s.getDescricao()).isEqualTo("Revisão");
        verify(escrita).salvar(s);
    }

    @Test
    void deveRejeitarDesativacaoInexistente() {
        assertThatThrownBy(() -> new DesativarServicoHandler(escrita).executar(new DesativarServicoCommand(99L)))
                .isInstanceOf(IllegalArgumentException.class);
        verify(escrita, never()).salvar(any());
    }

    @Test
    void deveConsultarPorIdEPreservarResultado() {
        var esperado = new BuscarServicoPorIdOutput(1L, "Revisão", BigDecimal.TEN, 60, false, servico().getDataCriacao());
        when(consulta.buscarDetalhesPorId(1L)).thenReturn(Optional.of(esperado));
        assertThat(new BuscarServicoPorIdHandler(consulta).executar(new BuscarServicoPorIdQuery(1L))).isEqualTo(esperado);
    }

    @Test
    void deveRejeitarConsultaInexistente() {
        assertThatThrownBy(() -> new BuscarServicoPorIdHandler(consulta).executar(new BuscarServicoPorIdQuery(99L)))
                .isInstanceOf(RecursoNaoEncontradoException.class);
    }

    @Test
    void deveRemoverIdsRepetidosAntesDaConsultaEmLote() {
        when(consulta.buscarDetalhesPorIds(List.of(2L, 1L))).thenReturn(List.of());
        assertThat(new BuscarServicosPorIdsHandler(consulta).executar(List.of(2L, 1L, 2L))).isEmpty();
        verify(consulta).buscarDetalhesPorIds(List.of(2L, 1L));
    }

    @Test
    void deveRejeitarLotesInvalidosAntesDeConsultar() {
        var handler = new BuscarServicosPorIdsHandler(consulta);
        for (List<Long> ids : List.of(List.<Long>of(), List.of(0L), List.of(-1L), Arrays.asList(1L, null), Collections.nCopies(101, 1L))) {
            assertThatThrownBy(() -> handler.executar(ids)).isInstanceOf(IllegalArgumentException.class);
        }
        verifyNoInteractions(consulta);
    }

    @Test
    void deveAceitarLimiteDeCemIds() {
        var ids = java.util.stream.LongStream.rangeClosed(1, 100).boxed().toList();
        new BuscarServicosPorIdsHandler(consulta).executar(ids);
        verify(consulta).buscarDetalhesPorIds(ids);
    }

    @Test
    void devePropagarFalhaNaConsultaEmLote() {
        var falha = new IllegalStateException("consulta indisponível");
        when(consulta.buscarDetalhesPorIds(List.of(1L))).thenThrow(falha);
        assertThatThrownBy(() -> new BuscarServicosPorIdsHandler(consulta).executar(List.of(1L))).isSameAs(falha);
    }

    private Servico servico() {
        var s = new Servico("Revisão", BigDecimal.TEN, 60);
        s.setId(1L);
        return s;
    }
}
