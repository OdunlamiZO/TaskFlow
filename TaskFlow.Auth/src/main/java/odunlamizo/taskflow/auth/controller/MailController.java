package odunlamizo.taskflow.auth.controller;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import odunlamizo.taskflow.auth.model.EmailVerificationToken;
import odunlamizo.taskflow.auth.model.User;
import odunlamizo.taskflow.auth.repository.EmailVerificationTokenRepository;
import odunlamizo.taskflow.auth.repository.UserRepository;
import odunlamizo.taskflow.auth.service.MailService;

@Controller
public class MailController {

    @Value("${odunlamizo.taskflow.mail.no-reply}")
    private String noReply;

    @Autowired
    private MailService mailService;

    @Autowired
    private ResourceLoader resourceLoader;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private EmailVerificationTokenRepository verificationTokenRepository;

    @RequestMapping(value = "/mail/verification", method = RequestMethod.POST)
    public String verification(@RequestParam String email) {
        Optional<User> _user = userRepository.findByEmail(email);
        if (_user.isPresent()) {
            Context thymeleafContext = new Context();
            User user = _user.get();
            thymeleafContext.setVariable("name", user.getName().getFirstName());
            EmailVerificationToken verificationToken = verificationTokenRepository.findByUser(user).orElseGet(() -> {
                EmailVerificationToken _verificationToken = new EmailVerificationToken();
                _verificationToken.setUser(user);
                _verificationToken = verificationTokenRepository.save(_verificationToken);
                return _verificationToken;
            });
            thymeleafContext.setVariable("token", verificationToken.getId());
            Resource logo = resourceLoader.getResource("classpath:static/images/logo-text.png");
            try {
                byte[] logoBytes = FileCopyUtils.copyToByteArray(logo.getInputStream());
                thymeleafContext.setVariable("logo", Base64.getEncoder().encodeToString(logoBytes));
            } catch (IOException e) {
                // log exception
            }
            thymeleafContext.setVariable("url",
                    ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString());
            String htmlBody = templateEngine.process("email.html", thymeleafContext);
            try {
                mailService.send(user.getEmail(), noReply, "[TaskFlow]: Email Verification", htmlBody);
            } catch (MessagingException e) {
                // log exception
            }
        } else {
            return ""; // what to do?
        }
        return String.format("redirect:/verify?email=%s", email);
    }

}
