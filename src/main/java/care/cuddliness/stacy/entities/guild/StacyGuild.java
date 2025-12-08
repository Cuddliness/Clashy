package care.cuddliness.stacy.entities.guild;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "guilds")
public class StacyGuild {


    @Getter
    @Setter
    @Id
    private Long id;
    public String naughtyWords;
    public StacyGuild(){

    }


}
