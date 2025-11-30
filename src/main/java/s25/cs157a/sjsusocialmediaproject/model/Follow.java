package s25.cs157a.sjsusocialmediaproject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "following")
@Getter
@Setter
@NoArgsConstructor
public class Follow {

    @EmbeddedId
    private FollowId id;

    // follower
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    @JoinColumn(name = "userID")
    private User user;

    // user being followed (friend)
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("friendId")
    @JoinColumn(name = "friendID")
    private User friend;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    public Follow(User user, User friend, LocalDateTime timeStamp) {
        this.user = user;
        this.friend = friend;
        this.timeStamp = timeStamp;
        this.id = new FollowId(
                user != null ? user.getId() : null,
                friend != null ? friend.getId() : null
        );
    }
}
