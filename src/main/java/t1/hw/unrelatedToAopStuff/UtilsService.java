package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Service;

@Service
public class UtilsService {
    private final long delay = 111L;

    public void delayMe(){
        try{Thread.sleep(delay);}catch(Exception e){}
    }
}
