package br.uenf.eacos.model.entity.eacos;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.uenf.eacos.constant.enumerator.model.entity.RecordingStatusEnum;
import br.uenf.eacos.model.dto.ItemDTO;
import br.uenf.eacos.model.entity.AbstractEntity;
import br.uenf.eacos.model.entity.auth.User;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "item", schema = "eacos")
public class Item extends AbstractEntity {

    @Default
    private String protocoloId = UUID.randomUUID().toString();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User userId;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video videoId;

    @ManyToOne
    @JoinColumn(name = "transector_id")
    private Transector transectorId;

    @ManyToOne
    @JoinColumn(name = "item_classe_id")
    private ItemClasse classeId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime timestamp;

    private Long latitude;

    private Long longitude;

    @Enumerated(EnumType.STRING)
    private RecordingStatusEnum status;

    public static Item fromDTO(ItemDTO item) {
        Item t = new Item();
        t.setUpdatedBy(item.getName());
        return t;
    }

    public void updateFromDTO(ItemDTO item) {
        this.setUpdatedBy(item.getName());
    }

}
