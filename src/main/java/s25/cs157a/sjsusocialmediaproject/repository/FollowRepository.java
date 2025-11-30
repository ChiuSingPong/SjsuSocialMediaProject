package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import s25.cs157a.sjsusocialmediaproject.model.Follow;
import s25.cs157a.sjsusocialmediaproject.model.FollowId;
import s25.cs157a.sjsusocialmediaproject.model.User;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, FollowId> {

    // who this user is following
    List<Follow> findByUser(User user);

    // who follows this user
    List<Follow> findByFriend(User friend);

    boolean existsByUserAndFriend(User user, User friend);

    @Transactional
    void deleteByUserAndFriend(User user, User friend);
}
