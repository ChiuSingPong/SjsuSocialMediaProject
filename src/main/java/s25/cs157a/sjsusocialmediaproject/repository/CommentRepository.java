package s25.cs157a.sjsusocialmediaproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import s25.cs157a.sjsusocialmediaproject.model.Comment;
import s25.cs157a.sjsusocialmediaproject.model.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {

    List<Comment> findByPostOrderByTimeStampAsc(Post post);
}
