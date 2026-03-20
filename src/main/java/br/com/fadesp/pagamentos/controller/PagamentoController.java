package br.com.fadesp.pagamentos.controller;

import br.com.fadesp.pagamentos.model.Pagamento;
import br.com.fadesp.pagamentos.model.StatusPagamento;
import br.com.fadesp.pagamentos.model.dto.AtualizaStatusDTO;
import br.com.fadesp.pagamentos.model.dto.PagamentoRequestDTO;
import br.com.fadesp.pagamentos.service.PagamentoService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pagamentos")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService service;

    @PostMapping
    public ResponseEntity<Pagamento> receberPagamento(@RequestBody PagamentoRequestDTO dto) {
        Pagamento pagamentoCriado = service.receberPagamento(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pagamentoCriado);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<Pagamento> atualizarStatus(
            @PathVariable Long id,
            @RequestBody AtualizaStatusDTO dto) {
        Pagamento pagamentoAtualizado = service.atualizarStatus(id, dto.status());
        return ResponseEntity.ok(pagamentoAtualizado);
    }

    @GetMapping
    public ResponseEntity<List<Pagamento>> listarPagamentos(
            @RequestParam(required = false) Integer codigoDebito,
            @RequestParam(required = false) String cpfCnpj,
            @RequestParam(required = false) StatusPagamento status) {

        List<Pagamento> pagamentos;

        if (codigoDebito != null) {
            pagamentos = service.filtrarPorCodigoDebito(codigoDebito);
        } else if (cpfCnpj != null) {
            pagamentos = service.filtrarPorCpfCnpj(cpfCnpj);
        } else if (status != null) {
            pagamentos = service.filtrarPorStatus(status);
        } else {
            pagamentos = service.listarTodos();
        }

        return ResponseEntity.ok(pagamentos);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPagamento(@PathVariable Long id) {
        service.excluirPagamento(id);
        return ResponseEntity.noContent().build();
    }
}
