package br.uenf.eacos.model.entity.eacos;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.AbstractEntity;
import br.uenf.eacos.model.entity.auth.User;
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
@Table(name = "video", schema = "eacos")
public class Video extends AbstractEntity {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User uploadedBy;

    private String title;
    private String description; 

    private String duration;
    private String latitude;
    private String longitude;
    private String location;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
    private LocalDateTime date;

    private String url;

    public static Video fromDTO(VideoDTO dto) {
        Video video = new Video();
        video.setUpdatedBy(dto.getName());
        return video;
    }

    public void updateFromDTO(VideoDTO dto) {
        this.setUpdatedBy(dto.getName());
    }

}
