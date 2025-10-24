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
@Table(name = "item_classe", schema = "eacos")
public class ItemClasse extends AbstractEntity {

    private String name;

}
