package com.OTPVerification.OTP.Verification.App.Service;
import com.OTPVerification.OTP.Verification.App.DTO.LoginDTO;
import com.OTPVerification.OTP.Verification.App.DTO.RegisterDTO;
import com.OTPVerification.OTP.Verification.App.Entity.AppUser;
import com.OTPVerification.OTP.Verification.App.Repository.UserRepository;
import com.OTPVerification.OTP.Verification.App.Util.EmailUtil;
import com.OTPVerification.OTP.Verification.App.Util.OTPUtil;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.Duration;
import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private OTPUtil otpUtil;
    @Autowired
    private EmailUtil emailUtil;
    @Autowired
    private UserRepository userRepository;


    public String register(RegisterDTO registerDTO) {
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(registerDTO.getEmail(), otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send OTP please try again!!!");
        }
        AppUser appUser = new AppUser();
        appUser.setName(registerDTO.getName());
        appUser.setEmail(registerDTO.getEmail());
        appUser.setPassword(registerDTO.getPassword());
        appUser.setOTP(otp);
        appUser.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(appUser);
        return "User registration successful";
    }


       public String verifyAdmin(String email,String otp){
           AppUser appUser = userRepository.findByEmail(email)
                   .orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
           if (appUser.getOTP().equals(otp) && Duration.between(appUser.getOtpGeneratedTime(),
                   LocalDateTime.now()).getSeconds() < (1*60)) {
               appUser.setActive(true);
               userRepository.save(appUser);
               return "OTP verified you can login";
           }
           return "Please regenerate otp and try again";
       }

    public String regenerateOtp(String email) {
        AppUser appUser = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found with this email: " + email));
        String otp = otpUtil.generateOtp();
        try {
            emailUtil.sendOtpEmail(email, otp);
        } catch (MessagingException e) {
            throw new RuntimeException("Unable to send otp please try again");
        }
        appUser.setOTP(otp);
        appUser.setOtpGeneratedTime(LocalDateTime.now());
        userRepository.save(appUser);
        return "Email sent... please verify account within 1 minute";
    }

    public String login(LoginDTO loginDto) {
        AppUser appUser = userRepository.findByEmail(loginDto.getEmail()).orElseThrow(() -> new RuntimeException("User not found with this email: " + loginDto.getEmail()));
        if (!loginDto.getPassword().equals(appUser.getPassword())) {
            return "Password is incorrect";
        } else if (!appUser.isActive()) {
            return "your account is not verified";
        }
        return "Login successful";
    }
}

