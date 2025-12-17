package care.cuddliness.clashy.api.obj.player.hero;

import lombok.Data;

import java.util.List;

@Data
public class Hero {

    private String name;
    private int level;
    private int maxLevel;
    private String village;
    private List<HeroEquipment> equipment;
}
