package care.cuddliness.clashy.command.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface CommandOptions {
        ClashyCommandOption[] value();
}
