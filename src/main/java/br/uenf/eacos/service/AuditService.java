package br.uenf.eacos.service;

public interface AuditService {

    void logCreate(Object entity, String usuarioResponsavel);

    void logUpdate(Object oldEntity, Object newEntity, String usuarioResponsavel);

    void logDelete(Object entity, String usuarioResponsavel);

}
