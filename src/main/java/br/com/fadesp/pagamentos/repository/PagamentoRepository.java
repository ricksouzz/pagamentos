package br.com.fadesp.pagamentos.repository;

import br.com.fadesp.pagamentos.model.Pagamento;
import br.com.fadesp.pagamentos.model.enums.StatusPagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, Long> {
    List<Pagamento> findAllByAtivoTrue();
    List<Pagamento> findByCodigoDebitoAndAtivoTrue(Integer codigoDebito);
    List<Pagamento> findByCpfCnpjAndAtivoTrue(String cpfCnpj);
    List<Pagamento> findByStatusAndAtivoTrue(StatusPagamento status);
}
