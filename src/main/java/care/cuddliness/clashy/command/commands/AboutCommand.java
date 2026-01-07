package care.cuddliness.clashy.command.commands;

import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import care.cuddliness.clashy.utils.EmbedColor;
import care.cuddliness.clashy.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@ClashyCommandComponent(name = "info")
public class AboutCommand implements ClashyCommandInterface {

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setColor(EmbedColor.SECONDARY);
        embedUtil.setTitle("About Clashy");
        embedUtil.addField(":tools: Developed by", "Cuddliness :feet:", false);
        embedUtil.addField(":books: Documentation", "https://cuddliness.github.io/Clashy/", false);
        event.replyEmbeds(embedUtil.build()).queue();

    }
}
