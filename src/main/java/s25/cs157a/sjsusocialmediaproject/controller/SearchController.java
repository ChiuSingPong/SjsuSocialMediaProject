package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Follow;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.FollowRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class SearchController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ProfileRepository profileRepository;
    private final FollowRepository followRepository;

    public SearchController(UserRepository userRepository,
                            PostRepository postRepository,
                            ProfileRepository profileRepository,
                            FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.profileRepository = profileRepository;
        this.followRepository = followRepository;
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q", required = false) String query,
                         HttpSession session,
                         Model model) {

        // --- auth check ---
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }

        // --- current user + profile ---
        User currentUser = userOpt.get();
        Profile currentProfile = profileRepository.findById(currentUser.getId())
                .orElse(null);

        // --- who am I already following? (for Follow/Unfollow buttons) ---
        Set<Integer> followingIds = followRepository.findByUser(currentUser)
                .stream()
                .map(f -> f.getFriend().getId())
                .collect(Collectors.toSet());

        // --- search results ---
        List<User> userResults;
        List<Post> postResults;

        if (query == null || query.trim().isEmpty()) {
            userResults = Collections.emptyList();
            postResults = Collections.emptyList();
        } else {
            String q = query.trim();
            userResults = userRepository
                    .findByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(q, q);
            postResults = postRepository
                    .findByContentContainingIgnoreCaseOrderByTimeStampDesc(q);
        }

        // --- model ---
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentProfile", currentProfile);
        model.addAttribute("followingIds", followingIds);
        model.addAttribute("q", query);
        model.addAttribute("userResults", userResults);
        model.addAttribute("postResults", postResults);

        return "search";
    }
}
