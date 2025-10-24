package br.uenf.eacos.service.implementation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.eacos.Video;
import br.uenf.eacos.repository.VideoRepository;
import br.uenf.eacos.service.VideoService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IVideoService implements VideoService {

    private final VideoRepository videoRepository;

    @Override
    public Page<Video> findAll(Pageable pageable) {
        return this.videoRepository.findAll(pageable);
    }

    @Override
    public Video save(VideoDTO dto) {
        Video video = Video.fromDTO(dto);
        return this.videoRepository.saveAndFlush(video);
    }

    @Override
    public Video update(Long id, VideoDTO dto) {
        Video v = this.videoRepository.findById(id).orElseThrow(
            () -> new RuntimeException("Video entity not found")
        );

        v.updateFromDTO(dto);

        return this.videoRepository.saveAndFlush(v);
    }


}
