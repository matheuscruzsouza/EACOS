package br.uenf.eacos.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.eacos.Video;

public interface VideoService {

    Page<Video> findAll(Pageable pageable);

    Video save(VideoDTO video);

    Video update(Long id, VideoDTO video);

}
