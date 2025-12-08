package care.cuddliness.stacy.command;

import care.cuddliness.stacy.command.annotation.StacyCommandOption;
import care.cuddliness.stacy.command.data.StacyCommandInterface;
import net.dv8tion.jda.api.interactions.commands.build.*;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public record StacyCommand(@NotNull StacyCommandInterface command, Map<String, StacySubCommand> subCommands, List<StacyCommandOption> options, String name) {

    public static @NotNull Map<String, StacySubCommand> computeSubCommands(@NotNull Collection<StacySubCommand> subCommands) {
        Map<String, StacySubCommand> map = new HashMap<>();

        for (StacySubCommand subCommand : subCommands) {
            if (!subCommand.subCommandId().isEmpty())
                map.put(subCommand.SubCommandGroup() + "/" + subCommand.subCommandId(), subCommand);
            else
                map.put(subCommand.subCommandId(), subCommand);
        }

        return map;
    }

    public CommandData createCommandData() {
        Map<String, StacySubCommand> map = new HashMap<>();
        List<SubcommandData> cmddata = new ArrayList<>();
        List<SubcommandGroupData> subCommandGroups = new ArrayList<>();
        SlashCommandData commandData = Commands.slash(name, name);

        for (StacySubCommand subCommand : subCommands.values()) {
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
