package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class StubPerformanceRepository {
    private final HashMap<String /*name*/, StubPerformanceService.PerformancePoint /*mark*/> performancePointHashMap
    = new HashMap<>();

    void addPerformancePoint(String name, StubPerformanceService.PerformancePoint performancePoint){
        if(performancePoint == null) return;
        performancePointHashMap.put(name,performancePoint);
    }

    Map<String, StubPerformanceService.PerformancePoint> getPerformancePoints(){
        return performancePointHashMap;
    }
}
