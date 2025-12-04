package s25.cs157a.sjsusocialmediaproject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;

import java.util.List;

@RestController
@RequestMapping("/debug")
public class DebugController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PostRepository postRepository;

    public DebugController(UserRepository userRepository,
                           ProfileRepository profileRepository,
                           PostRepository postRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.postRepository = postRepository;
    }
    //use this link to see table
    //http://localhost:8080/debug/users
    // SELECT * FROM USERS;
    @GetMapping("/users")
    public List<User> allUsers() {
        return userRepository.findAll();
    }

    // SELECT * FROM PROFILES;
    @GetMapping("/profiles")
    public List<Profile> allProfiles() {
        return profileRepository.findAll();
    }

    // SELECT * FROM POSTS ORDER BY timeStamp DESC;
    @GetMapping("/posts")
    public List<Post> allPosts() {
        return postRepository.findAll();
    }
}
