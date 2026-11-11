package com.dinoco.oficina.catalogoservico.domain;

import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServicoTest {

    @Test
    void deveAtualizarDadosSemReativarServicoOuAlterarDataDeCriacao() {
        var s = new Servico("Revisão", BigDecimal.TEN, 60);
        s.setId(1L);
        var data = s.getDataCriacao();
        s.desativar();
        s.atualizar("Revisão completa", new BigDecimal("200.50"), 120);
        assertThat(s.getId()).isEqualTo(1L);
        assertThat(s.getDataCriacao()).isEqualTo(data);
        assertThat(s.getAtivo()).isFalse();
        assertThat(s.getDescricao()).isEqualTo("Revisão completa");
        assertThat(s.getPrecoPadrao()).isEqualByComparingTo("200.50");
        assertThat(s.getTempoEstimadoMinutos()).isEqualTo(120);
    }

    @Test
    void devePermitirDesativacaoRepetida() {
        var s = new Servico("Revisão", BigDecimal.TEN, 60);
        s.desativar();
        s.desativar();
        assertThat(s.getAtivo()).isFalse();
        assertThat(s.getPrecoPadrao()).isEqualTo(BigDecimal.TEN);
    }
}
