package t1.hw.unrelatedToAopStuff;

import org.springframework.stereotype.Service;

@Service
public class StubSystemDiagnosticsService {
    private boolean getStatusNetwork(){
        return true;
    }

    private boolean getStatusProtocolSecurity(){
        return true;
    }

    private boolean getStatusAntiFraudFilter(){
        return true;
    }

    public boolean isSystemReady(){
        return getStatusNetwork() && getStatusProtocolSecurity() && getStatusAntiFraudFilter();
    }

    public void printSystemDiagnostics(){
        System.out.printf("""
                Network status: %s
                Protocol status: %s
                Anti fraud filter: %s
                """, getStatusNetwork(),getStatusProtocolSecurity(),getStatusAntiFraudFilter());
    }
}
