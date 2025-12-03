package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import s25.cs157a.sjsusocialmediaproject.model.Like;
import s25.cs157a.sjsusocialmediaproject.model.LikeId;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.User;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, LikeId> {

    long countByPost(Post post);

    boolean existsByUserAndPost(User user, Post post);

    List<Like> findByUser(User user);

    @Transactional
    void deleteByUserAndPost(User user, Post post);
}
