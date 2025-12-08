package care.cuddliness.stacy.interaction.modal.data;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface ModalExecutorInterface {

    void onExecute(Member sender, ModalInteractionEvent event);
}
