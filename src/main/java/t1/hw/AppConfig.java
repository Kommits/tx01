package t1.hw;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy  // <- как в анекдоте, где вся конструкция держиться на одной подпорке, в АОП вот эта подпорка
public class AppConfig {}
