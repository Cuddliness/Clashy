package care.cuddliness.stacy.interaction.modal.annotation;

import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ModalInputs.class)
@Component
public @interface ModalInput {

    String name();
    TextInputStyle inputstyle();
    String placeholder();
    int minLenght() default 10;
    int maxLenght()  default 100;
}
