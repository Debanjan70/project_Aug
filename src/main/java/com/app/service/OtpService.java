package com.app.service;

import com.app.entity.User;
import com.app.payload.OtpDetails;
import com.app.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class OtpService {
    private UserRepository userRepository;
    public OtpService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private final Map<String,OtpDetails> otpStorage = new HashMap<>();
    private static final int OTP_EXPIRY_TIME = 5;

    public String genarateOtp(String mobileNumber){

        Optional<User> opUser = userRepository.findByMobile(mobileNumber);
        if(opUser.isPresent()) {

            String otp = String.format("%06d", new Random().nextInt(999999));

            OtpDetails otpDetails = new OtpDetails(otp, System.currentTimeMillis());
            otpStorage.put(mobileNumber, otpDetails);

            return otp;
        }
        return "Mobile not found";
    }

    public boolean validateOtp(String mobileNumber, String otp){
        OtpDetails otpDetails = otpStorage.get(mobileNumber);
        if(otpDetails == null){
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long otpTime = otpDetails.getTimestamp();
        long timeDiffrence = TimeUnit.MILLISECONDS.toMinutes(currentTime - otpTime);

        if(timeDiffrence > OTP_EXPIRY_TIME){
            otpStorage.remove(mobileNumber);
            return false;
        }

        return otpDetails.getOtp().equals(otp);

    }
}
