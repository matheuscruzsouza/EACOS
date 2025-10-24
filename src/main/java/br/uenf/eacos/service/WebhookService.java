package br.uenf.eacos.service;

import br.uenf.eacos.model.dto.VideoProcessDTO;

public interface WebhookService {

    void saveVideoProcessedData(VideoProcessDTO dto);

}
