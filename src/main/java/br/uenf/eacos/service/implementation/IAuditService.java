package br.uenf.eacos.service.implementation;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import br.uenf.eacos.constant.enumerator.audit.AuditOperationEnum;
import br.uenf.eacos.model.entity.audit.AuditChange;
import br.uenf.eacos.model.entity.audit.AuditChangeSet;
import br.uenf.eacos.model.entity.audit.AuditLog;
import br.uenf.eacos.repository.audit.AuditRepository;
import br.uenf.eacos.repository.audit.ChangeSetRepository;
import br.uenf.eacos.repository.audit.ChangesRepository;
import br.uenf.eacos.service.AuditService;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class IAuditService implements AuditService {

    private final AuditRepository auditRepository;
    private final ChangeSetRepository changeSetRepository;
    private final ChangesRepository changesRepository;

    @Override
    public void logCreate(Object entity, String usuarioResponsavel) {
        String tableName = getTableName(entity.getClass());
        logEntity(tableName, AuditOperationEnum.CREATED, null, entity, usuarioResponsavel);
    }

    @Override
    public void logUpdate(Object oldEntity, Object newEntity, String usuarioResponsavel) {
        String tableName = getTableName(oldEntity.getClass());
        logEntity(tableName, AuditOperationEnum.UPDATED, newEntity, oldEntity, usuarioResponsavel);
    }

    @Override
    public void logDelete(Object entity, String usuarioResponsavel) {
        String tableName = getTableName(entity.getClass());
        logEntity(tableName, AuditOperationEnum.DELETED, entity, null, usuarioResponsavel);
    }
    private String getTableName(Class<?> clazz) {
        try {
            return clazz.getSimpleName().toLowerCase();
        } catch (Exception e) {
            Table annotation = clazz.getAnnotation(Table.class);
            return annotation != null ? annotation.name() : 
                   clazz.getSimpleName().toLowerCase();
        }
    }

    @SuppressWarnings("unchecked")
    private void logEntity(String tabelaAfetada, AuditOperationEnum tipoOperacao, Object dadosAntes, Object dadosDepois, String usuarioResponsavel) {
        AuditLog log = new AuditLog();
        log.setTabelaAfetada(tabelaAfetada);
        log.setTipoOperacao(tipoOperacao);
        log.setDataAlteracao(LocalDateTime.now());
        log.setUsuarioResponsavel(usuarioResponsavel);
        
        // Convertendo objetos para JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            log.setDadosAntes(mapper.writeValueAsString(dadosAntes));
            log.setDadosDepois(mapper.writeValueAsString(dadosDepois));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Erro ao converter objeto para JSON", e);
        }
        
        // Salvando o log principal
        AuditLog savedLog = auditRepository.save(log);
        
        // Criando conjunto de alterações
        AuditChangeSet changeSet = new AuditChangeSet();
        changeSet.setAuditLog(savedLog);
        changeSet.setObjetoTipo(tabelaAfetada);
        changeSet.setDescricaoAlteracao("Alteração do objeto");
        
        AuditChangeSet savedChangeSet = changeSetRepository.save(changeSet);
        
        // Registrando mudanças específicas
        if (dadosAntes != null && dadosDepois != null) {
            Map<String, Object> antesMap = (Map<String, Object>) mapper.convertValue(dadosAntes, Map.class);
            Map<String, Object> depoisMap = (Map<String, Object>) mapper.convertValue(dadosDepois, Map.class);
            
            antesMap.forEach((propriedade, valorAntigo) -> {
                Object valorNovo = depoisMap.get(propriedade);
                if (!Objects.equals(valorAntigo, valorNovo)) {
                    AuditChange change = new AuditChange();
                    change.setChangeSet(savedChangeSet);
                    change.setPropriedade(propriedade.toString());
                    change.setValorAntigo(valorAntigo.toString());
                    change.setValorNovo(valorNovo.toString());
                    
                    changesRepository.save(change);
                }
            });
        }
    }
}
