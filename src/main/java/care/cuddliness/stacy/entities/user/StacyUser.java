package care.cuddliness.stacy.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = {"userId", "guildId"})})
public class StacyUser {


    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Getter @Setter private UUID id = UUID.randomUUID();
    @Getter @Setter private Long userId;
    @Getter @Setter private Integer messageCount = 0;
    @Getter @Setter private Long guildId;
    @Getter @Setter private Long applicationId;

    public StacyUser(Long longId){}
    public StacyUser(){}
}
