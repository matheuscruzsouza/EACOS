package br.uenf.eacos.repository.audit;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uenf.eacos.model.entity.audit.AuditLog;

@Repository
public interface AuditRepository extends JpaRepository<AuditLog, String> {

    List<AuditLog> findByTabelaAfetada(String tabela);
    List<AuditLog> findByTipoOperacao(String tipoOperacao);

}
