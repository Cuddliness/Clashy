package care.cuddliness.clashy.entities.guild;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guild")
public class ClashyGuild {


    @Getter
    @Setter
    @Id
    private Long id;
    public String naughtyWords;
    public ClashyGuild(){

    }


}
