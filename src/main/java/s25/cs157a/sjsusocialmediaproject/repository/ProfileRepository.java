package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s25.cs157a.sjsusocialmediaproject.model.Profile;

public interface ProfileRepository extends JpaRepository<Profile, Integer> {
}

