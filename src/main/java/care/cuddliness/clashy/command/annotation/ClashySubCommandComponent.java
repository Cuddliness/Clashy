package care.cuddliness.clashy.command.annotation;

import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface ClashySubCommandComponent {

    Class<? extends ClashyCommandInterface> parent();
    String subCommandId();

    String subCommandGroupid() default "";

}
