package mb.repository;


import mb.entities.User;
import mb.enums.Role;
import mb.exceptions.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByEmail(String email);


    default User getByEmail(String email){
        return findByEmail(email).orElseThrow(() ->
                new NotFoundException("User with: "+email+" not found"));
    }

    Optional<User> findById(Long id);
    default User getById(Long userId){
        return findById(userId).orElseThrow(() ->
                new NotFoundException("User with: "+userId+" not found"));
    }


    boolean existsByEmail(String email);

    User findByRole(Role role);
}

