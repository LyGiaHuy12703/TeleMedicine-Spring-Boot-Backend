package org.telemedicine.server.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.telemedicine.server.dto.auth.*;
import org.telemedicine.server.dto.patients.PatientCreationRequest;
import org.telemedicine.server.entity.Token;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.entity.TokenRefresh;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.PatientMapper;
import org.telemedicine.server.repository.RefreshTokenRepository;
import org.telemedicine.server.repository.TokenRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;

import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    PatientRepository patientRepository;
    PatientMapper patientMapper;

    PasswordEncoder passwordEncoder;
    MedicalStaffRepository medicalStaffRepository;

    MailService mailService;

    TokenRepository tokenRepository;
    private final org.telemedicine.server.utils.helper helper;
    RefreshTokenRepository refreshTokenRepository;

    @NonFinal
    @Value("${jwt.accessToken}")
    protected String ACCESS_TOKEN_SECRET;
    @NonFinal
    @Value("${jwt.refreshToken}")
    protected String REFRESH_TOKEN_SECRET;
    @NonFinal
    @Value("${jwt.expiryTime}")
    protected int TOKEN_EXPIRY_TIME;
    @NonFinal
    @Value("${jwt.expiryTimeRefreshToken}")
    protected int TOKEN_REFRESH_EXPIRY_TIME;
    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            verifyTokenPrivate(token, false);
        } catch (AppException e) {
            isValid = false;
        }

        return IntrospectResponse.builder().valid(isValid).build();
    }
    private SignedJWT verifyTokenPrivate(String token, boolean isRefersh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(ACCESS_TOKEN_SECRET.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        //lấy thời gian hết hạn
        Date expityTime = (isRefersh)
                ?new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                .toInstant().plus(TOKEN_REFRESH_EXPIRY_TIME, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();

        //trả về true or false token hết hạn
        boolean verified = signedJWT.verify(verifier);
        if(!(verified && expityTime.after(new Date()))){
            throw new AppException(HttpStatus.BAD_REQUEST, "Unauthenticated", "auth-e-03");
        }

        if(tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Unauthenticated", "auth-e-03");

        return signedJWT;
    }
    // dang ky cho user
    public void signUp(PatientCreationRequest request) throws MessagingException {
        //tim email tồn tại không cho đăng ký
        if(patientRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");

        Patients patient = patientMapper.toPatient(request);

        patient.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();

        roles.add(Role.USER.toString());

        patient.setRoles(roles);

        patientRepository.save(patient);
        //tạo token xác thực
        String verifyToken = helper.generateTempPwd(32);

        Token token = new Token();
        token.setToken(verifyToken);
        token.setPatients(patient);
        tokenRepository.save(token);

        //gửi mail xác nhận
        String confirmationUrl = "http://localhost:8080/api/auth/verify/" + verifyToken;
        String message = "<p>Xin chào,</p>"
                + "<p>Vui lòng nhấn vào liên kết dưới đây để xác thực địa chỉ email của bạn:</p>"
                + "<p><a href=\"" + confirmationUrl + "\">Xác thực tài khoản</a></p>";
        mailService.sendVerificationMail(patient.getEmail(), message);
    }
    //verify email user
    public boolean verifyUser(String token) {
        Token verificationToken = tokenRepository.findByToken(token);

        if (verificationToken == null) {
            return false;
        }

        Patients patients = verificationToken.getPatients();
        patients.setVerified(true);
        patientRepository.save(patients);
        tokenRepository.delete(verificationToken); // Xóa token sau khi xác nhận

        return true;
    }
    //changePassword
    public void changePassword(PasswordChangeRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();

        Patients patients = patientRepository.findByEmail(email).orElse(null);

        if(patients == null){
            throw new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03");
        }

        boolean verified = passwordEncoder.matches(request.getPassword(), patients.getPassword());

        if(!verified){
            throw new AppException(HttpStatus.BAD_REQUEST, "Password invalid", "auth-e-02");
        }
        patients.setPassword(passwordEncoder.encode(request.getNewPassword()));
        patientRepository.save(patients);
    }
    //forgot password
    public void forgotPassword(ForgotPasswordRequest request){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if(patients == null){
            throw new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03");
        }


    }

    //authentication user
    public AuthResponse loginUser(AuthRequest request){
        Patients patient = patientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), patient.getPassword());

        if(!authenticated || !patient.isVerified()){
            throw new AppException(HttpStatus.BAD_REQUEST, "UnAuthenticated", "auth-e-04");
        }

        var accessToken = helper.generateTokenPatient(patient, TOKEN_EXPIRY_TIME, ACCESS_TOKEN_SECRET, null);
        var refreshToken = helper.generateTokenPatient(patient, TOKEN_REFRESH_EXPIRY_TIME, REFRESH_TOKEN_SECRET, null);

        Token token = tokenRepository.findByPatientsId(patient.getId());
        if(token == null){
            tokenRepository.save(Token.builder()
                    .patients(patient)
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .createAt(new Date())
                    .build()
            );
        }else{
            token.setToken(accessToken);
            token.setRefreshToken(refreshToken);
            tokenRepository.save(token);
        }
        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(helper.buildScopeUser(patient))
                .build();
    }
    //authentication staff
    public AuthResponse loginStaff(AuthRequest request){
        var staff = medicalStaffRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Staff not found", "auth-e-03"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), staff.getPassword());

        if(!authenticated){
            throw new AppException(HttpStatus.BAD_REQUEST, "UnAuthenticated", "auth-e-04");
        }

        var accessToken = helper.generateTokenStaff(staff, TOKEN_EXPIRY_TIME, ACCESS_TOKEN_SECRET, null);
        var refreshToken = helper.generateTokenStaff(staff, TOKEN_REFRESH_EXPIRY_TIME, REFRESH_TOKEN_SECRET, null);

        TokenRefresh tokenRefresh = refreshTokenRepository.findByStaffId(staff.getId());
        if(tokenRefresh == null){
            refreshTokenRepository.save(TokenRefresh.builder()
                    .staff(staff)
                    .token(accessToken)
                    .refreshToken(refreshToken)
                    .createAt(new Date())
                    .build()
            );
        }else {
            tokenRefresh.setRefreshToken(refreshToken);
            tokenRefresh.setToken(accessToken);
            refreshTokenRepository.save(tokenRefresh);
        }

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .role(helper.buildScopeStaff(staff))
                .build();
    }
    @Transactional
    public void logout(){
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Patients patients = patientRepository.findByEmail(email).orElse(null);
        if(patients == null){
            MedicalStaff staff = medicalStaffRepository.findByEmail(email).orElse(null);
            if(staff == null){
                throw new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03");
            }else {
                refreshTokenRepository.deleteRefreshTokenByStaffId(staff.getId());
            }
        }else {
            tokenRepository.deleteByPatientsId(patients.getId());
        }
    }
    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        if (request == null || request.getRefreshToken() == null) {
            throw new AppException(HttpStatus.BAD_REQUEST, "Refresh token is required", "auth-e-01");
        }
        String refreshToken = request.getRefreshToken();
        var signJWT = helper.verifyToken(refreshToken, REFRESH_TOKEN_SECRET);

        if (signJWT == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Invalid refresh token", "auth-e-02");
        }

        var role = signJWT.getJWTClaimsSet().getClaim("scope");
        if (role == null) {
            throw new AppException(HttpStatus.UNAUTHORIZED, "Role not found in token", "auth-e-04");
        }

        if ("USER".equals(role)) {
            Token tokenEntity = tokenRepository.findByRefreshToken(refreshToken);
            if (tokenEntity == null) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Unauthenticated", "auth-e-01");
            }

            Patients patients = patientRepository.findById(tokenEntity.getPatients().getId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03"));

            Date expireTimeOfRefreshToken = signJWT.getJWTClaimsSet().getExpirationTime();
            String newToken = helper.generateTokenPatient(patients, TOKEN_EXPIRY_TIME, ACCESS_TOKEN_SECRET, null);
            String newRefreshToken = helper.generateTokenPatient(patients, TOKEN_REFRESH_EXPIRY_TIME, REFRESH_TOKEN_SECRET, expireTimeOfRefreshToken);

            tokenEntity.setToken(newToken);
            tokenEntity.setRefreshToken(newRefreshToken);
            tokenRepository.save(tokenEntity);
            return AuthResponse.builder()
                    .accessToken(newToken)
                    .refreshToken(newRefreshToken)
                    .build();
        } else {
            TokenRefresh tokenRefreshEntity = refreshTokenRepository.findByRefreshToken(refreshToken);
            if (tokenRefreshEntity == null) {
                throw new AppException(HttpStatus.BAD_REQUEST, "Unauthenticated", "auth-e-03");
            }

            MedicalStaff staff = medicalStaffRepository.findById(tokenRefreshEntity.getStaff().getId())
                    .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "User not found", "auth-e-03"));

            Date expireTimeOfRefreshToken = signJWT.getJWTClaimsSet().getExpirationTime();
            String newToken = helper.generateTokenStaff(staff, TOKEN_EXPIRY_TIME, ACCESS_TOKEN_SECRET, null);
            String newRefreshToken = helper.generateTokenStaff(staff, TOKEN_REFRESH_EXPIRY_TIME, REFRESH_TOKEN_SECRET, expireTimeOfRefreshToken);

            tokenRefreshEntity.setToken(newToken);
            tokenRefreshEntity.setRefreshToken(newRefreshToken);
            refreshTokenRepository.save(tokenRefreshEntity);
            return AuthResponse.builder()
                    .accessToken(newToken)
                    .refreshToken(newRefreshToken)
                    .build();
        }
    }


}