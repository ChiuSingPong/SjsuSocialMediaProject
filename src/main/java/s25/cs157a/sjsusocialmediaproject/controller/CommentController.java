package s25.cs157a.sjsusocialmediaproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import s25.cs157a.sjsusocialmediaproject.model.Comment;
import s25.cs157a.sjsusocialmediaproject.model.Post;
import s25.cs157a.sjsusocialmediaproject.model.User;
import s25.cs157a.sjsusocialmediaproject.repository.CommentRepository;
import s25.cs157a.sjsusocialmediaproject.repository.PostRepository;
import s25.cs157a.sjsusocialmediaproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
public class CommentController {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    public CommentController(UserRepository userRepository,
                             PostRepository postRepository,
                             CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    @PostMapping("/comment")
    public String addComment(@RequestParam("postId") Integer postId,
                             @RequestParam("content") String content,
                             HttpSession session,
                             HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        // Don't allow empty comments
        if (content == null || content.trim().isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        Optional<User> userOpt = userRepository.findById(uid);
        Optional<Post> postOpt = postRepository.findById(postId);

        if (userOpt.isEmpty() || postOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        User currentUser = userOpt.get();
        Post post = postOpt.get();

        Comment comment = new Comment();
        comment.setUser(currentUser);
        comment.setPost(post);
        comment.setContent(content.trim());
        comment.setTimeStamp(LocalDateTime.now());

        commentRepository.save(comment);

        return "redirect:" + getRefererOrHome(request);
    }

    @PostMapping("/comment/delete")
    public String deleteComment(@RequestParam("commentId") Integer commentId,
                                HttpSession session,
                                HttpServletRequest request) {

        Integer uid = (Integer) session.getAttribute("userID");
        if (uid == null) {
            return "redirect:/login";
        }

        Optional<Comment> commentOpt = commentRepository.findById(commentId);
        if (commentOpt.isEmpty()) {
            return "redirect:" + getRefererOrHome(request);
        }

        Comment comment = commentOpt.get();

        // Only comment owner or post owner can delete the comment
        boolean isCommentOwner = comment.getUser().getId().equals(uid);
        boolean isPostOwner = comment.getPost().getUser().getId().equals(uid);

        if (isCommentOwner || isPostOwner) {
            commentRepository.delete(comment);
        }

        return "redirect:" + getRefererOrHome(request);
    }

    private String getRefererOrHome(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        return (referer != null && !referer.isBlank()) ? referer : "/home";
    }
}

