package care.cuddliness.clashy.api.obj;

import care.cuddliness.clashy.api.obj.hero.Hero;
import care.cuddliness.clashy.api.obj.troop.Troop;
import lombok.Data;

import java.util.List;

@Data
public class ClashPlayer {
    String tag, name, role, warPreference;
    int townHallLevel, townHallWeaponLevel, expLevel, trophies, warStars, attackWins, defenceWIns, builderHallLevel, builderBaseTrophies, bestBuilderBaseTrophies, donations, donationsReceived, clanCapitalContributions;
    PlayerClashClan clan;
    List<Hero> heroes;
    List<Troop> troops;

}
