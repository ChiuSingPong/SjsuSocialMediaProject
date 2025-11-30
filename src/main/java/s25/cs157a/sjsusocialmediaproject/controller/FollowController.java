package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Follow;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.FollowRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class FollowController {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;

    public FollowController(UserRepository userRepository,
                            FollowRepository followRepository) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
    }

    @PostMapping("/follow")
    public String follow(@RequestParam("friendId") Integer friendId,
                         HttpSession session,
                         HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        if (uid.equals(friendId)) {
            // cannot follow yourself; just go back
            return "redirect:" + getRefererOrHome(request);
        }

        Optional<User> userOpt = userRepository.findById(uid);
        Optional<User> friendOpt = userRepository.findById(friendId);
        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        User currentUser = userOpt.get();
        User friend = friendOpt.get();

        // only create if not already following
        if (!followRepository.existsByUserAndFriend(currentUser, friend)) {
            Follow follow = new Follow(currentUser, friend, LocalDateTime.now());
            followRepository.save(follow);
        }

        return "redirect:" + getRefererOrHome(request);
    }

    @PostMapping("/unfollow")
    public String unfollow(@RequestParam("friendId") Integer friendId,
                           HttpSession session,
                           HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<User> userOpt = userRepository.findById(uid);
        Optional<User> friendOpt = userRepository.findById(friendId);
        if (userOpt.isEmpty() || friendOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        User currentUser = userOpt.get();
        User friend = friendOpt.get();

        followRepository.deleteByUserAndFriend(currentUser, friend);

        return "redirect:" + getRefererOrHome(request);
    }

    private String getRefererOrHome(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null && !referer.isBlank()) ? referer : "/home";
    }
}
