package care.cuddliness.stacy;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@EnableAutoConfiguration
@SpringBootApplication
public class StacyApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext stacyApplication = new SpringApplicationBuilder(StacyApplication.class).web(
                WebApplicationType.NONE).run(args);

    }
}
