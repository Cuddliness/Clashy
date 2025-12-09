package care.cuddliness.clashy.command.annotation;

import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
@Repeatable(CommandOptions.class)
public @interface ClashyCommandOption {

    @NotNull String name();
    @NotNull String descrip();
    @NotNull OptionType t();
    boolean required() default false;
    boolean auto() default false;




}
