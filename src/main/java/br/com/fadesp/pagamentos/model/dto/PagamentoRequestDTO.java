package br.com.fadesp.pagamentos.model.dto;

import br.com.fadesp.pagamentos.model.TipoPagamento;
import java.math.BigDecimal;

public record PagamentoRequestDTO(
        Integer codigoDebito,
        String cpfCnpj,
        TipoPagamento tipoPagamento,
        String numeroCartao,
        BigDecimal valor
) {}