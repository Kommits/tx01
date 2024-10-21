package t1.hw.unrelatedToAopStuff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StubNotificationService {
    @Autowired private StubNotificationRepository stubNotificationRepository;

    public static class Notification{
        String addressee;
        LocalDateTime date;
        String textOf;
        public Notification(String _addressee, LocalDateTime _date, String _textOf){
            addressee = _addressee; date = _date; textOf = _textOf;
        }
        public Notification(){}
    }

    public void sendNotification(String addressee, Notification notification){
        stubNotificationRepository.addNotificationMessage(addressee,notification);
    }

    public void printNotifications(){
        var notifications = stubNotificationRepository.getNotifications();
        for(var key : notifications.keySet()){
            System.out.println("addressee:" + key);
            for(var notification : notifications.get(key)){
                System.out.println(notification.date + ":" + notification.addressee + ":" + notification.textOf);
            }
        }
    }
}
