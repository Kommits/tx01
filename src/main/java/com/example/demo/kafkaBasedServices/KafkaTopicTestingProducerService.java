package com.example.demo.kafkaBasedServices;

import com.example.demo.config.KafkaTopicConfig;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

//
// с этого продюсера я начинаю разработку чтобы удостовериться что кафка вообще работает
// у нас здесь дефлтный темплейт и ДИ внедрит то что нам нужно просто по типу

@Service
public class KafkaTopicTestingProducerService {
    private final KafkaTemplate<String, String> kafkaTemplate;
    public KafkaTopicTestingProducerService(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }
    public void sendMessage(String message) {
        if(message == null) return;

        //
        // отсечка крайне важна - так как за нею в консоли пойдет отладка кафки по пресылке
        System.out.println("----------- Sending message: -------------" + message);

        //
        // сюда же также можно вставить коллбек после отправки (мы делать этого не будем):
        kafkaTemplate.send(KafkaTopicConfig.Topics.TOPIC_TESTING, message);
    }
}
