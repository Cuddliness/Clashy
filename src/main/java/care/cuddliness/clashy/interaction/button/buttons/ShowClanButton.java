package care.cuddliness.clashy.interaction.button.buttons;

import care.cuddliness.clashy.api.ClashApi;
import care.cuddliness.clashy.api.obj.clan.ClashClan;
import care.cuddliness.clashy.api.obj.clan.ClashClanMember;
import care.cuddliness.clashy.interaction.button.annotation.ClashyButtonComponent;
import care.cuddliness.clashy.interaction.button.data.ButtonExecutorInterface;
import care.cuddliness.clashy.utils.EmbedUtil;
import net.dv8tion.jda.api.components.buttons.ButtonStyle;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@ClashyButtonComponent(name = "show_clan", style = ButtonStyle.SUCCESS)
public class ShowClanButton implements ButtonExecutorInterface {

    @Override
    public void onExecute(Member clicker, ButtonInteractionEvent event) {
        Message message = event.getMessage();
        ClashApi clashApi = new ClashApi();

        message.getEmbeds().get(0).getFields().forEach(field -> {
            if(field.getName().equalsIgnoreCase("Clan")){
                ClashClan clan = clashApi.getClan(StringUtils.substringBetween(field.getValue(), "(", ")").replace("`", ""));
                prettyClanPrint(event, clan);
            }
        });
    }

    public void prettyClanPrint(ButtonInteractionEvent event, ClashClan clan){
        EmbedUtil embedUtil = new EmbedUtil();
        embedUtil.setTitle(clan.getName() + "(`" + clan.getTag() + "`) lvl: " + clan.getClanLevel());
        embedUtil.setDescription(clan.getDescription());
        embedUtil.setThumbnail(clan.getBadgeUrls().getMedium());
        embedUtil.addField("Members", "List of members", false);
        StringBuilder builder = new StringBuilder();
        for(ClashClanMember m : clan.getMemberList()){
            builder.append(m.getName()).append(" (**").append(m.getTag()).append("**) - ").append(m.getRole()).append("\n");
        }
        List<String> strings = new ArrayList<>();
        int index = 0;
        while (index < builder.length()) {
            strings.add(builder.substring(index, Math.min(index + 800,builder.length())));
            index += 800;
        }
        List<MessageEmbed> embeds = new ArrayList<>();
        embeds.add(embedUtil.build());
        for(String i : strings){
            embeds.add(new EmbedUtil().setDescription(i).build());
        }
        event.replyEmbeds(embeds).queue();
    }
}
