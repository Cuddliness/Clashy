package care.cuddliness.clashy.interaction.button;

import care.cuddliness.clashy.interaction.button.data.ButtonExecutorInterface;
import org.jetbrains.annotations.NotNull;

public record StacyButton(@NotNull ButtonExecutorInterface buttonExecutorInterface, @NotNull String name) {


}
