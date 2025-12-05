package s25.cs157a.sjsusocialmediaproject.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users") // TABLE USERS
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id // PRIMARY KEY
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userID") // userID INT auto-increment
    private Integer id;

    @Column(name = "userName", nullable = false, unique = true)
    private String username; // userName VARCHAR(225) NOT NULL UNIQUE

    @Column(nullable = false, unique = true)
    private String email; // email VARCHAR(255) NOT NULL UNIQUE

    @Column(name = "password", nullable = false)
    @JsonIgnore
    private String password; // password VARCHAR(255) NOT NULL

    // Relationships

    // Users and posts: one-to-many
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Post> posts;

    // Users and profiles: one-to-one (mapped in Profile via @OneToOne @MapsId)
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Profile profile;

    // Users and comments: one-to-many
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Comment> comments;

    // Users and likes: one-to-many
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Like> likes;

    // following (this user is the follower)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> following;

    // followers (other users following this user)
    @OneToMany(mappedBy = "friend", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Follow> followers;
}
