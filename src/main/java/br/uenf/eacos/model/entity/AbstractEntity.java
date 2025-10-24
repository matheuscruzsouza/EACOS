package br.uenf.eacos.model.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.uenf.eacos.constant.enumerator.model.entity.EntityStatusEnum;
import br.uenf.eacos.listener.AuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditListener.class)
public abstract class AbstractEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "created_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, 
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", 
        locale = "pt-BR", 
        timezone = "Brazil/East")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @JsonFormat(shape = JsonFormat.Shape.STRING, 
        pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", 
        locale = "pt-BR", 
        timezone = "Brazil/East")
    private LocalDateTime updatedAt;

    @Column(name = "created_by")
    protected String createdBy;

    @Column(name = "updated_by")
    protected String updatedBy;

    @Default
    @Column(name = "entity_status")
    @Enumerated(EnumType.STRING)
    protected EntityStatusEnum entityStatus = EntityStatusEnum.ACTIVE;

    @PrePersist
    public void setCreationDate() {
        this.createdAt = LocalDateTime.now();
    }

    @PreUpdate
    public void setChangeDate() {
        this.updatedAt = LocalDateTime.now();
    }

}
