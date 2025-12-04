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

        if (userRepository.count() > 0) {
            return;
        }

        // --- USERS ---
        User alex = createUser("alexmartinez", "alex@sjsu.edu", "password123");
        User sarah = createUser("sarahj", "sarah@sjsu.edu", "password123");
        User mike = createUser("mikechen", "mike@sjsu.edu", "password123");
        User jess = createUser("jpark", "jessica@sjsu.edu", "password123");
        User david = createUser("dkim", "david@sjsu.edu", "password123");
        User emma = createUser("ewilson", "emma@sjsu.edu", "password123");
        User ryan = createUser("ryanthompson", "ryan@sjsu.edu", "password123");
        User olivia = createUser("oliviag", "olivia@sjsu.edu", "password123");
        User ethan = createUser("ethanlee", "ethan@sjsu.edu", "password123");
        User sophia = createUser("sophiam", "sophia@sjsu.edu", "password123");
        User jacob = createUser("jacobr", "jacob@sjsu.edu", "password123");
        User ava = createUser("avajones", "ava@sjsu.edu", "password123");
        User william = createUser("willb", "william@sjsu.edu", "password123");
        User isabella = createUser("isabellac", "isabella@sjsu.edu", "password123");
        User james = createUser("jamesw", "james@sjsu.edu", "password123");

        userRepository.saveAll(List.of(alex, sarah, mike, jess, david, emma, ryan, olivia,
                ethan, sophia, jacob, ava, william, isabella, james));

        // --- PROFILES ---
        Profile pAlex = createProfile(alex, "SJSU Student - CS Major", "https://via.placeholder.com/80x80.png?text=A");
        Profile pSarah = createProfile(sarah, "Business Major | Marketing Club", "https://via.placeholder.com/80x80.png?text=S");
        Profile pMike = createProfile(mike, "Mechanical Engineering | Car enthusiast", "https://via.placeholder.com/80x80.png?text=M");
        Profile pJess = createProfile(jess, "Psychology Student | Mental Health Advocate", "https://via.placeholder.com/80x80.png?text=J");
        Profile pDavid = createProfile(david, "Computer Engineering | Robotics Club", "https://via.placeholder.com/80x80.png?text=D");
        Profile pEmma = createProfile(emma, "Nursing Student | Future RN", "https://via.placeholder.com/80x80.png?text=E");
        Profile pRyan = createProfile(ryan, "Software Engineering | Hackathon Winner", "https://via.placeholder.com/80x80.png?text=R");
        Profile pOlivia = createProfile(olivia, "Art & Design Major | Digital Artist", "https://via.placeholder.com/80x80.png?text=O");
        Profile pEthan = createProfile(ethan, "Data Science | AI Researcher", "https://via.placeholder.com/80x80.png?text=Et");
        Profile pSophia = createProfile(sophia, "Biology Pre-Med | Aspiring Doctor", "https://via.placeholder.com/80x80.png?text=So");
        Profile pJacob = createProfile(jacob, "Physics Major | Astronomy Club", "https://via.placeholder.com/80x80.png?text=Ja");
        Profile pAva = createProfile(ava, "Communications | Campus Radio Host", "https://via.placeholder.com/80x80.png?text=Av");
        Profile pWilliam = createProfile(william, "Finance Major | Investment Club", "https://via.placeholder.com/80x80.png?text=W");
        Profile pIsabella = createProfile(isabella, "Environmental Studies | Sustainability", "https://via.placeholder.com/80x80.png?text=I");
        Profile pJames = createProfile(james, "History Major | Museum Intern", "https://via.placeholder.com/80x80.png?text=Jm");

        profileRepository.saveAll(List.of(pAlex, pSarah, pMike, pJess, pDavid, pEmma, pRyan, pOlivia,
                pEthan, pSophia, pJacob, pAva, pWilliam, pIsabella, pJames));

        // --- POSTS ---
        Post post1 = createPost(alex, "Just finished my finals! So excited for winter break! Go Spartans!", 
                "https://images.pexels.com/photos/462331/pexels-photo-462331.jpeg?auto=compress&cs=tinysrgb&w=1200", 0);
        Post post2 = createPost(sarah, "Had a great study session today at MLK Library.", null, 2);
        Post post3 = createPost(mike, "Engineering club meeting was fun today. Built a mini robot!", null, 3);
        Post post4 = createPost(jess, "Psychology project is finally done. Time to celebrate!", null, 4);
        Post post5 = createPost(david, "Just got accepted into the robotics research program!", null, 5);
        Post post6 = createPost(emma, "Clinical rotations start next week. Nervous but excited!", null, 6);
        Post post7 = createPost(ryan, "Won first place at the SJSU Hackathon! Team effort pays off.", 
                "https://images.pexels.com/photos/3861969/pexels-photo-3861969.jpeg?auto=compress&cs=tinysrgb&w=1200", 7);
        Post post8 = createPost(olivia, "New digital art piece complete. What do you think?", 
                "https://images.pexels.com/photos/1762851/pexels-photo-1762851.jpeg?auto=compress&cs=tinysrgb&w=1200", 8);
        Post post9 = createPost(ethan, "Machine learning model finally hitting 95% accuracy!", null, 9);
        Post post10 = createPost(sophia, "MCAT prep is no joke. Coffee is my best friend right now.", null, 10);
        Post post11 = createPost(jacob, "Captured some amazing shots of the lunar eclipse last night!", 
                "https://images.pexels.com/photos/47367/full-moon-moon-bright-sky-47367.jpeg?auto=compress&cs=tinysrgb&w=1200", 11);
        Post post12 = createPost(ava, "Just wrapped up my radio show. Thanks for tuning in everyone!", null, 12);
        Post post13 = createPost(william, "Stock market analysis project complete. Learned so much about trading.", null, 13);
        Post post14 = createPost(isabella, "Campus sustainability event was a huge success! 500+ attendees.", null, 14);
        Post post15 = createPost(james, "Spending my Saturday at the San Jose Museum of Art. So inspiring!", null, 15);

        postRepository.saveAll(List.of(post1, post2, post3, post4, post5, post6, post7, post8, post9,
                post10, post11, post12, post13, post14, post15));

        // --- COMMENTS ---
        Comment c1 = createComment(post1, sarah, "Congrats Alex! Enjoy the break!");
        Comment c2 = createComment(post1, mike, "Nice work, you earned it.");
        Comment c3 = createComment(post2, alex, "Library study sessions are the best.");
        Comment c4 = createComment(post3, david, "That robot sounds awesome! Can I join next time?");
        Comment c5 = createComment(post4, emma, "Congrats Jess! Psychology is so interesting.");
        Comment c6 = createComment(post5, mike, "That's huge! Congratulations David!");
        Comment c7 = createComment(post6, sophia, "You'll do great Emma! Nursing is a noble path.");
        Comment c8 = createComment(post7, ethan, "Amazing job Ryan! What did you build?");
        Comment c9 = createComment(post7, alex, "First place?! That's incredible!");
        Comment c10 = createComment(post8, jess, "This is beautiful Olivia! Love the colors.");
        Comment c11 = createComment(post9, ryan, "95%? That's impressive. What framework are you using?");
        Comment c12 = createComment(post10, emma, "Hang in there Sophia! You've got this!");
        Comment c13 = createComment(post11, olivia, "Wow Jacob, these shots are stunning!");
        Comment c14 = createComment(post12, isabella, "I was listening! Great show Ava!");
        Comment c15 = createComment(post13, david, "Finance seems complex. Would love to learn more.");

        commentRepository.saveAll(List.of(c1, c2, c3, c4, c5, c6, c7, c8, c9, c10, c11, c12, c13, c14, c15));

        // --- LIKES ---
        likeRepository.save(createLike(sarah, post1, alex));
        likeRepository.save(createLike(mike, post1, alex));
        likeRepository.save(createLike(jess, post1, alex));
        likeRepository.save(createLike(david, post1, alex));
        likeRepository.save(createLike(emma, post1, alex));
        likeRepository.save(createLike(alex, post2, sarah));
        likeRepository.save(createLike(mike, post2, sarah));
        likeRepository.save(createLike(david, post3, mike));
        likeRepository.save(createLike(ryan, post3, mike));
        likeRepository.save(createLike(mike, post5, david));
        likeRepository.save(createLike(ethan, post5, david));

        likeRepository.save(createLike(alex, post7, ryan));
        likeRepository.save(createLike(sarah, post7, ryan));
        likeRepository.save(createLike(jess, post8, olivia));
        likeRepository.save(createLike(sophia, post11, jacob));

        // --- FOLLOWING: Alex follows everyone ---
        followRepository.save(createFollow(alex, sarah));
        followRepository.save(createFollow(alex, mike));
        followRepository.save(createFollow(alex, jess));
        followRepository.save(createFollow(alex, david));
        followRepository.save(createFollow(alex, emma));
        followRepository.save(createFollow(alex, ryan));
        followRepository.save(createFollow(alex, olivia));
        followRepository.save(createFollow(alex, ethan));
        followRepository.save(createFollow(alex, sophia));
        followRepository.save(createFollow(alex, jacob));
        followRepository.save(createFollow(alex, ava));
        followRepository.save(createFollow(alex, william));
        followRepository.save(createFollow(alex, isabella));
        followRepository.save(createFollow(alex, james));
        followRepository.save(createFollow(sarah, alex));
        followRepository.save(createFollow(sarah, jess));
        followRepository.save(createFollow(mike, alex));
        followRepository.save(createFollow(mike, david));
        followRepository.save(createFollow(ryan, alex));
        followRepository.save(createFollow(ryan, ethan));
        followRepository.save(createFollow(olivia, ava));
        followRepository.save(createFollow(sophia, emma));
        followRepository.save(createFollow(jacob, ethan));
    }

    private User createUser(String username, String email, String password) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        return user;
    }

    private Profile createProfile(User user, String bio, String imageUrl) {
        Profile profile = new Profile();
        profile.setUser(user);
        profile.setBio(bio);
        profile.setProfileImage(imageUrl);
        profile.setLastUpdated(LocalDateTime.now());
        return profile;
    }

    private Post createPost(User user, String content, String mediaUrl, int hoursAgo) {
        Post post = new Post();
        post.setUser(user);
        post.setContent(content);
        post.setMediaUrl(mediaUrl);
        post.setTimeStamp(LocalDateTime.now().minusHours(hoursAgo));
        return post;
    }

    private Comment createComment(Post post, User user, String content) {
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setContent(content);
        comment.setTimeStamp(LocalDateTime.now());
        return comment;
    }

    private Like createLike(User user, Post post, User postOwner) {
        Like like = new Like();
        like.setId(new LikeId(user.getId(), post.getId()));
        like.setUser(user);
        like.setPost(post);
        like.setLikedUser(postOwner);
        like.setTimeStamp(LocalDateTime.now());
        return like;
    }

    private Follow createFollow(User follower, User followed) {
        Follow follow = new Follow();
        follow.setId(new FollowId(follower.getId(), followed.getId()));
        follow.setUser(follower);
        follow.setFriend(followed);
        follow.setTimeStamp(LocalDateTime.now());
        return follow;
    }
}
