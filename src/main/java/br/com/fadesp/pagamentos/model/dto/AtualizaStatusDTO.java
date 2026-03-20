package br.com.fadesp.pagamentos.model.dto;

import br.com.fadesp.pagamentos.model.StatusPagamento;
import jakarta.validation.constraints.NotNull;

public record AtualizaStatusDTO(
        @NotNull(message = "O novo status é obrigatório.")
        StatusPagamento status
) {}
