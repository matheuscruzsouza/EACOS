package br.uenf.eacos.model.entity.audit;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.uenf.eacos.constant.enumerator.audit.AuditOperationEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "audit_log", schema = "log")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AuditOperationEnum tipoOperacao;

    @JsonFormat(shape = JsonFormat.Shape.STRING, 
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", 
        locale = "pt-BR", 
        timezone = "Brazil/East")
    private LocalDateTime  dataAlteracao;

    private String tabelaAfetada;

    private String usuarioResponsavel;

    @Lob
    @Column(columnDefinition = "text")
    private String dadosAntes;

    @Lob
    @Column(columnDefinition = "text")
    private String dadosDepois;

}
