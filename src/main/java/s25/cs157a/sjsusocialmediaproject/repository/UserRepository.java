package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s25.cs157a.sjsusocialmediaproject.model.User;
import java.util.List;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    List<User> findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(
            String usernamePart,
            String emailPart
    );
}

