package care.cuddliness.clashy.autoconfig;

import care.cuddliness.clashy.command.ClashyCommandHandler;
import care.cuddliness.clashy.repositories.*;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
@RequiredArgsConstructor
public class DiscordCommandAutoConfiguration {
    private final ApplicationContext applicationContext;
    private final GuildRepositoryInterface guildRepositoryInterface;

    private final JDA jda;

    @Bean
    @ConditionalOnMissingBean
    public ClashyCommandHandler discordCommandBackend() {
        Guild guild = this.jda.getGuildById(-1L);
        return new ClashyCommandHandler(this.applicationContext, this.jda, guild);
    }

}
