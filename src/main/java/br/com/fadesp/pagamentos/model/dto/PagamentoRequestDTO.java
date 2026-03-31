package br.com.fadesp.pagamentos.model.dto;

import br.com.fadesp.pagamentos.model.enums.TipoPagamento;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PagamentoRequestDTO(
        @NotNull(message = "O código do débito é obrigatório.")
        Integer codigoDebito,

        @NotBlank(message = "O CPF/CNPJ é obrigatório.")
        @Size(min = 11, max = 14, message = "O CPF/CNPJ deve ter entre 11 e 14 caracteres.")
        String cpfCnpj,

        @NotNull(message = "O tipo de pagamento é obrigatório.")
        TipoPagamento tipoPagamento,

        String numeroCartao,

        @NotNull(message = "O valor é obrigatório.")
        @Positive(message = "O valor deve ser maior que zero.")
        BigDecimal valor
) {}