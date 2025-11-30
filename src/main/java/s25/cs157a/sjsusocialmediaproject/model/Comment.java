package s25.cs157a.sjsusocialmediaproject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "commentID")
    private Integer id;

    // Posts and comments: many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postID", nullable = false)
    private Post post;

    // Users and comments: many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    private User user;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    // denormalized profile image (copied from profile at comment time)
    @Column(name = "profileImage")
    private String profileImage;
}
