package com.example.demo.kafkaBasedServices;

import com.example.demo.config.KafkaTopicConfig;
import com.example.demo.entities.BLClientEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaRowObtainedFromTableConsumerService {
    @KafkaListener(topics = KafkaTopicConfig.Topics.ROW_OBTAINED_FROM_TABLE, groupId = KafkaTopicConfig.Groups.COMMON_GROUP)
    public void listen(@Payload Object message, Acknowledgment acknowledgment) {
        String jsonMessage = (String)((ConsumerRecord)message).value();
        ObjectMapper objectMapper = new ObjectMapper();
        BLClientEntity entity = null;
        try {
            entity = objectMapper.readValue(jsonMessage, BLClientEntity.class);
            System.out.println("and after long way we've finally received the message:");
            System.out.println(entity);
            acknowledgment.acknowledge();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
