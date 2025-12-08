package care.cuddliness.stacy.command.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface CommandOptions {
        StacyCommandOption[] value();
}
