package care.cuddliness.clashy.command;

import care.cuddliness.clashy.command.annotation.ClashyCommandOption;
import care.cuddliness.clashy.command.data.ClashySubCommandInterface;

import java.util.List;

public record ClashySubCommand(ClashySubCommandInterface command, String subCommandId, String SubCommandGroup, List<ClashyCommandOption> options){

}
