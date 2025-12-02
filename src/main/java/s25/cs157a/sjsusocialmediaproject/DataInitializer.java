package s25.cs157a.sjsusocialmediaproject;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import s25.cs157a.sjsusocialmediaproject.model.*;
import s25.cs157a.sjsusocialmediaproject.repository.*;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository likeRepository;

    public DataInitializer(UserRepository userRepository,
                           ProfileRepository profileRepository,
                           FollowRepository followRepository,
                           PostRepository postRepository,
                           CommentRepository commentRepository,
                           LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.followRepository = followRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.likeRepository = likeRepository;
    }

    @Override
    public void run(String... args) {

        // only seed if DB is empty
        if (userRepository.count() > 0) {
            return;
        }

        // --- USERS ---
        User alex   = new User();
        alex.setUsername("alexmartinez");
        alex.setEmail("alex@sjsu.edu");
        alex.setPassword("password123");


        User sarah  = new User();
        sarah.setUsername("sarahj");
        sarah.setEmail("sarah@sjsu.edu");
        sarah.setPassword("password123");


        User mike   = new User();
        mike.setUsername("mikechen");
        mike.setEmail("mike@sjsu.edu");
        mike.setPassword("password123");


        User jess   = new User();
        jess.setUsername("jpark");
        jess.setEmail("jessica@sjsu.edu");
        jess.setPassword("password123");


        User david  = new User();
        david.setUsername("dkim");
        david.setEmail("david@sjsu.edu");
        david.setPassword("password123");


        User emma   = new User();
        emma.setUsername("ewilson");
        emma.setEmail("emma@sjsu.edu");
        emma.setPassword("password123");


        userRepository.saveAll(List.of(alex, sarah, mike, jess, david, emma));

        // --- PROFILES ---
        Profile pAlex = new Profile();
        pAlex.setUser(alex);
        pAlex.setBio("SJSU Student - CS Major");
        pAlex.setProfileImage("https://via.placeholder.com/80x80.png?text=A");
        pAlex.setLastUpdated(LocalDateTime.now());

        Profile pSarah = new Profile();
        pSarah.setUser(sarah);
        pSarah.setBio("Business Major");
        pSarah.setProfileImage("https://via.placeholder.com/80x80.png?text=S");
        pSarah.setLastUpdated(LocalDateTime.now());

        Profile pMike = new Profile();
        pMike.setUser(mike);
        pMike.setBio("Mechanical Engineering Student");
        pMike.setProfileImage("https://via.placeholder.com/80x80.png?text=M");
        pMike.setLastUpdated(LocalDateTime.now());

        Profile pJess = new Profile();
        pJess.setUser(jess);
        pJess.setBio("Psychology Student");
        pJess.setProfileImage("https://via.placeholder.com/80x80.png?text=J");
        pJess.setLastUpdated(LocalDateTime.now());

        Profile pDavid = new Profile();
        pDavid.setUser(david);
        pDavid.setBio("Computer Engineering Student");
        pDavid.setProfileImage("https://via.placeholder.com/80x80.png?text=D");
        pDavid.setLastUpdated(LocalDateTime.now());

        Profile pEmma = new Profile();
        pEmma.setUser(emma);
        pEmma.setBio("Nursing Student");
        pEmma.setProfileImage("https://via.placeholder.com/80x80.png?text=E");
        pEmma.setLastUpdated(LocalDateTime.now());

        profileRepository.saveAll(List.of(pAlex, pSarah, pMike, pJess, pDavid, pEmma));

        // --- FOLLOWING: Alex follows everyone ---
        for (User friend : List.of(sarah, mike, jess, david, emma)) {
            Follow f = new Follow();
            f.setId(new FollowId(alex.getId(), friend.getId()));
            f.setUser(alex);
            f.setFriend(friend);
            f.setTimeStamp(LocalDateTime.now());
            followRepository.save(f);
        }

        // --- POSTS ---
        Post post1 = new Post();
        post1.setUser(alex);
        post1.setContent("Just finished my finals! So excited for winter break! Go Spartans!");
        post1.setMediaUrl("https://images.pexels.com/photos/462331/pexels-photo-462331.jpeg?auto=compress&cs=tinysrgb&w=1200");
        post1.setTimeStamp(LocalDateTime.now());

        Post post2 = new Post();
        post2.setUser(sarah);
        post2.setContent("Had a great study session today at MLK Library.");
        post2.setTimeStamp(LocalDateTime.now().minusHours(2));

        Post post3 = new Post();
        post3.setUser(mike);
        post3.setContent("Engineering club meeting was fun today.");
        post3.setTimeStamp(LocalDateTime.now().minusHours(3));

        Post post4 = new Post();
        post4.setUser(jess);
        post4.setContent("Psychology project is finally done.");
        post4.setTimeStamp(LocalDateTime.now().minusHours(4));

        postRepository.saveAll(List.of(post1, post2, post3, post4));

        // --- COMMENTS ---
        Comment c1 = new Comment();
        c1.setPost(post1);
        c1.setUser(sarah);
        c1.setContent("Congrats Alex! Enjoy the break!");
        c1.setTimeStamp(LocalDateTime.now());

        Comment c2 = new Comment();
        c2.setPost(post1);
        c2.setUser(mike);
        c2.setContent("Nice work, you earned it.");
        c2.setTimeStamp(LocalDateTime.now());

        Comment c3 = new Comment();
        c3.setPost(post2);
        c3.setUser(alex);
        c3.setContent("Library study sessions are the best.");
        c3.setTimeStamp(LocalDateTime.now());

        commentRepository.saveAll(List.of(c1, c2, c3));

        // --- LIKES ---
        Like like1 = new Like();
        like1.setId(new LikeId(sarah.getId(), post1.getId()));
        like1.setUser(sarah);
        like1.setPost(post1);
        like1.setLikedUser(alex);
        like1.setTimeStamp(LocalDateTime.now());

        Like like2 = new Like();
        like2.setId(new LikeId(mike.getId(), post1.getId()));
        like2.setUser(mike);
        like2.setPost(post1);
        like2.setLikedUser(alex);
        like2.setTimeStamp(LocalDateTime.now());

        Like like3 = new Like();
        like3.setId(new LikeId(jess.getId(), post1.getId()));
        like3.setUser(jess);
        like3.setPost(post1);
        like3.setLikedUser(alex);
        like3.setTimeStamp(LocalDateTime.now());

        likeRepository.saveAll(List.of(like1, like2, like3));
    }
}
