package care.cuddliness.clashy.api.obj;

import lombok.Data;
import lombok.Getter;

@Data
public class ClashPlayer {
    String tag, name, role, warPreference;
    int townHallLevel, townHallWeaponLevel, expLevel, trophies, warStars, attackWins, defenceWIns, builderHallLevel, builderBaseTrophies, bestBuilderBaseTrophies, donations, donationsReceived, clanCapitalContributions;
    ClashClan clan;
}
