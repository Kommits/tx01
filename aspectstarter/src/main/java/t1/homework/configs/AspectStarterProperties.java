package t1.homework.configs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

//
// здесь мы читаем пропертиз из раздела стартера:
@ConfigurationProperties(prefix = "aspectstarter")
public class AspectStarterProperties {
    @Getter @Setter private Boolean logging;
    @Getter @Setter private Integer level;
}
