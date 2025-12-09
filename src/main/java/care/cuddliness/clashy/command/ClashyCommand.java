package care.cuddliness.clashy.command;

import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.data.ClashyCommandInterface;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record ClashyCommand(@NotNull ClashyCommandInterface command, Map<String, ClashySubCommand> subCommands, List<ClashyCommandOption> options, String name) {

    public static @NotNull Map<String, ClashySubCommand> computeSubCommands(@NotNull Collection<ClashySubCommand> subCommands) {
        Map<String, ClashySubCommand> map = new HashMap<>();

        for (ClashySubCommand subCommand : subCommands) {
            if (!subCommand.subCommandId().isEmpty())
                map.put(subCommand.SubCommandGroup() + "/" + subCommand.subCommandId(), subCommand);
            else
                map.put(subCommand.subCommandId(), subCommand);
        }

        return map;
    }

    public CommandData createCommandData() {
        Map<String, ClashySubCommand> map = new HashMap<>();
        List<SubcommandData> cmddata = new ArrayList<>();
        List<SubcommandGroupData> subCommandGroups = new ArrayList<>();
        SlashCommandData commandData = Commands.slash(name, name);

        for (ClashySubCommand subCommand : subCommands.values()) {
            if(subCommandGroups.stream().noneMatch(subcommandGroupData -> subcommandGroupData.getName().equalsIgnoreCase(subCommand.SubCommandGroup()))){
                if(!subCommand.SubCommandGroup().equalsIgnoreCase("")) {
                    subCommandGroups.add(new SubcommandGroupData(subCommand.SubCommandGroup(), "Bwah"));
                }
            }

            SubcommandData data = new SubcommandData(subCommand.subCommandId(), "HII");

            for(SubcommandGroupData groupData : subCommandGroups){
                if(groupData.getName().equalsIgnoreCase(subCommand.SubCommandGroup())){
                    groupData.addSubcommands(data);
                }
            }
            subCommand.options().forEach(option -> data.addOption(option.t(), option.name(), option.descrip(),
                    option.required(), option.auto()));
            if(subCommand.SubCommandGroup().equalsIgnoreCase("")) {
                cmddata.add(data);
            }
            map.putIfAbsent(subCommand.subCommandId(), subCommand);

        }
        commandData.addSubcommandGroups(subCommandGroups);
        commandData.addSubcommands(cmddata);
        this.subCommands.putAll(map);
        return commandData;
    }

}
