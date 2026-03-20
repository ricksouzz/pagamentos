package br.com.fadesp.pagamentos.service;

import br.com.fadesp.pagamentos.model.Pagamento;
import br.com.fadesp.pagamentos.model.StatusPagamento;
import br.com.fadesp.pagamentos.model.TipoPagamento;
import br.com.fadesp.pagamentos.model.dto.PagamentoRequestDTO;
import br.com.fadesp.pagamentos.repository.PagamentoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PagamentoServiceTest {

    @Mock
    private PagamentoRepository repository;

    @InjectMocks
    private PagamentoService service;

    @Test
    @DisplayName("Deve lançar exceção ao tentar pagar com cartão sem informar o número")
    void receberPagamento_ComCartaoSemNumero_DeveLancarExcecao() {
        PagamentoRequestDTO dto = new PagamentoRequestDTO(
                123, "12345678901", TipoPagamento.CARTAO_CREDITO, null, new BigDecimal("100.00")
        );

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            service.receberPagamento(dto);
        });

        assertEquals("Número do cartão é obrigatório para este método de pagamento.", exception.getMessage());
        verify(repository, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve salvar novo pagamento com status PENDENTE_PROCESSAMENTO e ativo")
    void receberPagamento_ComSucesso_DeveSalvarComStatusPendente() {
        PagamentoRequestDTO dto = new PagamentoRequestDTO(
                123, "12345678901", TipoPagamento.PIX, null, new BigDecimal("100.00")
        );
        Pagamento pagamentoSalvo = new Pagamento(1L, 123, "12345678901", TipoPagamento.PIX, null, new BigDecimal("100.00"), StatusPagamento.PENDENTE_PROCESSAMENTO, true);

        when(repository.save(any(Pagamento.class))).thenReturn(pagamentoSalvo);

        Pagamento resultado = service.receberPagamento(dto);

        assertNotNull(resultado);
        assertEquals(StatusPagamento.PENDENTE_PROCESSAMENTO, resultado.getStatus());
        assertTrue(resultado.isAtivo());
        verify(repository, times(1)).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve impedir alteração de status se o pagamento já estiver PROCESSADO_COM_SUCESSO")
    void atualizarStatus_DeSucessoParaFalha_DeveLancarExcecao() {
        Pagamento pagamento = new Pagamento(1L, 123, "12345678901", TipoPagamento.PIX, null, new BigDecimal("100.00"), StatusPagamento.PROCESSADO_COM_SUCESSO, true);
        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            service.atualizarStatus(1L, StatusPagamento.PROCESSADO_COM_FALHA);
        });

        assertEquals("Pagamentos processados com sucesso não podem ter o status alterado.", exception.getMessage());
        verify(repository, never()).save(any(Pagamento.class));
    }

    @Test
    @DisplayName("Deve realizar exclusão lógica mudando o status para inativo se estiver PENDENTE_PROCESSAMENTO")
    void excluirPagamento_ComStatusPendente_DeveInativar() {
        Pagamento pagamento = new Pagamento(1L, 123, "12345678901", TipoPagamento.PIX, null, new BigDecimal("100.00"), StatusPagamento.PENDENTE_PROCESSAMENTO, true);

        when(repository.findById(1L)).thenReturn(Optional.of(pagamento));
        when(repository.save(any(Pagamento.class))).thenReturn(pagamento);

        service.excluirPagamento(1L);

        assertFalse(pagamento.isAtivo());
        verify(repository, times(1)).save(pagamento);
    }
}