package com.example.demo.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

//
// это наши топики:
//
// я делаю:
// (-1-) тестовый сценарий, чтобы проверить. что все вообще работает (я приложение последовательно же собираю)
//
// и я использую для устранения magic статические поля, потому что
// у меня стоит не юльтимейт версия, а комьюнити, поэтому я ставлю просто переменные, а не тяну их в виде value,
// (я мог бы поставить и @Value, но я все таки хочу показать как я приложение делаю - как есть)
//
// и проблема основная кафки - как я вижу - это то что вместо того чтобы сделать один флоу конфигурирования
// там их восемь - поэтому я буду очень много комментариев оставлять (кафка очень не очевидное флоу имеет)
// и я специально не буду использовать флоу господина руководителя (так как он сам по себе законченый)
//
// Потом, почему я начинаю с тестового блока
// я использую классический флоу разработки под стороннюю систему:
//  (-а-) разработать примитивный сценарий для взаимодействия с внешней системой : если он не работает проблема не в BL
//  (-б-) разработать бизнес логику : на базе примитивного сценария (который проще отладить и шаблонизировать)
//  (-в-) на то время что хватит - добавлять дополнительный функционал (у меня это стримы)
//
// (-2-) после этого топик для домашней работы чтобы через него выполнить задание (-б-)
// (-3-) после чего топик для стримов (-в-)

@Configuration
public class KafkaTopicConfig {

    public static class Topics{

        //
        // для тестового сенария
        public static final String TOPIC_TESTING = "topic-testing";

        //
        // для сценария BL:
        // у нас будет аж два события (чтобы уже совсем разорвать логику исполнения):
        // первое что объект отправляется на сохранение в базу
        // и второе что обьект из базы изьят (по сценарию BL обогащенный идентификатором)
        public static final String ROW_GOES_TO_TABLE = "row_goes_to_table";
        public static final String ROW_OBTAINED_FROM_TABLE = "row_obtained_from_table";

        // для стримов:
        public static final String STREAMS_TOPIC = "streams-topic";
    }

    public static class Groups{
        public static final String COMMON_GROUP = "common-group";
    }

    @Bean
    public NewTopic topicTesting() {
        return TopicBuilder
                .name(Topics.TOPIC_TESTING)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic rowGoesToTable() {
        return TopicBuilder
                .name(Topics.ROW_GOES_TO_TABLE)
                .partitions(1).
                replicas(1)
                .build();
    }
    @Bean
    public NewTopic rowObtainedFromTable() {
        return TopicBuilder
                .name(Topics.ROW_OBTAINED_FROM_TABLE)
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic streamsTopic() {
        return TopicBuilder
                .name(Topics.STREAMS_TOPIC)
                .partitions(1)
                .replicas(1)
                .build();
    }
}
