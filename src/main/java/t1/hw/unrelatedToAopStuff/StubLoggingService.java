package t1.hw.unrelatedToAopStuff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StubLoggingService {
    @Autowired private StubLoggingRepository stubLoggingRepository;

    public static class LoggingMessage{
        LocalDateTime dateTime;
        String message;
        public LoggingMessage(LocalDateTime _dateTime, String _message){
            dateTime = _dateTime; message = _message;
        }
        public LoggingMessage(){}
    }

    public void addLoggingMessage(String topic, StubLoggingService.LoggingMessage message){
        stubLoggingRepository.addLoggingMessage(topic, message);
    }

    public void printLogging(){
        var logData = stubLoggingRepository.getLogging();
        for(var key : logData.keySet()){
            System.out.println("topic:" + key);
            for(var message : logData.get(key)){
                System.out.println(message.dateTime + ":" + message.message);
            }
        }
    }
}
