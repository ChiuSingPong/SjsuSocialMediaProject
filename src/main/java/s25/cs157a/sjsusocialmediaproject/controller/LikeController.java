package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Like;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.LikeRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class LikeController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    public LikeController(UserRepository userRepository,
                          PostRepository postRepository,
                          LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
    }

    @PostMapping("/like")
    public String likePost(@RequestParam("postId") Integer postId,
                           HttpSession session,
                           HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        Optional<Post> postOpt = postRepository.findById(postId);

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        User currentUser = userOpt.get();
        Post post = postOpt.get();

        // Only create like if not already liked
        if (!likeRepository.existsByUserAndPost(currentUser, post)) {
            Like like = new Like(currentUser, post, post.getUser(), LocalDateTime.now());
            likeRepository.save(like);
        }

        return "redirect:" + getRefererOrHome(request);
    }

    @PostMapping("/unlike")
    public String unlikePost(@RequestParam("postId") Integer postId,
                             HttpSession session,
                             HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        Optional<Post> postOpt = postRepository.findById(postId);

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        User currentUser = userOpt.get();
        Post post = postOpt.get();

        likeRepository.deleteByUserAndPost(currentUser, post);

        return "redirect:" + getRefererOrHome(request);
    }

    private String getRefererOrHome(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null && !referer.isBlank()) ? referer : "/home";
    }
}

