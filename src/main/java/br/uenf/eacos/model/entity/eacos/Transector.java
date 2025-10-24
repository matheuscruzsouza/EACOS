package br.uenf.eacos.model.entity.eacos;

import br.uenf.eacos.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "transector", schema = "eacos")
public class Transector extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video videoId;

    private Long latitude;

    private Long longitude;

    private String beachId;
    
}
