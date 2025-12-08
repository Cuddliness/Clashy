package care.cuddliness.stacy.listeners;

import care.cuddliness.stacy.repositories.GuildRepositoryInterface;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GuildJoinListener extends ListenerAdapter {

    @Autowired
    private GuildRepositoryInterface guildRepositoryInterface ;
    @Autowired
    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(GuildJoinListener.class);
    @Override
    public void onGuildJoin(GuildJoinEvent event) {

        //Example on how to handle a basic listener
    }
}
