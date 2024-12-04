package com.OTPVerification.OTP.Verification.App.Repository;

import com.OTPVerification.OTP.Verification.App.Entity.AppUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<AppUser, Integer> {
   Optional<AppUser> findByEmail(String email);
}
