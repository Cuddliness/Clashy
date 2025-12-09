package care.cuddliness.clashy.repositories;

import care.cuddliness.clashy.entities.guild.ClashyGuild;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface GuildRepositoryInterface extends CrudRepository <ClashyGuild, Long> {

    ClashyGuild findById(long id);

    @Modifying
    @Query(value = "insert ignore into guilds (id) VALUES (:id)", nativeQuery = true)
    @Transactional
    void saveGuildIfExists(@Param("id") String id);


}
