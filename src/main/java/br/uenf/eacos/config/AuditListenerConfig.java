package br.uenf.eacos.config;

import org.springframework.context.annotation.Configuration;

import br.uenf.eacos.listener.AuditListener;
import br.uenf.eacos.service.AuditService;
import jakarta.persistence.EntityManager;

@Configuration
public class AuditListenerConfig {

    public AuditListenerConfig(
        AuditService auditService, 
        EntityManager entityManager
    ) {
        AuditListener.setAuditService(
            auditService, 
            entityManager
        );
    }

}
