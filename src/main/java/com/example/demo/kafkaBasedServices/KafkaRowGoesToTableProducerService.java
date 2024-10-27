package com.example.demo.kafkaBasedServices;

import com.example.demo.config.KafkaTopicConfig;
import com.example.demo.entities.BLClientEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRowGoesToTableProducerService {
    private final KafkaTemplate<String, BLClientEntity> kafkaTemplate;

    public KafkaRowGoesToTableProducerService(KafkaTemplate<String, BLClientEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(BLClientEntity message) {
        if(message == null) return;
        kafkaTemplate.send(KafkaTopicConfig.Topics.ROW_GOES_TO_TABLE, message);
    }
}
