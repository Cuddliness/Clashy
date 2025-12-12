package care.cuddliness.clashy.commands;

import care.cuddliness.clashy.api.ClashApi;
import care.cuddliness.clashy.api.obj.ClashPlayer;
import care.cuddliness.clashy.api.obj.hero.Hero;
import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import care.cuddliness.clashy.entities.user.ClashyUser;
import care.cuddliness.clashy.repositories.ClashUserRepository;
import care.cuddliness.clashy.utils.EmbedUtil;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URISyntaxException;
import java.util.Map;

@ClashyCommandComponent(name = "profile")
@ClashyCommandOption(name = "tag", descrip = "Player tag to lookup", t = OptionType.STRING, required = false, auto = false)
public class ProfileCommand implements ClashyCommandInterface {
    @Autowired ClashUserRepository userRepository;
    @Override
    public void onExecute(@NotNull Member sender, @NotNull SlashCommandInteractionEvent event) {
        if (event.getOptions().isEmpty()) {
            ClashyUser user = userRepository.findByUserId(sender.getIdLong());
            if (user == null) {
                event.reply("You don't have an account linked").setEphemeral(true).queue();
            } else {
                ClashApi clashApi = new ClashApi();
                EmbedUtil embedUtil = new EmbedUtil();
                ClashPlayer player = clashApi.getClashPlayer(user.getPlayerTag().replace("#", "%23"));
                embedUtil.setTitle(player.getName().replace("\"", "") + " (`" + player.getTag().toString().replace("\"", "") + "`)" + " lvl `" + player.getExpLevel() + "`");
                embedUtil.addField("TownHall", "Level: " + player.getTownHallLevel(), false);
                embedUtil.addField("Clan", player.getClan().getName() + " (`" + player.getClan().getTag().replace("\"", "") + "`) "+ " (" + StringUtils.capitalize(player.getRole()) + ")", false);
                embedUtil.addField("Clan donations", "Donated: `" + player.getDonations() + "` " + "Donations Received: " + "`" + player.getDonationsReceived() + "`", false);
                embedUtil.addField(":star: War stars", player.getWarStars() + "", true);
                embedUtil.addField(":shield: War preference", StringUtils.capitalize(player.getWarPreference()), true);
                embedUtil.addField("Heroes", prettyHeroes(player), false);
                embedUtil.addBlankSpace(true);
                File th = new File(getClass().getClassLoader().getResource("townhall/" + player.getTownHallLevel() + ".png").getFile());
                embedUtil.setThumbnail(th);
                event.replyEmbeds(embedUtil.build()).addFiles(FileUpload.fromData(th)).queue();

            }
        } else {
            String playertag = event.getOption("tag").getAsString().replace("#", "%23");
            ClashApi clashApi = new ClashApi();
            EmbedUtil embedUtil = new EmbedUtil();
            ClashPlayer clashPlayer = clashApi.getClashPlayer(playertag);

            embedUtil.addField(clashPlayer.getName().replace("\"", ""), clashPlayer.getTag().toString().replace("\"", "") + " lvl " + clashPlayer.getExpLevel(), false);
            embedUtil.addField("TownHall", "Level: " + clashPlayer.getTownHallLevel(), false);
            embedUtil.addField("Clan", clashPlayer.getClan().getName() + " - lvl. " + clashPlayer.getClan().getClanLevel() + " (" + clashPlayer.getRole() + ")", false);
            try {
                embedUtil.setImage(getClass().getClassLoader().getResource("townhall/" + clashPlayer.getTownHallLevel() + ".png").toURI().toString());
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }
            event.replyEmbeds(embedUtil.build()).queue();
        }
    }
    private String prettyHeroes(ClashPlayer player){
        StringBuilder stringBuilder = new StringBuilder();
        Map<String, String> HERO_EMOJIS = getStringStringMap();
        for (Hero h : player.getHeroes()) {
            String emoji = HERO_EMOJIS.get(h.getName());
            if (emoji != null) {
                stringBuilder.append(emoji)
                        .append(" `")
                        .append(h.getLevel())
                        .append("`/`")
                        .append(h.getMaxLevel())
                        .append("` ");
            }
        }
        return stringBuilder.toString();
    }


    @NotNull
    private static Map<String, String> getStringStringMap() {
        String babarianKinkEmoji = "<:BabarianKing:1448331341266681907>";
        String archerQueenEmoji = "<:ArcherQueen:1448331271179862186>";
        String grandWardenEmoji = "<:GrandWarden:1448331345834152046>";
        String minionPrinceEmoji = "<:MinionPrince:1448331348631883919>";
        String royalPrinceEmoji = "<:RoyalChamp:1448331339567992924> ";
        String battleMachineEmoji = "<:BattleMachine:1448331344051703959>";
        String battleCopterEmoji = "<:BattleCopter:1448331342831157431>";
        Map<String, String> HERO_EMOJIS = Map.of(
                "Barbarian King", babarianKinkEmoji,
                "Archer Queen", archerQueenEmoji,
                "Grand Warden", grandWardenEmoji,
                "Royal Champion", royalPrinceEmoji,
                "Minion Prince", minionPrinceEmoji,
                "Battle Machine", battleMachineEmoji,
                "Battle Copter", battleCopterEmoji
        );
        return HERO_EMOJIS;
    }
}
