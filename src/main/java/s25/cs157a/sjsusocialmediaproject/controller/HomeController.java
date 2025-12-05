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
import s25.cs157a.sjsusocialmediaproject.repository.LikeRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.ProfileRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final FollowRepository followRepository;
    private final ProfileRepository profileRepository;
    private final LikeRepository likeRepository;

    @Value("${file.upload-dir:uploads}")
    private String uploadDir;


    public HomeController(UserRepository userRepository,
                          PostRepository postRepository,
                          FollowRepository followRepository,
                          ProfileRepository profileRepository,
                          LikeRepository likeRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.followRepository = followRepository;
        this.profileRepository = profileRepository;
        this.likeRepository = likeRepository;
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

        // Get set of post IDs that current user has liked
        Set<Integer> likedPostIds = likeRepository.findByUser(currentUser)
                .stream()
                .map(like -> like.getPost().getId())
                .collect(Collectors.toSet());

        model.addAttribute("currentUser", currentUser);
        model.addAttribute("currentProfile", profile);
        model.addAttribute("posts", posts);
        model.addAttribute("contacts", contacts);
        model.addAttribute("contactImages", contactImages);
        model.addAttribute("likedPostIds", likedPostIds);

        return "home";
    }

    @PostMapping("/post")
    public String createPost(@RequestParam("content") String content,
                             @RequestParam(value = "mediaFile", required = false) MultipartFile mediaFile,
                             HttpSession session) {

        // 1. Check login
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        // 2. Get user
        Optional<User> userOpt = userRepository.findById(uid);
        if (userOpt.isEmpty()) {
            session.invalidate();
            return "redirect:/login";
        }
        User currentUser = userOpt.get();

        // 3. Optional media upload
        String mediaUrl = null;
        if (mediaFile != null && !mediaFile.isEmpty()) {
            try {
                Path uploadPath = Paths.get(uploadDir).toAbsolutePath().normalize();
                Files.createDirectories(uploadPath);

                String originalFilename = mediaFile.getOriginalFilename();
                String ext = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    ext = originalFilename.substring(originalFilename.lastIndexOf("."));
                }

                String fileName = "post_" + currentUser.getId() + "_" + System.currentTimeMillis() + ext;
                Path filePath = uploadPath.resolve(fileName);

                // save file
                mediaFile.transferTo(filePath.toFile());

                mediaUrl = "/uploads/" + fileName;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // 4. Save post
        Post newPost = new Post();
        newPost.setUser(currentUser);
        newPost.setContent(content);
        newPost.setTimeStamp(LocalDateTime.now());
        newPost.setMediaUrl(mediaUrl);

        // INSERT INTO posts (userID, content, mediaUrl, timeStamp)
        // VALUES (?, ?, NULL, ?);
        postRepository.save(newPost);

        return "redirect:/home";
    }

    @PostMapping("/post/delete")
    public String deletePost(@RequestParam("postId") Integer postId,
                             HttpSession session) {

        // 1. Check if user is logged in
        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        // 2. Find the post in database (SELECT * FROM posts WHERE postID = ?)
        Optional<Post> postOpt = postRepository.findById(postId);
        if (postOpt.isEmpty()) {
            return "redirect:/home"; // Post doesn't exist
        }

        Post post = postOpt.get();

        // 3. Authorization: Only the post owner can delete their post
        if (!post.getUser().getId().equals(uid)) {
            return "redirect:/home"; // Not authorized
        }

        // 4. Delete from database (DELETE FROM posts WHERE postID = ?)
        postRepository.delete(post);

        // 5. Redirect back to home
        return "redirect:/home";
    }
}
