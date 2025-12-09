package care.cuddliness.clashy.commands;

import care.cuddliness.clashy.api.ClashApi;
import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import care.cuddliness.clashy.entities.user.ClashyUser;
import care.cuddliness.clashy.repositories.ClashUserRepository;
import care.cuddliness.clashy.utils.EmbedUtil;
import com.google.gson.JsonObject;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

@ClashyCommandComponent(name = "profile")
@ClashyCommandOption(name = "tag", descrip = "Player tag to lookup", t = OptionType.STRING, required = false, auto = false)
public class ProfileCommand implements ClashyCommandInterface {
    @Autowired
    ClashUserRepository userRepository;
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if(event.getOptions().isEmpty()){
            ClashyUser user = userRepository.findByUserId(sender.getIdLong());
            if (user == null){
                event.reply("You don't have an account linked").setEphemeral(true).queue();
                return;
            }else{
                ClashApi clashApi = new ClashApi();
                EmbedUtil embedUtil = new EmbedUtil();
                JsonObject player = clashApi.getAccount(user.getPlayerTag().replace("#", "%23"));

                embedUtil.addField(player.get("name").toString().replace("\"", ""), player.get("tag").toString().replace("\"", ""), false);
                embedUtil.addField("TownHall", "Level: " + player.get("townHallLevel"), false);
                event.replyEmbeds(embedUtil.build()).queue();
                return;
            }
        }
        String playertag = event.getOption("tag").getAsString().replace("#", "%23");
        ClashApi clashApi = new ClashApi();
        EmbedUtil embedUtil = new EmbedUtil();
        JsonObject player = clashApi.getAccount(playertag);

        embedUtil.addField(player.get("name").toString().replace("\"", ""), player.get("tag").toString().replace("\"", ""), false);
        embedUtil.addField("TownHall", "Level: " + player.get("townHallLevel"), false);
        event.replyEmbeds(embedUtil.build()).queue();
    }
}
