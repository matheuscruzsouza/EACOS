package br.uenf.eacos.service.implementation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.uenf.eacos.model.dto.VideoDTO;
import br.uenf.eacos.model.entity.eacos.Video;
import br.uenf.eacos.repository.VideoRepository;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes unitários para IVideoService")
class IVideoServiceTest {

    @Mock
    private VideoRepository videoRepository;

    @InjectMocks
    private IVideoService videoService;

    private Video video;
    private VideoDTO videoDTO;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        video = Video.builder().build();
        video.setId(1L);
        
        videoDTO = new VideoDTO("Test Video");
        
        pageable = PageRequest.of(0, 10);
    }

    @Test
    @DisplayName("Deve retornar página de vídeos quando findAll for chamado")
    void shouldReturnPageOfVideosWhenFindAllCalled() {
        // Given
        Page<Video> expectedPage = new PageImpl<>(java.util.List.of(video));
        when(videoRepository.findAll(pageable)).thenReturn(expectedPage);

        // When
        Page<Video> result = videoService.findAll(pageable);

        // Then
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(video, result.getContent().get(0));
        verify(videoRepository).findAll(pageable);
    }

    @Test
    @DisplayName("Deve salvar vídeo quando save for chamado com VideoDTO válido")
    void shouldSaveVideoWhenSaveCalledWithValidVideoDTO() {
        // Given
        Video savedVideo = Video.builder().build();
        savedVideo.setId(1L);
        
        when(videoRepository.saveAndFlush(any(Video.class))).thenReturn(savedVideo);

        // When
        Video result = videoService.save(videoDTO);

        // Then
        assertNotNull(result);
        assertEquals(savedVideo.getId(), result.getId());
        verify(videoRepository).saveAndFlush(any(Video.class));
    }

    @Test
    @DisplayName("Deve atualizar vídeo existente quando update for chamado com ID válido")
    void shouldUpdateExistingVideoWhenUpdateCalledWithValidId() {
        // Given
        Long videoId = 1L;
        Video existingVideo = Video.builder().build();
        existingVideo.setId(videoId);
        
        Video updatedVideo = Video.builder().build();
        updatedVideo.setId(videoId);

        when(videoRepository.findById(videoId)).thenReturn(Optional.of(existingVideo));
        when(videoRepository.saveAndFlush(any(Video.class))).thenReturn(updatedVideo);

        // When
        Video result = videoService.update(videoId, videoDTO);

        // Then
        assertNotNull(result);
        assertEquals(videoId, result.getId());
        verify(videoRepository).findById(videoId);
        verify(videoRepository).saveAndFlush(any(Video.class));
    }

    @Test
    @DisplayName("Deve lançar RuntimeException quando update for chamado com ID inexistente")
    void shouldThrowRuntimeExceptionWhenUpdateCalledWithNonExistentId() {
        // Given
        Long nonExistentId = 999L;
        when(videoRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // When & Then
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> videoService.update(nonExistentId, videoDTO));
        
        assertEquals("Video entity not found", exception.getMessage());
        verify(videoRepository).findById(nonExistentId);
        verify(videoRepository, never()).saveAndFlush(any(Video.class));
    }

    @Test
    @DisplayName("Deve chamar updateFromDTO quando update for executado")
    void shouldCallUpdateFromDTOWhenUpdateExecuted() {
        // Given
        Long videoId = 1L;
        Video existingVideo = spy(Video.builder().build());
        existingVideo.setId(videoId);
        
        when(videoRepository.findById(videoId)).thenReturn(Optional.of(existingVideo));
        when(videoRepository.saveAndFlush(any(Video.class))).thenReturn(existingVideo);

        // When
        videoService.update(videoId, videoDTO);

        // Then
        verify(existingVideo).updateFromDTO(videoDTO);
    }

    @Test
    @DisplayName("Deve chamar fromDTO quando save for executado")
    void shouldCallFromDTOWhenSaveExecuted() {
        // Given
        Video savedVideo = Video.builder().build();
        savedVideo.setId(1L);
        
        when(videoRepository.saveAndFlush(any(Video.class))).thenReturn(savedVideo);

        // When
        videoService.save(videoDTO);

        // Then
        verify(videoRepository).saveAndFlush(any(Video.class));
    }
}