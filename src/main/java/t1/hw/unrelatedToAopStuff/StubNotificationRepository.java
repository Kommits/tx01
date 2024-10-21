package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Repository
public class StubNotificationRepository {
    private final HashMap<String /*addressee*/, ArrayList<StubNotificationService.Notification>> notifications
    = new HashMap<>();

    void addNotificationMessage(String addressee, StubNotificationService.Notification notification){
        if(notification == null) return;
        if(notifications.get(addressee) == null) {
            notifications.put(addressee, new ArrayList<>());
        }
        notifications.get(addressee).add(notification);
    }

    Map<String, ArrayList<StubNotificationService.Notification>> getNotifications(){
        return notifications;
    }
}
