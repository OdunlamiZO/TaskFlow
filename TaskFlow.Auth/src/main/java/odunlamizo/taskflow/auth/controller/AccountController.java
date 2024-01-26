package odunlamizo.taskflow.auth.controller;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import odunlamizo.taskflow.auth.dto.FileDto;
import odunlamizo.taskflow.auth.dto.UserDto;
import odunlamizo.taskflow.auth.model.EmailVerificationToken;
import odunlamizo.taskflow.auth.model.File;
import odunlamizo.taskflow.auth.model.ProfilePhoto;
import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.EmailVerificationTokenRepository;
import odunlamizo.taskflow.auth.repository.ProfilePhotoRepository;
import odunlamizo.taskflow.auth.repository.UserRepository;
import odunlamizo.taskflow.auth.service.FileService;
import odunlamizo.taskflow.auth.service.UserService;
import odunlamizo.taskflow.auth.service.ValidationService;
import odunlamizo.taskflow.auth.util.Models;

@Controller
public class AccountController {

    @Value("${odunlamizo.taskflow.web.url}")
    private String webUrl;

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private ProfilePhotoRepository profilePhotoRepository;

    @Autowired
    private EmailVerificationTokenRepository verificationTokenRepository;

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String signup(@ModelAttribute UserDto user) {
        boolean isValid = validationService.validate(user);
        if (!isValid) {
            return ""; // what to do?
        }
        return String.format("redirect:%s", userService.save(user));
    }

    @RequestMapping(value = "/profile/update", method = RequestMethod.POST)
    public String updateProfile(UserDto user, HttpServletRequest request) {
        // set values for email and password so validation will not fail
        user.setEmail("john@taskflow.xxx");
        user.setPassword("i8BinKay");
        boolean isValid = validationService.validate(user);
        if (!isValid) {
            return ""; // what to do?
        }
        userService.updateProfile(user, request);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/profile/photo/upload", method = RequestMethod.POST)
    public ResponseEntity<FileDto> uploadPhoto(@RequestBody FileDto _file) {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        File file = fileService.save(_file);
        Optional<ProfilePhoto> _profilePhoto = profilePhotoRepository.findByUser(principal.getUsername());
        if (_profilePhoto.isPresent()) {
            profilePhotoRepository.delete(_profilePhoto.get());
        }
        ProfilePhoto profilePhoto = new ProfilePhoto();
        profilePhoto.setFile(file);
        profilePhoto.setUser(principal.getUsername());
        profilePhoto = profilePhotoRepository.save(profilePhoto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(Models.modelToDto(Optional.of(profilePhoto.getFile()), FileDto.class));
    }

    @RequestMapping(value = "/profile/photo/delete", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deletePhoto() {
        UserDetails principal = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ProfilePhoto> _profilePhoto = profilePhotoRepository.findByUser(principal.getUsername());
        if (_profilePhoto.isPresent()) {
            profilePhotoRepository.delete(_profilePhoto.get());
        }
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/verify", method = RequestMethod.POST)
    public String verify(@RequestParam("token") String id) {
        Optional<EmailVerificationToken> _token = verificationTokenRepository.findById(id);
        if (_token.isPresent()) {
            EmailVerificationToken token = _token.get();
            User user = token.getUser();
            user.setEnabled(true);
            userRepository.save(user);
            verificationTokenRepository.deleteById(id);
        } else {
            return ""; // what to do?
        }
        return String.format("redirect:%s/login", webUrl);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public ResponseEntity<Map<String, UserDto>> user(@RequestParam String query) {
        UserDto user = Models.modelToDto(
                query.contains("@") ? userRepository.findByEmail(query) : userRepository.findByUsername(query),
                UserDto.class);
        return ResponseEntity.status(HttpStatus.OK).body(Collections.singletonMap("user", user));
    }

}
