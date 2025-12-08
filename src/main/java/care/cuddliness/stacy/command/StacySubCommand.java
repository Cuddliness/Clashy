package care.cuddliness.stacy.command;

import care.cuddliness.stacy.command.annotation.StacyCommandOption;
import care.cuddliness.stacy.command.data.StacySubCommandInterface;

import java.util.List;

public record StacySubCommand(StacySubCommandInterface command, String subCommandId, String SubCommandGroup, List<StacyCommandOption> options){

}
