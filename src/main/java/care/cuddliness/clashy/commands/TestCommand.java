package care.cuddliness.clashy.commands;

import care.cuddliness.clashy.api.ClashApi;
import care.cuddliness.clashy.api.http.OkHttpProvider;
import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.jetbrains.annotations.NotNull;

@ClashyCommandComponent(name = "test")
public class TestCommand implements ClashyCommandInterface {

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        ClashApi clashApi = new ClashApi(new OkHttpProvider());
        clashApi.getAccount(event.getMember().getId());
        event.reply(clashApi.getAccount("cuddliness").getAsString()).queue();
    }
}
