package care.cuddliness.stacy.command.annotation;

import care.cuddliness.stacy.command.data.StacyCommandInterface;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface StacySubCommandComponent {

    Class<? extends StacyCommandInterface> parent();
    String subCommandId();

    String subCommandGroupid() default "";

}
