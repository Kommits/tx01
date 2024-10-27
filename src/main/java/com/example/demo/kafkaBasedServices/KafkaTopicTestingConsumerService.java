package com.example.demo.kafkaBasedServices;

//
// я не вешаю консумацию на уровне класса @KafkaListener(..) + @KafkaHandler(isDefault=...)
// буду вешать её на метод
//
// и я указываю common-group для всех листенеров -
// группа это по сути
// чтото типа ИДЕНТИФИКАТОРА ИТЕРАТОРА для топиков при чтении (у меня тут 1-к-1 везде,
// поэтому везде будет одинаковая группа) -- в оригинале offset/group мне кажется только путает
// (сравните iteratorId="eventIteratorOne") <- но по сути именно это и имеется ввиду
//
// и группа приходит из TopicСonfig - что наверное не очень хорошо семантически (группа из топика)
// но так как она вообще не используется и одна на всех : пусть будет так
// (я мог бы переименовать в KafkaConfig -- но это будет снова не корректно - так как
// разработчики рекомендуют делать отдельные конфиги на каждый случай жизни : топики отдельно, админа отдельно и тд.)
//
// и по умолчанию акнолидж ставится автоматически (мы его отключаем в конфиге),
// но надо понимать - что весь блок с акнолиджем был бы пропущен по умолчанию, то есть эта логика излишняя
//   kafka:
//    listener:
//      ack-mode: MANUAL_IMMEDIATE
//    consumer:
//      enable-auto-commit: false

import com.example.demo.config.KafkaTopicConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaTopicTestingConsumerService {
    private final boolean debugMe = false;

    @KafkaListener(topics = KafkaTopicConfig.Topics.TOPIC_TESTING, groupId = KafkaTopicConfig.Groups.COMMON_GROUP)
    public void listen(@Payload String message, Acknowledgment acknowledgment) {
        boolean canAck = true;

        System.out.println("Received message with a confirmation: " + message);

        // это у нас не смотря на то что какая то глупость происходит
        // очень важный кусок в разработке на кафке
        // (я не могу брекпоинт переслать в виде пулл реквеста)
        // это debug для внешней среды:
        // для того чтобы проверить что нам в кафку свалилось
        //
        // ставим здесь задержку : чтобы была возможность посмотреть
        // внешними средствами что в сообщениях чтото находится
        // (для просмотра того что внутри кафки я использую OffsetExplorer)
        // и после этого вырубаем приложение, эмулируя классику распределенной системы

        if(debugMe) try{Thread.sleep(10_000_000);}catch(Exception e){}

        // ну и после ребута приложения и снятия задержки мы естественно получаем
        // сообщения ДВА (прошлое которое застряло - и новое отправленное)

        // с этого момента можно быть увереным
        // что кафка работает адекватно : акнолидж не отправляется тогда когда не надо
        // а по умолчанию напомню он отправляется сам по себе
        // (что кстати такой себе момент и вилы тут можно получить очень быстро)
        // ну и данные приходят в том количестве что нам надо
        // то есть теперь можно к бизнес логике переходить, а наш паттерн консюмер-продюсер шаблонизировать

        if(canAck){
            acknowledgment.acknowledge();
        }
    }
}
