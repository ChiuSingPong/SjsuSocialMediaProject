package s25.cs157a.sjsusocialmediaproject.model;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
public class FollowId implements Serializable {

    private Integer userId;
    private Integer friendId;

    public FollowId(Integer userId, Integer friendId) {
        this.userId = userId;
        this.friendId = friendId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FollowId)) return false;
        FollowId that = (FollowId) o;
        return Objects.equals(userId, that.userId)
                && Objects.equals(friendId, that.friendId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, friendId);
    }
}
