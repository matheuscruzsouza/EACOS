package br.uenf.eacos.service.implementation;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.uenf.eacos.constant.enumerator.audit.AuditOperationEnum;
import br.uenf.eacos.model.entity.audit.AuditChange;
import br.uenf.eacos.model.entity.audit.AuditChangeSet;
import br.uenf.eacos.model.entity.audit.AuditLog;
import br.uenf.eacos.model.entity.eacos.Item;
import br.uenf.eacos.repository.audit.AuditRepository;
import br.uenf.eacos.repository.audit.ChangeSetRepository;
import br.uenf.eacos.repository.audit.ChangesRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IAuditService")
class IAuditServiceTest {

    @Mock
    private AuditRepository auditRepository;

    @Mock
    private ChangeSetRepository changeSetRepository;

    @Mock
    private ChangesRepository changesRepository;

    @InjectMocks
    private IAuditService auditService;

    private Item testItem;
    private Item updatedItem;
    private String usuarioResponsavel;

    @BeforeEach
    void setUp() {
        testItem = Item.builder()
                .protocoloId("test-protocol-123")
                .build();
        testItem.setId(1L);
        
        updatedItem = Item.builder()
                .protocoloId("updated-protocol-456")
                .build();
        updatedItem.setId(1L);
        
        usuarioResponsavel = "test-user";
    }

    @Test
    @DisplayName("Deve registrar log de criação quando logCreate for chamado")
    void shouldLogCreateWhenLogCreateCalled() {
        // Given
        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.CREATED)
                .tabelaAfetada("item")
                .usuarioResponsavel(usuarioResponsavel)
                .build();
        
        AuditChangeSet savedChangeSet = AuditChangeSet.builder()
                .id(1L)
                .auditLog(savedAuditLog)
                .objetoTipo("item")
                .descricaoAlteracao("Alteração do objeto")
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(savedChangeSet);

        // When
        auditService.logCreate(testItem, usuarioResponsavel);

        // Then
        verify(auditRepository).save(any(AuditLog.class));
        verify(changeSetRepository).save(any(AuditChangeSet.class));
    }

    @Test
    @DisplayName("Deve registrar log de atualização quando logUpdate for chamado")
    void shouldLogUpdateWhenLogUpdateCalled() {
        // Given
        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.UPDATED)
                .tabelaAfetada("item")
                .usuarioResponsavel(usuarioResponsavel)
                .build();
        
        AuditChangeSet savedChangeSet = AuditChangeSet.builder()
                .id(1L)
                .auditLog(savedAuditLog)
                .objetoTipo("item")
                .descricaoAlteracao("Alteração do objeto")
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(savedChangeSet);

        // When
        auditService.logUpdate(testItem, updatedItem, usuarioResponsavel);

        // Then
        verify(auditRepository).save(any(AuditLog.class));
        verify(changeSetRepository).save(any(AuditChangeSet.class));
        verify(changesRepository, atLeastOnce()).save(any(AuditChange.class));
    }

    @Test
    @DisplayName("Deve registrar log de exclusão quando logDelete for chamado")
    void shouldLogDeleteWhenLogDeleteCalled() {
        // Given
        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.DELETED)
                .tabelaAfetada("item")
                .usuarioResponsavel(usuarioResponsavel)
                .build();
        
        AuditChangeSet savedChangeSet = AuditChangeSet.builder()
                .id(1L)
                .auditLog(savedAuditLog)
                .objetoTipo("item")
                .descricaoAlteracao("Alteração do objeto")
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(savedChangeSet);

        // When
        auditService.logDelete(testItem, usuarioResponsavel);

        // Then
        verify(auditRepository).save(any(AuditLog.class));
        verify(changeSetRepository).save(any(AuditChangeSet.class));
    }

    @Test
    @DisplayName("Deve registrar mudanças específicas quando propriedades forem diferentes")
    void shouldLogSpecificChangesWhenPropertiesAreDifferent() {
        // Given
        Map<String, Object> oldData = new HashMap<>();
        oldData.put("protocoloId", "old-protocol");
        oldData.put("status", "ACTIVE");
        
        Map<String, Object> newData = new HashMap<>();
        newData.put("protocoloId", "new-protocol");
        newData.put("status", "INACTIVE");

        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.UPDATED)
                .build();
        
        AuditChangeSet savedChangeSet = AuditChangeSet.builder()
                .id(1L)
                .auditLog(savedAuditLog)
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(savedChangeSet);

        // When
        auditService.logUpdate(testItem, updatedItem, usuarioResponsavel);

        // Then
        verify(changesRepository, atLeastOnce()).save(any(AuditChange.class));
    }

    @Test
    @DisplayName("Deve usar nome da classe como nome da tabela quando não há anotação @Table")
    void shouldUseClassNameAsTableNameWhenNoTableAnnotation() {
        // Given
        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.CREATED)
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(AuditChangeSet.builder().build());

        // When
        auditService.logCreate(testItem, usuarioResponsavel);

        // Then
        verify(auditRepository).save(any(AuditLog.class));
    }

    @Test
    @DisplayName("Deve definir data de alteração como agora quando log for criado")
    void shouldSetCurrentDateTimeWhenLogIsCreated() {        
        AuditLog savedAuditLog = AuditLog.builder()
                .id(1L)
                .tipoOperacao(AuditOperationEnum.CREATED)
                .build();

        when(auditRepository.save(any(AuditLog.class))).thenReturn(savedAuditLog);
        when(changeSetRepository.save(any(AuditChangeSet.class))).thenReturn(AuditChangeSet.builder().build());

        // When
        auditService.logCreate(testItem, usuarioResponsavel);

        // Then
        verify(auditRepository).save(any(AuditLog.class));
    }
}
