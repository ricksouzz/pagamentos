package br.com.fadesp.pagamentos.model.dto;

import br.com.fadesp.pagamentos.model.StatusPagamento;

public record AtualizaStatusDTO(
        StatusPagamento status
) {}
