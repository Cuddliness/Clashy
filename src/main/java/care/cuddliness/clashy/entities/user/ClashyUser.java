package care.cuddliness.clashy.entities.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table(name = "user")
public class ClashyUser {


    @GeneratedValue(strategy = GenerationType.UUID)
    @Id
    @Getter @Setter private UUID id = UUID.randomUUID();
    @Getter @Setter private Long userId;
    @Getter @Setter private String playerTag;

    public ClashyUser(Long userId, String playerTag){
        this.userId = userId;
        this.playerTag = playerTag;
    }
    public ClashyUser(){}
}
