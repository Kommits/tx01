package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Service;

// заглушки есть заглушки
// никакого отношения к заданию они по сути не имеют
// поэтому я их кучей просто в этом пакадже свалил
// добавив мизер функционала

@Service
public class StubAuthService {
    public static String defaultSystemServiceToken =
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ3b3JkIjoia3Jhc2F2Y2hpY2siLCJleHBpcmF0aW9uIjoxNjEyMTg1ODAwfQ.5RtoKcq4-AVuM1SO2_jP2fPlmvn8SkJPPpiAqjY5ZQA";
    public boolean isTokenAuthorized(String token){
        if(token != null) return true;
        return false;
    }
}
