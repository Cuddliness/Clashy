package care.cuddliness.clashy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class ClashyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext clashyApplication = new SpringApplicationBuilder(ClashyApplication.class).web(
                WebApplicationType.NONE).run(args);

    }
}
