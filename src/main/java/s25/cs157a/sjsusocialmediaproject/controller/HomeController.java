package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDateTime;
import s25.cs157a.sjsusocialmediaproject.model.Follow;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.FollowRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

    public HomeController(UserRepository userRepository,
                          PostRepository postRepository,
                          FollowRepository followRepository,
                          ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User currentUser = userOpt.get();

        // ✅ use the injected bean, not ProfileRepository.class
        Profile profile = profileRepository.findById(currentUser.getId())
                .orElse(null);

        // friends / people current user follows
        List<User> contacts = followRepository.findByUser(currentUser)
                .stream()
                .map(Follow::getFriend)
                .toList();

// build list of users whose posts we want to see: current user + friends
        List<User> visibleUsers = new java.util.ArrayList<>();
        visibleUsers.add(currentUser);
        visibleUsers.addAll(contacts);

// only posts from current user + friends
        List<Post> posts = visibleUsers.isEmpty()
                ? java.util.Collections.emptyList()
                : postRepository.findByUserInOrderByTimeStampDesc(visibleUsers);

        // map contact.id -> profileImage URL (may be null)
        Map<Integer, String> contactImages = new HashMap<>();
        for (User c : contacts) {
            profileRepository.findById(c.getId())
                    .ifPresent(p -> contactImages.put(c.getId(), p.getProfileImage()));
        }

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentProfile", profile);
        model.addAttribute("posts", posts);
        model.addAttribute("contacts", contacts);
        model.addAttribute("contactImages", contactImages);

        return "home";
    }

    @PostMapping("/post")
    public String createPost(@RequestParam("content") String content,
                             HttpSession session) {

        // 1. Check if user is logged in
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        // 2. Get the current user from database (SELECT * FROM users WHERE userID = ?)
        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        User currentUser = userOpt.get();

        // 3. Create new Post object
        Post newPost = new Post();
        newPost.setUser(currentUser);          
        newPost.setContent(content);           
        newPost.setTimeStamp(LocalDateTime.now()); 

        // 4. Save to database (INSERT INTO posts VALUES (...))
        postRepository.save(newPost);

        // 5. Redirect back to home to see the new post
        return "redirect:/home";
    }
}
