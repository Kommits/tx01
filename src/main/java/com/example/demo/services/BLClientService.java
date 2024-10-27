package com.example.demo.services;

import com.example.demo.entities.BLClientEntity;
import com.example.demo.kafkaBasedServices.KafkaRowGoesToTableProducerService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class BLClientService {
    private final KafkaRowGoesToTableProducerService kafkaRowGoesToTableProducerService;

    public ArrayList<BLClientEntity> loadEntities(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<BLClientEntity> clients = new ArrayList<>();
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("clients.json")) {
            if (inputStream == null) throw new Exception();
            clients = objectMapper.readValue(inputStream, new TypeReference<ArrayList<BLClientEntity>>() {});
        } catch (Exception e) {
            e.printStackTrace();
        }
        return clients;
    }

    public void fromResourceFileToKafka(){
        var entities = loadEntities();
        entities.forEach(kafkaRowGoesToTableProducerService::sendMessage);
    }
}
