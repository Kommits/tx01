package t1.homework.configs;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import t1.homework.aspects.AopCuts;
import t1.homework.services.AdequateTestingService;

//
// здесь мы создаем бины на базе конфигурационных пропертиз:
@Configuration
@EnableConfigurationProperties(AspectStarterProperties.class)
public class AspectStarterAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public AdequateTestingService adequateTestingService(AspectStarterProperties properties){
        return new AdequateTestingService(properties.getLogging(),properties.getLevel());
    }

    @Bean
    @ConditionalOnMissingBean
    public AopCuts aopCuts(AspectStarterProperties properties){
        return new AopCuts(properties.getLogging(),properties.getLevel());
    }
}
