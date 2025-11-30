package s25.cs157a.sjsusocialmediaproject.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "profiles")
@Getter
@Setter
@NoArgsConstructor
public class Profile {

    // PK is also FK to users.userID
    @Id
    @Column(name = "userID")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "userID")
    private User user;

    @Column(name = "bio")
    private String bio;

    @Column(name = "profileImage")
    private String profileImage;

    @Column(name = "lastUpdated")
    private LocalDateTime lastUpdated;
}
