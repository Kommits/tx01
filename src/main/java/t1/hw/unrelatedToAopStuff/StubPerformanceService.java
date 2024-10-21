package t1.hw.unrelatedToAopStuff;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class StubPerformanceService {
    @Autowired private StubPerformanceRepository performanceRepository;

    public static class PerformancePoint{
        String name;
        LocalDateTime start;
        LocalDateTime end;
        public PerformancePoint(String _name, LocalDateTime _start, LocalDateTime _end){
            name = _name; start = _start; end = _end;
        }
        public PerformancePoint(){}
    }

    public LocalDateTime startPerformancePoint(String performancePointName){
        var now = LocalDateTime.now();
        var pp = new PerformancePoint();
        pp.name = performancePointName;
        pp.start = now;
        pp.end = null;
        performanceRepository.addPerformancePoint(performancePointName,pp);
        return now;
    }

    public LocalDateTime endPerformancePoint(String performancePointName){
        var now = LocalDateTime.now();
        var pp = performanceRepository.getPerformancePoints().getOrDefault(performancePointName,null);
        if(pp == null) return null;
        pp.end = now;
        return now;
    }

    private static Long localDateTimeDiffToMS(LocalDateTime end, LocalDateTime start){
        long perf = ((end.toEpochSecond(ZoneOffset.UTC) * 1_000_000) + (end.getNano() / 1_000))
                - ((start.toEpochSecond(ZoneOffset.UTC) * 1_000_000) + (start.getNano() / 1_000));
        return perf;
    }

    public Long getPerformance(String performancePointName){
        var pp = performanceRepository.getPerformancePoints().getOrDefault(performancePointName,null);
        if(pp == null) return -1L;
        if(pp.start == null || pp.end == null) return -1L;
        return localDateTimeDiffToMS(pp.end,pp.start);
    }

    public void printPerformances(){
        for(var key : performanceRepository.getPerformancePoints().keySet()){
            var pp = performanceRepository.getPerformancePoints().get(key);
            if(pp.start == null || pp.end == null) continue;
            var perf = localDateTimeDiffToMS(pp.end, pp.start);
            System.out.print("\t\t performance point: ");
            System.out.println("" + pp.name + ":" + perf);
        }
    }
}
