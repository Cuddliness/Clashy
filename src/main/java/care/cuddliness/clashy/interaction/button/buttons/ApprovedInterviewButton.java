package care.cuddliness.clashy.interaction.button.buttons;

import care.cuddliness.clashy.interaction.button.annotation.ClashyButtonComponent;
import care.cuddliness.clashy.interaction.button.data.ButtonExecutorInterface;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

@ClashyButtonComponent(name = "test", style = ButtonStyle.SUCCESS)

public class ApprovedInterviewButton implements ButtonExecutorInterface {


    // Executes when button with the id 'test' is pressed
    public void onExecute(Member clicker, ButtonInteractionEvent event) {
        event.reply("You clicked a button!").queue();

    }
}
