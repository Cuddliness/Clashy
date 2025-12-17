package care.cuddliness.clashy.api.obj.player.hero;

import lombok.Data;

@Data
public class HeroEquipment {
    private String name;
    private int level;
    private int maxLevel;
    private String village;
}
