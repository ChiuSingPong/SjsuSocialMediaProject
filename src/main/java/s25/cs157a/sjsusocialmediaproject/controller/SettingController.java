package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import s25.cs157a.sjsusocialmediaproject.model.Profile;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Controller
public class SettingController {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;

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
        model.addAttribute("currentProfile", profile);

        return "settings";
    }

    @PostMapping("/settings")
    public String updateSettings(@RequestParam(required = false) String bio,
                                 @RequestParam(required = false) String email,
                                 @RequestParam(required = false) String username,
                                 @RequestParam(required = false) String password,
                                 @RequestParam(name = "profileImageFile", required = false)
                                 MultipartFile profileImageFile,
                                 HttpSession session,
                                 Model model) {

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

        // Load or create profile once and reuse
        Profile profile = profileRepository.findById(currentUser.getId())
                .orElse(new Profile());
        profile.setUser(currentUser);

        // ---------- VALIDATION: duplicate email ----------
        if (email != null && !email.isBlank() && !email.equals(currentUser.getEmail())) {
            var existingEmailUser = userRepository.findByEmail(email);
            if (existingEmailUser.isPresent()
                    && !existingEmailUser.get().getId().equals(currentUser.getId())) {

                model.addAttribute("currentUser", currentUser);
                model.addAttribute("currentProfile", profile);
                model.addAttribute("error", "Email is already in use.");

                return "settings";
            }
        }

        // ---------- VALIDATION: duplicate username ----------
        if (username != null && !username.isBlank() && !username.equals(currentUser.getUsername())) {
            var existingUsernameUser = userRepository.findByUsername(username);
            if (existingUsernameUser.isPresent()
                    && !existingUsernameUser.get().getId().equals(currentUser.getId())) {

                model.addAttribute("currentUser", currentUser);
                model.addAttribute("currentProfile", profile);
                model.addAttribute("error", "Username is already taken.");

                return "settings";
            }
        }

        // ---------- UPDATE USER FIELDS AFTER PASSING VALIDATION ----------
        if (email != null && !email.isBlank()) {
            currentUser.setEmail(email);
        }
        if (username != null && !username.isBlank()) {
            currentUser.setUsername(username);
        }
        if (password != null && !password.isBlank()) {
            // For class project; in real app you would hash this
            currentUser.setPassword(password);
        }
        userRepository.save(currentUser);

        // ---------- PROFILE FIELDS ----------
        if (bio != null) {
            profile.setBio(bio);
        }

        // handle image upload
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                String originalFilename = profileImageFile.getOriginalFilename();
                String extension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    extension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String fileName = "profile_" + currentUser.getId() + "_" + System.currentTimeMillis() + extension;
                Path filePath = uploadPath.resolve(fileName);

                profileImageFile.transferTo(filePath.toFile());
                profile.setProfileImage("/uploads/" + fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        profile.setLastUpdated(LocalDateTime.now());
        profileRepository.save(profile);

        return "redirect:/profile";
    }

}
