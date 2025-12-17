package care.cuddliness.clashy.api.obj.clan;

import lombok.Data;

import java.util.List;

@Data
public class ClashClan {

    String tag, name, type, description,warFrequency;
    ClanLocation location;
    boolean isFamilyFriendly;
    ClanBadgeUrls badgeUrls;
    int clanLevel, clanPoints, clanBuilderBasePoints, clanCapitalPoints,warWinStreak,warWins, members;
    List<ClashClanMember> memberList;


}
