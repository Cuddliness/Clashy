package care.cuddliness.clashy.command;

import care.cuddliness.clashy.command.annotation.ClashyCommandComponent;
import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.annotation.ClashySubCommandComponent;
import care.cuddliness.clashy.command.data.AutoCompletableInterface;
import care.cuddliness.clashy.command.data.ClashyCommandExecutorInterface;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import care.cuddliness.clashy.command.data.ClashySubCommandInterface;
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

public class ClashyCommandHandler {

    private static final @NotNull Logger LOGGER = LoggerFactory.getLogger(ClashyCommandHandler.class);

    private final @NotNull JDA jda;

    private final @NotNull Map<String, ClashyCommand> commandsByName = new HashMap<>();
    private final @NotNull Map<Long, ClashyCommand> commandsById = new HashMap<>();

    public ClashyCommandHandler(@NotNull ApplicationContext applicationContext, @NotNull JDA jda, @Nullable Guild guild) {
        this.jda = jda;
        ClashyCommandFileHandler commandFileHandler = new ClashyCommandFileHandler();

        Map<Class<? extends ClashyCommandInterface>, Set<ClashySubCommand>> subCommandReferences = new HashMap<>();
        for (ClashySubCommandInterface subCommand : applicationContext.getBeansOfType(ClashySubCommandInterface.class).values()) {

            ClashySubCommandComponent subCommandComponent = subCommand.getClass().getAnnotation(ClashySubCommandComponent.class);

            ClashyCommandOption[] options = subCommand.getClass().getAnnotationsByType(ClashyCommandOption.class);

            ClashySubCommand commandData = new ClashySubCommand(subCommand, subCommandComponent.subCommandId(),
                    subCommandComponent.subCommandGroupid(), Arrays.stream(options).toList());

            Set<ClashySubCommand> commandSet = subCommandReferences.computeIfAbsent(subCommandComponent.parent(), k -> new HashSet<>());
            commandSet.add(commandData);
        }

        for (ClashyCommandInterface command : applicationContext.getBeansOfType(ClashyCommandInterface.class).values()) {
            ClashyCommandComponent commandComponent = command.getClass().getAnnotation(ClashyCommandComponent.class);
            Map<String, ClashySubCommand> mappedSubCommands = ClashyCommand.computeSubCommands(subCommandReferences.getOrDefault(command.getClass(), new HashSet<>()));

            ClashyCommandOption[] options = command.getClass().getAnnotationsByType(ClashyCommandOption.class);

            ClashyCommand commandData = new ClashyCommand(command, mappedSubCommands,Arrays.stream(options).toList(), commandComponent.name());
            this.commandsByName.put(commandData.name(), commandData);
        }

        LOGGER.info("Registered all in-memory commands: {}", this.commandsByName.values());

        this.init();
    }

    public void init() {
        List<CommandData> list = new ArrayList<>();
        commandsByName.forEach((s, stacyCommand) -> {
            list.add(stacyCommand.createCommandData());
            commandsById.put(this.jda.retrieveCommands().complete().stream().filter(command -> command.getName().equalsIgnoreCase(stacyCommand.name())).findFirst().get().getIdLong(), stacyCommand);
                LOGGER.info("Bound created command {}", stacyCommand.name());

            });

        jda.updateCommands().addCommands(list).queue();
    }
        @EventListener(SlashCommandInteractionEvent.class)
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        CompletableFuture.runAsync(() -> {
            ClashyCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
            System.out.println("Determine if command is: " + event.getCommandIdLong());
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
            ClashyCommandExecutorInterface commandExecutor = this.determineExecutor(event.getCommandIdLong(), event.getSubcommandName(), event.getSubcommandGroup());
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

    private @Nullable ClashyCommandExecutorInterface determineExecutor(long commandId, @Nullable String subCommandName, @Nullable String subCommandGroup) {
        System.out.println("Command Id: " + commandId + " Subcommand: " + subCommandName + " Group: " + subCommandGroup);
        String commandPath = subCommandGroup == null ? subCommandName : subCommandGroup + "/" + subCommandName;
        ClashyCommand command = this.commandsById.get(commandId);
        if (command == null) {
            LOGGER.error("Command not found with ID {} and path {}", commandId, commandPath);
            return null;
        }
        if (subCommandName == null) return command.command();

        if (subCommandGroup != null) subCommandName = subCommandGroup + "/" + subCommandName;

        ClashySubCommand subCommand = command.subCommands().get(subCommandName);
        if (subCommand == null) {
            LOGGER.error("SubCommand not found with ID {} and path {}", subCommandName, commandPath);
            return null;
        }
        return subCommand.command();
    }
}
