package com.smc.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.smc.domain.User;
import com.smc.pojo.LoginResponse;
import com.smc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Value("${smc.registration-confirm-url}")
    private String registrationConfirmUrl;

    @Value("${spring.mail.username}")
    private String from;
    
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepo;

    @Override
    public int register(User user) throws Exception {
        List<User> userList = userRepo.findByEmail(user.getEmail());
        if (!userList.isEmpty()) {
            throw new Exception("User with email \"" + user.getEmail() + "\" already exists in our system.");
        }
        user.setPassword("");
        userRepo.save(user);
        sendConfirmationEmail(user.getId(), user.getEmail());
        return user.getId();
    }

    private void sendConfirmationEmail(int id, String email) throws MessagingException {
        String subject = "You Have Submitted a Registration Request";
        String url = registrationConfirmUrl + id;
        String content = "<html><body>" +
                "You have submitted a registration request, please click " +
                "<a href=\"" + url + "\">here</a>" +
                " to confirm. If you didn't raise this request, no action is needed.<br/>" +
                "If you can not click on the link, you can copy the link below:<br/>" + url +
                "</body></html>";
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    @Override
    public boolean confirmRegistration(User user) throws Exception {
        User u = userRepo.findUserById(user.getId());
        if (u.getConfirmed()) {
            throw new Exception("This user has already confirmed the registration.");
        }
        u.setPassword(user.getPassword());
        u.setConfirmed(true);
        userRepo.save(u);
        return true;
    }

    @Override
    public List<User> findAll() {
        return userRepo.findAll();
    }

    @Override
    public User findUserById(int id) {
        return userRepo.findUserById(id);
    }

    public String getToken(User user) {
        Date date = new Date(System.currentTimeMillis() + 3600000);
        String token = JWT.create()
                .withAudience(user.getId() + "")
                .withExpiresAt(date)
                .withClaim("id", user.getId())
                .withClaim("username", user.getUsername())
                .withClaim("email", user.getEmail())
                .withClaim("mobileNumber", user.getMobileNumber())
                .withClaim("admin", user.getAdmin())
                .withClaim("confirmed", user.getConfirmed())
                .sign(Algorithm.HMAC256(user.getPassword()));
        return token;
    }

    @Override
    public LoginResponse login(User user) throws Exception {
        String email = user.getEmail();
        String password = user.getPassword();
        List<User> userList = userRepo.findByEmailAndPassword(email, password);
        if (!userList.isEmpty()) {
            user = userList.get(0);
            String token = getToken(user);
            user.setPassword(null);
            return new LoginResponse(user, token);
        } else {
            throw new Exception("The user \"" + email + "\" doesn't exist or the password was incorrect.");
        }
    }

    @Override
    public boolean updateUser(User user) throws Exception {
        User u = userRepo.findUserById(user.getId());
        if (u == null) {
            throw new Exception("The user \"" + user.getEmail() + "\" doesn't exist.");
        }
        u.setUsername(user.getUsername());
        u.setMobileNumber(user.getMobileNumber());
        u.setPassword(user.getPassword());
        userRepo.save(u);
        return true;
    }

}
