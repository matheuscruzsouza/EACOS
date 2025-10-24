package br.uenf.eacos.model.entity.eacos;

import br.uenf.eacos.model.entity.AbstractEntity;
import jakarta.persistence.Entity;
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
@Table(name = "machine_learning_model", schema = "eacos")
public class MLModel extends AbstractEntity {

    private String model;
    private String version;
    private String description;

}
