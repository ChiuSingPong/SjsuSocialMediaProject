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
public class LikeId implements Serializable {

    private Integer userId;
    private Integer postId;

    public LikeId(Integer userId, Integer postId) {
        this.userId = userId;
        this.postId = postId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LikeId)) return false;
        LikeId likeId = (LikeId) o;
        return Objects.equals(userId, likeId.userId) &&
                Objects.equals(postId, likeId.postId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, postId);
    }
}
