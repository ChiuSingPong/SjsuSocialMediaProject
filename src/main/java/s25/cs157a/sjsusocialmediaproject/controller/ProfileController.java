package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Follow;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.FollowRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;

    public ProfileController(UserRepository userRepository,
                             PostRepository postRepository,
                             FollowRepository followRepository,
                             ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.profileRepository = profileRepository;
    }

    /** Show the logged-in user's profile page */
    @GetMapping("/profile")
    public String profile(Model model, HttpSession session) {

        // 1) Check if someone is logged in
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        // 2) Try to load that user safely
        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            // session has a stale/bad id → reset and force login
            session.invalidate();
            return "redirect:/login";
        }

        User currentUser = userOpt.get();

        // 3) Load profile (may not exist yet)
        Profile currentProfile =
                profileRepository.findById(currentUser.getId()).orElse(null);

        // 4) Load posts for this user
        List<Post> posts =
                postRepository.findByUserOrderByTimeStampDesc(currentUser);

        // 5) Load friends/following
        List<User> friends =
                followRepository.findByUser(currentUser)
                        .stream()
                        .map(Follow::getFriend)
                        .collect(Collectors.toList());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentProfile", currentProfile);
        model.addAttribute("posts", posts);
        model.addAttribute("friends", friends);

        return "profile"; // profile.html
    }

    /** Update the "intro"/bio section for the CURRENT logged in user */
    @PostMapping("/profile/intro")
    public String updateIntro(@RequestParam String bio, HttpSession session) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        User user = userOpt.get();

        Profile profile = profileRepository.findById(user.getId())
                .orElse(new Profile());
        profile.setUser(user);
        profile.setBio(bio);
        profile.setLastUpdated(LocalDateTime.now());

        profileRepository.save(profile);

        return "redirect:/profile";
    }
}

