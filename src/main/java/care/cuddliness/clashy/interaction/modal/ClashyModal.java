package care.cuddliness.clashy.interaction.modal;

import care.cuddliness.clashy.interaction.modal.annotation.ModalInput;
import care.cuddliness.clashy.interaction.modal.data.ModalExecutorInterface;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public record ClashyModal(@NotNull ModalExecutorInterface modalExecutorInterface, List<ModalInput> options, @NotNull String name) {

}
