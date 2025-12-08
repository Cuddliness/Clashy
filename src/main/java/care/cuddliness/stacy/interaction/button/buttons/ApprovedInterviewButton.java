package care.cuddliness.stacy.interaction.button.buttons;

import care.cuddliness.stacy.entities.user.StacyUser;
import care.cuddliness.stacy.interaction.button.annotation.StacyButtonComponent;
import care.cuddliness.stacy.interaction.button.data.ButtonExecutorInterface;
import care.cuddliness.stacy.utils.EmbedColor;
import care.cuddliness.stacy.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.Objects;

@StacyButtonComponent(name = "test", style = ButtonStyle.SUCCESS)

public class ApprovedInterviewButton implements ButtonExecutorInterface {


    // Executes when button with the id 'test' is pressed
    public void onExecute(Member clicker, ButtonInteractionEvent event) {
        event.reply("You clicked a button!").queue();

    }
}
