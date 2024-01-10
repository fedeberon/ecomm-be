package com.ideaas.ecomm.ecomm.services;

import com.ideaas.ecomm.ecomm.domain.User;
import com.ideaas.ecomm.ecomm.repository.ScheduleDao;
import com.ideaas.ecomm.ecomm.repository.UserDao;
import com.ideaas.ecomm.ecomm.services.interfaces.IAuthenticationFacade;
import com.ideaas.ecomm.ecomm.services.interfaces.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.*;

@Service
public class UserService implements IUserService {

    private UserDao dao;
    private ScheduleDao scheduleDao;
    private IAuthenticationFacade authenticationFacade;

    @Autowired
    public UserService(final UserDao dao,
                       final IAuthenticationFacade authenticationFacade) {
        this.dao = dao;
        this.authenticationFacade = authenticationFacade;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = dao.getById(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

    }

    @Override
    public User save(final User user) {
        BCryptPasswordEncoder passwordEncoder =new BCryptPasswordEncoder();
        String password = passwordEncoder.encode(user.getPassword());
        user.setUsername(user.getEmail());
        user.setPassword(password);

        return dao.save(user);
    }

    @Override
    public User update(final User user) { 
        return dao.save(user);
    }

    @Override
    public Optional<User> get(String username) {
        return dao.findById(username);
    }

    @Override
    public List<User> findAll() {
        return dao.findAll();
    }


    @Override
    public User getCurrent(){
        String username = authenticationFacade.getAuthentication().getName();
        Optional<User> user = get(username);
        if (user.isPresent()) {
            return user.get();
        } else {
            return null;
        }
    }

    //METODOS PARA RECUPERACIÓN DE CONTRASEÑA
    private String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    private void sendPasswordResetEmail(String to, String resetUrl) throws MessagingException {
        // Set email properties
        String host = "smtp.mail.com";
        String port = "587";
        String username = "camaracomercialtest@mail.com";
        String password = "GPT3J63YXP4FNQB7VEJM";

        // Configure email properties
        Properties properties465 = new Properties();
        properties465.put("mail.smtp.host", "smtp.mail.com");
        properties465.put("mail.smtp.port", "465");
        properties465.put("mail.smtp.auth", "true");
        properties465.put("mail.smtp.ssl.enable", "true");
        properties465.put("mail.smtp.ssl.trust", "smtp.mail.com");

        // Set the username and password directly in the Session properties
        //properties.put("mail.smtp.user", username);
        //properties.put("mail.smtp.password", password);

        // Get the default Session object
        Session session = Session.getInstance(properties465, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        //DEBUG ANTI SSL (PRUEBAS)
        session.setDebug(true); // Add this line for debugging

        // Create a default MimeMessage object
        MimeMessage message = new MimeMessage(session);
        // Set From: header field
        message.setFrom(new InternetAddress(username));
        // Set To: header field
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        // Set Subject: header field
        message.setSubject("Reestablecimiento de contraseña");

        // Set the actual message
        message.setText("Haz clic en el siguiente enlace para reestablecer tu contraseña: : " + resetUrl);

        // Send message
        Transport.send(message);
    }

    @Override
    public Boolean sendNewPswrdMail(User user) {
        String resetToken = generateResetToken();

        //TESTING
        String resetUrl = "http://localhost:3000/reset-password?token=" + resetToken;
        //PRODUCCIÓN
        //String resetUrl = "https://white-label-gules.vercel.app/reset-password?token=" + resetToken;

        // Send the password reset email
        try {
            sendPasswordResetEmail(user.getEmail(), resetUrl);
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
    }
}
