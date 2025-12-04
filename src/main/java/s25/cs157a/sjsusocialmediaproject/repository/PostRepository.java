package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.User;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    List<Post> findByUserOrderByTimeStampDesc(User user);

    List<Post> findByUserInOrderByTimeStampDesc(List<User> users);

    List<Post> findByContentContainingIgnoreCaseOrderByTimeStampDesc(String contentPart);
}

