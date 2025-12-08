package care.cuddliness.stacy.command;

import care.cuddliness.stacy.command.annotation.StacyCommandComponent;
import care.cuddliness.stacy.command.annotation.StacyCommandOption;
import care.cuddliness.stacy.command.annotation.StacySubCommandComponent;
import care.cuddliness.stacy.command.data.AutoCompletableInterface;
import care.cuddliness.stacy.command.data.StacyCommandExecutorInterface;
import care.cuddliness.stacy.command.data.StacyCommandInterface;
import care.cuddliness.stacy.command.data.StacySubCommandInterface;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.Command;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;

import java.util.*;
import java.util.concurrent.CompletableFuture;

public class StacyCommandHandler {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(StacyCommandHandler.class);

    private final @NotNull JDA jda;

    private final StacyCommandFileHandler commandFileHandler;

    private final @NotNull Map<String, StacyCommand> commandsByName = new HashMap<>();
    private final @NotNull Map<Long, StacyCommand> commandsById = new HashMap<>();

    public StacyCommandHandler(@NotNull ApplicationContext applicationContext, @NotNull JDA jda, @Nullable Guild guild) {
        this.jda = jda;
        this.commandFileHandler = new StacyCommandFileHandler();

        Map<Class<? extends StacyCommandInterface>, Set<StacySubCommand>> subCommandReferences = new HashMap<>();
        for (StacySubCommandInterface subCommand : applicationContext.getBeansOfType(StacySubCommandInterface.class).values()) {

            StacySubCommandComponent subCommandComponent = subCommand.getClass().getAnnotation(StacySubCommandComponent.class);

            StacyCommandOption[] options = subCommand.getClass().getAnnotationsByType(StacyCommandOption.class);

            StacySubCommand commandData = new StacySubCommand(subCommand, subCommandComponent.subCommandId(),
                    subCommandComponent.subCommandGroupid(), Arrays.stream(options).toList());

            Set<StacySubCommand> commandSet = subCommandReferences.computeIfAbsent(subCommandComponent.parent(), k -> new HashSet<>());
            commandSet.add(commandData);
        }

        for (StacyCommandInterface command : applicationContext.getBeansOfType(StacyCommandInterface.class).values()) {
            StacyCommandComponent commandComponent = command.getClass().getAnnotation(StacyCommandComponent.class);
            Map<String, StacySubCommand> mappedSubCommands = StacyCommand.computeSubCommands(subCommandReferences.getOrDefault(command.getClass(), new HashSet<>()));

            StacyCommandOption[] options = command.getClass().getAnnotationsByType(StacyCommandOption.class);

            StacyCommand commandData = new StacyCommand(command, mappedSubCommands,Arrays.stream(options).toList(), commandComponent.name());
            this.commandsByName.put(commandData.name(), commandData);
        }

        LOGGER.info("Registered all in-memory commands: {}", this.commandsByName.values());

        this.init();
    }

    public void init() {
        List<CommandData> list = new ArrayList<>();
        commandsByName.forEach((s, stacyCommand) -> {
            list.add(stacyCommand.createCommandData());
            for(Command command : this.jda.retrieveCommands().complete()){
                commandsById.put(command.getIdLong(), stacyCommand);
                LOGGER.info("Bound created command {} to ID {}", stacyCommand.name(), command.getIdLong());
            }

        });

        jda.updateCommands().addCommands(list).queue();
    }
        @EventListener(SlashCommandInteractionEvent.class)
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CompletableFuture.runAsync(() -> {
            StacyCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
            if (commandExecutor == null) return;

            commandExecutor.onExecute(event.getMember(), event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling slash command event", throwable);
            return null;
        });
    }

    @EventListener(CommandAutoCompleteInteractionEvent.class)
    public void onSlashCommandAutoComplete(CommandAutoCompleteInteractionEvent event) {
        CompletableFuture.runAsync(() -> {
            StacyCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
            if (commandExecutor == null) return;

            if (!(commandExecutor instanceof AutoCompletableInterface autoCompletable)) {
                LOGGER.info("Received autocomplete event for non-autocompletable command {}", event.getCommandString());
                return;
            }

            autoCompletable.onAutoComplete(event);
        }).exceptionally(throwable -> {
            LOGGER.error("Error occurred whilst handling autocomplete event", throwable);
            return null;
        });
    }

    private @Nullable StacyCommandExecutorInterface determineExecutor(long commandId, @Nullable String subCommandName, @Nullable String subCommandGroup) {
        System.out.println("Command Id: " + commandId + " Subcommand: " + subCommandName + " Group: " + subCommandGroup);
        String commandPath = subCommandGroup == null ? subCommandName : subCommandGroup + "/" + subCommandName;
        StacyCommand command = this.commandsById.get(commandId);
        if (command == null) {
            LOGGER.error("Command not found with ID {} and path {}", commandId, commandPath);
            return null;
        }
        if (subCommandName == null) return command.command();

        if (subCommandGroup != null) subCommandName = subCommandGroup + "/" + subCommandName;

        StacySubCommand subCommand = command.subCommands().get(subCommandName);
        if (subCommand == null) {
            LOGGER.error("SubCommand not found with ID {} and path {}", subCommandName, commandPath);
            return null;
        }
        return subCommand.command();
    }
}
