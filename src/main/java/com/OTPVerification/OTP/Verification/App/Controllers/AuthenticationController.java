package com.OTPVerification.OTP.Verification.App.Controllers;
import com.OTPVerification.OTP.Verification.App.DTO.LoginDTO;
import com.OTPVerification.OTP.Verification.App.DTO.RegisterDTO;
import com.OTPVerification.OTP.Verification.App.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("admin")
@RequiredArgsConstructor
public class AuthenticationController {


    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterDTO registerDTO) {
        return new ResponseEntity<>(userService.register(registerDTO), HttpStatus.OK);
    }

    @PutMapping("/verifyAdmin")
    public ResponseEntity<String> verifyAdmin(@RequestParam String email, @RequestParam String otp) {
        return new ResponseEntity<>(userService.verifyAdmin(email,otp),HttpStatus.OK);
    }

    @PutMapping("/regenerate-otp")
    public ResponseEntity<String> regenerateOtp(@RequestParam String email) {
        return new ResponseEntity<>(userService.regenerateOtp(email), HttpStatus.OK);
    }

    @PutMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO loginDto) {
        return new ResponseEntity<>(userService.login(loginDto), HttpStatus.OK);
    }

}
