package odunlamizo.taskflow.auth.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import odunlamizo.taskflow.auth.dto.FileDto;
import odunlamizo.taskflow.auth.dto.UserDto;
import odunlamizo.taskflow.auth.model.ProfilePhoto;
import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.ProfilePhotoRepository;
import odunlamizo.taskflow.auth.repository.UserRepository;
import odunlamizo.taskflow.auth.util.Models;

@Controller
public class ViewController {

    @Value("${odunlamizo.taskflow.web.url}")
    private String webUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signup(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "signup";
    }

    @RequestMapping(value = "/verify", method = RequestMethod.GET)
    public String verify(@RequestParam String email, Model model) {
        model.addAttribute("email", email);
        return "verify";
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public String profile(Model model, HttpServletRequest request) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<User> user = userRepository.findByUsername(principal.getUsername());
        model.addAttribute("user", Models.modelToDto(user, UserDto.class));
        Optional<ProfilePhoto> photo = profilePhotoRepository.findByUser(principal.getUsername());
        model.addAttribute("photo",
                photo.isPresent() ? Models.modelToDto(Optional.of(photo.get().getFile()), FileDto.class) : null);
        model.addAttribute("webUrl", webUrl);
        model.addAttribute("request", request);
        return "profile";
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    public String settings() {
        return "settings";
    }

}
