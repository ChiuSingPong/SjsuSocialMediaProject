package s25.cs157a.sjsusocialmediaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Post post;

    // Users and comments: many-to-one
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userID", nullable = false)
    @JsonIgnore
    private User user;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

}
