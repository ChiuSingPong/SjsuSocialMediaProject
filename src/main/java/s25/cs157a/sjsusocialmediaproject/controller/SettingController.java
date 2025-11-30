package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class SettingController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    public SettingController(UserRepository userRepository,
                             ProfileRepository profileRepository) {
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
    }

    @GetMapping("/settings")
    public String settingsPage(Model model, HttpSession session) {
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

        Profile profile = profileRepository.findById(currentUser.getId())
                .orElse(new Profile());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("profile", profile);

        return "settings"; // settings.html
    }

    @PostMapping("/settings")
    public String updateSettings(
            @RequestParam String email,
            @RequestParam(required=false) String profileImage,
            @RequestParam String username,
            @RequestParam(required=false) String bio,
            HttpSession session
    ) {

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

        Profile profile = profileRepository.findById(currentUser.getId())
                .orElse(new Profile());

        profile.setUser(currentUser);
        profile.setBio(bio);
        profile.setProfileImage(profileImage);
        profile.setLastUpdated(LocalDateTime.now());

        profileRepository.save(profile);

        // after changing settings, go back to profile or home
        return "redirect:/profile";
    }
}
