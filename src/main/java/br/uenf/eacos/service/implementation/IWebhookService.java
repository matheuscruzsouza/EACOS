package br.uenf.eacos.service.implementation;

import org.springframework.stereotype.Service;

import br.uenf.eacos.model.dto.VideoProcessDTO;
import br.uenf.eacos.service.ItemService;
import br.uenf.eacos.service.WebhookService;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class IWebhookService implements WebhookService {

    private final ItemService itemService;

    @Override
    public void saveVideoProcessedData(VideoProcessDTO dto) {

        dto.getItems().forEach(item -> {
            this.itemService.save(item);
        });

    }

}
