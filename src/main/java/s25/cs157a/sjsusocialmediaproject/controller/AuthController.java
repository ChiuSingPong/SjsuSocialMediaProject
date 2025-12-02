package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class AuthController {

    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // --------------- LOGIN -----------------

    @GetMapping("/login")
    public String loginPage() {
        return "login";   // login.html
    }

    @PostMapping("/login")
    public String loginSubmit(
            @RequestParam String email,
            @RequestParam String password,
            HttpSession session,
            Model model
    ) {
        Optional<User> userOpt = userRepository.findByEmail(email);

        if (userOpt.isEmpty()) {
            model.addAttribute("error", "No account found with that email.");
            return "login";
        }

        User user = userOpt.get();

        // plain-text password check for now
        if (!user.getPassword().equals(password)) {
            model.addAttribute("error", "Incorrect password.");
            return "login";
        }

        // store user id in session
        session.setAttribute("userID", user.getId());

        // after login, go to home
        return "redirect:/home";
    }

    // --------------- SIGNUP -----------------

    @GetMapping("/signup")
    public String signupPage() {
        return "signup";  // signup.html
    }

    @PostMapping("/signup")
    public String signupSubmit(
            @RequestParam String fullname,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            HttpSession session,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("error", "Passwords do not match.");
            return "signup";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            model.addAttribute("error", "Email already registered.");
            return "signup";
        }

        User newUser = new User();
        newUser.setUsername(fullname);
        newUser.setEmail(email);
        newUser.setPassword(password); // in real projects, hash this!

        // THIS is the part that actually saves the user:
        userRepository.save(newUser);

        // Do NOT auto-login after signup → send to login page
        return "redirect:/login";
    }

    // --------------- LOGOUT -----------------

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    // --------------- ROOT ("/") -----------------

    @GetMapping("/")
    public String root(HttpSession session) {
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";   // not logged in → go to login
        }
        return "redirect:/home";        // logged in → go to home/feed
    }
}
