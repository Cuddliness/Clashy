package care.cuddliness.clashy.repositories;

import care.cuddliness.clashy.entities.user.ClashyUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ClashUserRepository extends CrudRepository<ClashyUser, UUID> {
    ClashyUser findByUserId(long id);

}
