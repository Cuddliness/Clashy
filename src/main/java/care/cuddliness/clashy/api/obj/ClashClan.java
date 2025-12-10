package care.cuddliness.clashy.api.obj;

import lombok.Data;
import lombok.Getter;

@Data
public class ClashClan {
    String tag, name;
    int clanLevel;
    BadgeUrls badgeUrls;
}
