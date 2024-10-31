package t1.homework.services;

import org.springframework.stereotype.Service;

//
// это тестовый сервис чтобы посмотреть что пропертис корректно гуляют между приложениями:
@Service
public class AdequateTestingService {
    private Boolean logging;
    private Integer level;

    public AdequateTestingService(Boolean _logging, Integer _level){
        logging = _logging; level = _level;
    }

    public void printProperties(){
        System.out.println(logging);
        System.out.println(level);
    }
}
