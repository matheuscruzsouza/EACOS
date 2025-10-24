package br.uenf.eacos.repository.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uenf.eacos.model.entity.audit.AuditChange;

@Repository
public interface ChangesRepository extends JpaRepository<AuditChange, String> {

}
