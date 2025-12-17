package care.cuddliness.clashy.api.obj.player;

import lombok.Data;

@Data
public class PlayerClashClan {
    String tag, name;
    int clanLevel;
    BadgeUrls badgeUrls;
}
