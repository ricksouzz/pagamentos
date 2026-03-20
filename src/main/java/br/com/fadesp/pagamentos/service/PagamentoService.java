package br.com.fadesp.pagamentos.service;

import br.com.fadesp.pagamentos.model.Pagamento;
import br.com.fadesp.pagamentos.model.StatusPagamento;
import br.com.fadesp.pagamentos.model.TipoPagamento;
import br.com.fadesp.pagamentos.model.dto.PagamentoRequestDTO;
import br.com.fadesp.pagamentos.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository repository;

    public Pagamento receberPagamento(PagamentoRequestDTO dto) {
        if ((dto.tipoPagamento() == TipoPagamento.CARTAO_CREDITO ||
                dto.tipoPagamento() == TipoPagamento.CARTAO_DEBITO) &&
                (dto.numeroCartao() == null || dto.numeroCartao().isBlank())) {
            throw new IllegalArgumentException("Número do cartão é obrigatório para este método de pagamento.");
        }

        Pagamento pagamento = new Pagamento();
        pagamento.setCodigoDebito(dto.codigoDebito());
        pagamento.setCpfCnpj(dto.cpfCnpj());
        pagamento.setTipoPagamento(dto.tipoPagamento());
        pagamento.setNumeroCartao(dto.numeroCartao());
        pagamento.setValor(dto.valor());

        pagamento.setStatus(StatusPagamento.PENDENTE_PROCESSAMENTO);
        pagamento.setAtivo(true);

        return repository.save(pagamento);
    }

    public Pagamento atualizarStatus(Long id, StatusPagamento novoStatus) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        if (!pagamento.isAtivo()) {
            throw new IllegalArgumentException("Não é possível alterar o status de um pagamento inativo.");
        }

        StatusPagamento statusAtual = pagamento.getStatus();

        if (statusAtual == StatusPagamento.PROCESSADO_COM_SUCESSO) {
            throw new IllegalStateException("Pagamentos processados com sucesso não podem ter o status alterado.");
        }

        if (statusAtual == StatusPagamento.PROCESSADO_COM_FALHA && novoStatus != StatusPagamento.PENDENTE_PROCESSAMENTO) {
            throw new IllegalStateException("Pagamentos com falha só podem retornar para Pendente de Processamento.");
        }

        if (statusAtual == StatusPagamento.PENDENTE_PROCESSAMENTO &&
                (novoStatus != StatusPagamento.PROCESSADO_COM_SUCESSO && novoStatus != StatusPagamento.PROCESSADO_COM_FALHA)) {
            throw new IllegalStateException("Transição de status inválida a partir de Pendente de Processamento.");
        }

        pagamento.setStatus(novoStatus);
        return repository.save(pagamento);
    }

    public List<Pagamento> listarTodos() {
        return repository.findAllByAtivoTrue();
    }

    public List<Pagamento> filtrarPorCodigoDebito(Integer codigoDebito) {
        return repository.findByCodigoDebitoAndAtivoTrue(codigoDebito);
    }

    public List<Pagamento> filtrarPorCpfCnpj(String cpfCnpj) {
        return repository.findByCpfCnpjAndAtivoTrue(cpfCnpj);
    }

    public List<Pagamento> filtrarPorStatus(StatusPagamento status) {
        return repository.findByStatusAndAtivoTrue(status);
    }

    public void excluirPagamento(Long id) {
        Pagamento pagamento = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Pagamento não encontrado."));

        if (pagamento.getStatus() != StatusPagamento.PENDENTE_PROCESSAMENTO) {
            throw new IllegalStateException("Apenas pagamentos com status Pendente de Processamento podem ser excluídos.");
        }

        pagamento.setAtivo(false);
        repository.save(pagamento);
    }
}