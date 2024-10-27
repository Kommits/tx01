package com.example.demo;

import com.example.demo.config.KafkaTopicConfig;
import com.example.demo.kafkaBasedServices.KafkaTopicTestingProducerService;
import com.example.demo.services.BLClientService;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Properties;

@SpringBootApplication
@RequiredArgsConstructor
public class DemoApplication implements CommandLineRunner {
	private final ApplicationContext ctx;
	// <- для примера со стримами
	private final KafkaTemplate<String, String> kafkaTemplate;

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		//
		// (-1-) сценарий с отправкой тестового сообщения для проверки работы кафки:
		// -------------------------------------------------------------------
		var topicTestingProducer = ctx.getBean(KafkaTopicTestingProducerService.class);
		topicTestingProducer.sendMessage("my message :" + LocalDateTime.now());

		//
		// (-2-) сценарий с бизнес логикой из домашней работы (также как и в GET контроллере):
		// -----------------------------------------------------------------------------
		var clientService = ctx.getBean(BLClientService.class);
		clientService.fromResourceFileToKafka();

		//
		// (-3-) сценарий с КStream:
		// (размещу in_place чтобы не засорять структуру)
		// общая логика кафка стримов это поставить middleware который будет перехватывать
		// данные посланные с одного топика - и чтото с ними делать (у нас направлять на другой топик)
		//
		// если брать чисто логическую архитектуру этого действа то это фактически .flatMap(...)
		// где каждая новая врезка конструкции это переход на какой нибудь топик:
		// <- простой перестрим (как в примере)
		// 		.flatMap(eventTopicA -> emitEventTopicB)
		// <- два стрима из одного
		// 		.flatMap(eventTopicB -> emitEventTopicC,emitEventTopicD)
		// <- объединение стримов
		// 		.flatMap(eventTopicC, eventTopicD -> emitEventFinal)
		// --------------------------------------------------

		// создаем пропсы стрима:
		var p = new Properties();
		p.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG,"localhost:9092");
		p.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafka-streams");
		p.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
		p.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());

		// делаем стрим под топик:
		var sb = new StreamsBuilder();
		KStream<String,String> sourceStream = sb.stream(KafkaTopicConfig.Topics.STREAMS_TOPIC);

		// делаем обработчик для стрима:
		KStream<String,String> streamWithHandler = sourceStream.mapValues((el) -> {
			return el.toUpperCase();
		});
		// и перестримливаем его в другое место
		// <- на TOPIC_TESTING уже есть хандлер вывода, поэтому туда
		streamWithHandler.to(KafkaTopicConfig.Topics.TOPIC_TESTING);

		// устанавливаем стрим в виде middleware
		var streams = new KafkaStreams(sb.build(),p);
		streams.start();

		// так как middleware по сути установлен
		// скормим в стрим который подсоединен к topic_input данные
		List.of("--- one","--- two","--- thr").forEach(
				el -> kafkaTemplate.send(KafkaTopicConfig.Topics.STREAMS_TOPIC,el)
		);

		// не забываем грохнуть middleware
		Runtime.getRuntime().addShutdownHook(new Thread(streams::close));
	}
}
