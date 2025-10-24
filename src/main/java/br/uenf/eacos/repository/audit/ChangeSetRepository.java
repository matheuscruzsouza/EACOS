package br.uenf.eacos.repository.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.uenf.eacos.model.entity.audit.AuditChangeSet;

@Repository
public interface ChangeSetRepository extends JpaRepository<AuditChangeSet, String> {

}
