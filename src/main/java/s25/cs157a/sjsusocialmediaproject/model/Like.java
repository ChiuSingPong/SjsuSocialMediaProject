package s25.cs157a.sjsusocialmediaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "liked")
@Getter
@Setter
@NoArgsConstructor
public class Like {

    @EmbeddedId
    private LikeId id;

    // the user who liked the post
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userID")
    private User user;

    // the post that was liked
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("postId")
    @JoinColumn(name = "postID")
    @JsonIgnore
    private Post post;

    // the owner of the post
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "likedID")
    private User likedUser;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    public Like(User user, Post post, User likedUser, LocalDateTime timeStamp) {
        this.user = user;
        this.post = post;
        this.likedUser = likedUser;
        this.timeStamp = timeStamp;
        this.id = new LikeId(
                user != null ? user.getId() : null,
                post != null ? post.getId() : null
        );
    }
}
