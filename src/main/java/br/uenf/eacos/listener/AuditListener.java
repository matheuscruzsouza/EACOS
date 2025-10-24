package br.uenf.eacos.listener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.springframework.stereotype.Component;

import br.uenf.eacos.service.AuditService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuditListener {
    private static EntityManager entityManager;
    private static AuditService auditService;

    public static void setAuditService(
        AuditService service,
        EntityManager entityManager
    ) {
        AuditListener.auditService = service;
        AuditListener.entityManager = entityManager;
    }

    @PrePersist
    private void beforeCreate(Object object) {
        log.info("Antes de salvar um objeto no banco de dados.");
        auditService.logCreate(object, null);
    }

    @PreUpdate
    private void beforeUpdate(Object object) {
        log.info("Antes de atualizar um objeto no banco de dados.");
        Object original = this.clone(object);

        Object oldObject = entityManager.find(object.getClass(), getEntityId(object));
        entityManager.refresh(oldObject);

        Object obj = this.clone(oldObject);

        entityManager.merge(original);

        auditService.logUpdate(oldObject, obj, null);
    }


    @PreRemove
    private void beforeDelete(Object object) {
        log.info("Antes de remover um objeto do banco de dados.");
        auditService.logDelete(object, null);
    }

    private String getEntityId(Object entity) {
        try {
            return entity.getClass().getMethod("getId").invoke(entity).toString();
        } catch (Exception e) {
            throw new RuntimeException("Failed to get entity ID", e);
        }
    }

    private Object clone(Object object) {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(object);
            oos.flush();
            oos.close();
            bos.close();
            byte[] byteData = bos.toByteArray();
            ByteArrayInputStream bis = new ByteArrayInputStream(byteData);
            ObjectInputStream ois = new ObjectInputStream(bis);
            return ois.readObject();
        } catch (Exception e) {
            return object;
        }
    }

}
