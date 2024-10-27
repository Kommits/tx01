package com.example.demo.kafkaBasedServices;

import com.example.demo.config.KafkaTopicConfig;
import com.example.demo.entities.BLClientEntity;
import com.example.demo.repositories.BLClientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

//
// для наглядности - я буду сериализовать, но не буду ничего десериализировать через ДИ
// (десериализация задается также как сериализация или в настройках приложения
// или добавлением отдельного маппера в флоу обработки кафки) - в приложении же я её специально отключил
// и приму сообщение прямо как его поставляет нам кафка и потом его разберу
//
// надо понимать что через один канал мы можем отправлять что угодно в принципе
// и в любой комбинации
// и канал никак не привязан к какому то отдельному типу, типам или даже ЦЕЛИКОМ типу
// мы там можем стримить например куски байт какие нибудь разорванные (и все в одном канале)
// и поэтому по хорошему бы их вообще отправлять через
// value = Json{dataType:ourDataType,dataPart:1,dataHash:..,dataSign:..,bytes:...реальные данные здесь...}
//
// это не все, -
// вторая проблема с десериализацией заключается в том
// что при входящих не понятных данных - встроенная сериализация будет просто падать
// так что тут (-a-) или пишем две строки лишние в консюмере, (-b-) или ленимся и вставляем её на уровень
// приложения, (-c-) или пишем собственный меппер и вставляем его в флоу кафки (пример господина руководителя)

@Service
@RequiredArgsConstructor
public class KafkaRowGoesToTableConsumerService {
    private final BLClientRepository clientRepository;
    private final KafkaRowObtainedFromTableProducerService kafkaRowObtainedFromTableProducerService;

    @KafkaListener(topics = KafkaTopicConfig.Topics.ROW_GOES_TO_TABLE, groupId = KafkaTopicConfig.Groups.COMMON_GROUP)
    public void listen(@Payload Object message, Acknowledgment acknowledgment) {
        String jsonMessage = (String)((ConsumerRecord)message).value();
        boolean canAck = false;

        ObjectMapper objectMapper = new ObjectMapper();
        BLClientEntity entity = null;
        try {
            entity = objectMapper.readValue(jsonMessage, BLClientEntity.class);
            entity = clientRepository.save(entity);
            canAck = true;
        } catch (Exception e) {
            // с автоматическим акнолиджем мы бы здесь оказались в
            // крайне не приятной ситуации
            e.printStackTrace();
        }

        // получили энтити шлем его дальше по цепочке уже с обогащением
        kafkaRowObtainedFromTableProducerService.sendMessage(entity);

        if(canAck){
            acknowledgment.acknowledge();
        }
    }
}
