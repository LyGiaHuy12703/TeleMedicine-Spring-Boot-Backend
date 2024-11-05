package org.telemedicine.server.utils;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import jakarta.annotation.Nullable;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.exception.AppException;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Random;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
public class helper {
    @NonFinal
    @Value("${jwt.accessToken}")
    protected String SIGN_KEY;
    @NonFinal
    @Value("${jwt.expiryTime}")
    protected int TOKEN_EXPIRY_TIME;
    @NonFinal
    @Value("${jwt.expiryTimeRefreshToken}")
    protected int TOKEN_REFRESH_EXPIRY_TIME;
    public String generateTempPwd(int length) {
        String numbers = "012345678";
        char otp[] = new char[length];
        Random getOtpNum = new Random();
        for (int i = 0; i < length; i++) {
            otp[i] = numbers.charAt(getOtpNum.nextInt(numbers.length()));
        }
        String optCode = "";
        for (int i = 0; i < otp.length; i++) {
            optCode += otp[i];
        }
        return optCode;
    }

    //build scope for user
//    public String buildScopeUser(Patients patients){
//        StringJoiner stringJoiner = new StringJoiner(" ");//phân cách bằng dấu cách
//        if(!CollectionUtils.isEmpty(patients.getRole())){
//            patients.getRoles().forEach(stringJoiner::add);
//        }
//
//        return stringJoiner.toString();
//    }

    //generate token cho user
    public String generateTokenPatient(
            Patients patients,
            int expireDay,
            String secretKey,
            @Nullable Date expireTime
    ) {
        Date expirationTimeVar = expireTime == null ? new Date(
                Instant.now().plus(expireDay, ChronoUnit.SECONDS).toEpochMilli()
        ) : expireTime;
        JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new  JWTClaimsSet.Builder()
                .subject(patients.getEmail())
                .issuer("telemedicine.com")
                .issueTime(new Date())
                .expirationTime(expirationTimeVar)
                .claim("scope", Role.USER.toString())
                .claim("name",patients.getFullName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwtHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //generate token for staff
    public String generateTokenStaff(
            MedicalStaff staff,
            int expireDay,
            String secretKey,
            @Nullable Date expireTime
    ) {
        Date expirationTimeVar = expireTime == null ? new Date(
                Instant.now().plus(expireDay, ChronoUnit.SECONDS).toEpochMilli()
        ) : expireTime;
        JWSHeader jwtHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet claimsSet = new  JWTClaimsSet.Builder()
                .subject(staff.getEmail())
                .issuer("telemedicine.com")
                .issueTime(new Date())
                .expirationTime(expirationTimeVar)
                .claim("scope",buildScopeStaff(staff))
                .claim("name",staff.getFullName())
                .build();
        Payload payload = new Payload(claimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwtHeader, payload);

        try {
            jwsObject.sign(new MACSigner(secretKey));
            return jwsObject.serialize();
        }catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    //build scope for staff
    public String buildScopeStaff(MedicalStaff staff){
        StringJoiner stringJoiner = new StringJoiner(" ");//phân cách bằng dấu cách
        if(!CollectionUtils.isEmpty(staff.getRoles())){
            staff.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }
    public SignedJWT verifyToken(String token, String secretKey) throws ParseException, JOSEException {
        var verifier = new MACVerifier(secretKey.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);

        var verify = signedJWT.verify(verifier);

        var expireTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if(!verify) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Unauthenticated", "auth-e-01");
//            throw new AppException(ErrorEnum.UNAUTHENTICATED);
        }
        if(!expireTime.after(new Date())) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Token expired", "auth-e-08");
//            throw new AppException(ErrorEnum.TOKEN_EXPIRE);
        }
        return signedJWT;

    }

}
