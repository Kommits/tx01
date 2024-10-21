package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StubLoggingRepository {
    private final
    HashMap<String /*topic*/, ArrayList<StubLoggingService.LoggingMessage>> logData = new HashMap<>();

    void addLoggingMessage(String topic, StubLoggingService.LoggingMessage message){
        if(message == null) return;
        if(logData.get(topic) == null) {
            logData.put(topic, new ArrayList<>());
        }
        logData.get(topic).add(message);
    }

    Map<String, ArrayList<StubLoggingService.LoggingMessage>> getLogging(){
        return logData;
    }
}
