package com.example.demo.kafkaBasedServices;

import com.example.demo.config.KafkaTopicConfig;
import com.example.demo.entities.BLClientEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaRowObtainedFromTableProducerService {
    private final KafkaTemplate<String, BLClientEntity> kafkaTemplate;

    public KafkaRowObtainedFromTableProducerService(KafkaTemplate<String, BLClientEntity> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(BLClientEntity message) {
        if(message == null) return;

        kafkaTemplate.send(KafkaTopicConfig.Topics.ROW_OBTAINED_FROM_TABLE, message);
    }
}


