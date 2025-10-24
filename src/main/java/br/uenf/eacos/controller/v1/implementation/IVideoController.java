package br.uenf.eacos.controller.v1.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import br.uenf.eacos.controller.v1.VideoController;
import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.eacos.Video;
import br.uenf.eacos.service.VideoService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class IVideoController implements VideoController {

    private final VideoService videoService;

    public ResponseEntity<Page<Video>> findAll(
        Integer page,
        Integer size
    ) {
        return ResponseEntity.ok(
            this.videoService.findAll(
                PageRequest.of(page, size)));
    }

    public ResponseEntity<Video> save(
        VideoDTO video
    ) {
        return ResponseEntity.ok(
            this.videoService.save(video));
    }

    public ResponseEntity<Video> update(
        Long id,
        VideoDTO video
    ) {
        return ResponseEntity.ok(
            this.videoService.update(id, video));
    }

}
