package care.cuddliness.clashy.command.commands;

import care.cuddliness.clashy.api.ClashApi;
import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import care.cuddliness.clashy.entities.user.ClashyUser;
import care.cuddliness.clashy.repositories.ClashUserRepository;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@ClashyCommandComponent(name = "linkaccount")
@ClashyCommandOption(name = "token", descrip = "API token found in clash of clans profile settings", t = OptionType.STRING, required = true, auto = false)
@ClashyCommandOption(name = "playertag", descrip = "API token found in clash of clans profile settings", t = OptionType.STRING, required = true, auto = false)

public class LinkAccountCommand implements ClashyCommandInterface {
    @Autowired
    ClashUserRepository userRepository;

    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        System.out.println(event.getOptions());
        String playertag = event.getOption("playertag").getAsString().replace("#", "%23");
        String playerToken = event.getOption("token").getAsString().replace("#", "%23");

        ClashApi clashApi = new ClashApi();
        if(clashApi.linkAccount(playertag, playerToken)){
            ClashyUser user = userRepository.findByUserId(event.getMember().getIdLong());
            if(user != null){
                user.setPlayerTag(event.getOption("playertag").getAsString());
            }else{
                user = new ClashyUser(event.getMember().getIdLong(), event.getOption("playertag").getAsString());
            }
            userRepository.save(user);
            event.reply("Account linked").setEphemeral(true).queue();
        }else{
            event.reply("Account is not from this user.").setEphemeral(true).queue();
        }


    }
}
