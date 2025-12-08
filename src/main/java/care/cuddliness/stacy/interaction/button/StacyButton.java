package care.cuddliness.stacy.interaction.button;

import care.cuddliness.stacy.interaction.button.data.ButtonExecutorInterface;
import org.jetbrains.annotations.NotNull;

public record StacyButton(@NotNull ButtonExecutorInterface buttonExecutorInterface, @NotNull String name) {


}
