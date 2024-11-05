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
import org.telemedicine.server.entity.*;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.mapper.PatientMapper;
import org.telemedicine.server.repository.*;

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
    private final MedicalRecordBookRepository medicalRecordBookRepository;

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
    // dang ky cho user
    public void register(PatientCreationRequest request) throws MessagingException {
        //tim email tồn tại không cho đăng ký
        if(patientRepository.existsByEmail(request.getEmail()))
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
    }
    //verify email user
    public AuthResponse verifyRegister(PatientCreationRequest request) {
        // Find user if not existed
        boolean existedUser = patientRepository.existsByEmail(request.getEmail());
        if(existedUser){
            throw new AppException(HttpStatus.BAD_REQUEST, "Email has existed", "auth-e-01");
        }
        // Hash password
        String hashedPassword = passwordEncoder.encode(request.getPassword());
        request.setPassword(hashedPassword);

        // Roles for normal user
        Role role = Role.USER;
        Patients patients = Patients.builder()
                .email(request.getEmail())
                .fullName(request.getFullName())
                .password(request.getPassword())
                .build();
        patients.setRole(role);
        patientRepository.save(patients);

        MedicalRecordBook medicalRecordBook = MedicalRecordBook.builder()
                .patients(patients)
                .build();
        medicalRecordBookRepository.save(medicalRecordBook);

        // Generate a pair of token
        String accessToken = helper.generateTokenPatient(patients,TOKEN_EXPIRY_TIME,ACCESS_TOKEN_SECRET,null);
        String refreshToken = helper.generateTokenPatient(patients,TOKEN_REFRESH_EXPIRY_TIME,REFRESH_TOKEN_SECRET,null);

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
            throw new AppException(HttpStatus.BAD_REQUEST, "Mật khẩu không đúng", "auth-e-02");
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
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Login fail", "auth-e-03"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), patient.getPassword());

        if(!authenticated){
            throw new AppException(HttpStatus.BAD_REQUEST, "Login fail", "auth-e-04");
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
                .role(Role.USER.toString())
                .build();
    }
    //authentication staff
    public AuthResponse loginStaff(AuthRequest request){
        var staff = medicalStaffRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(HttpStatus.NOT_FOUND, "Login fail", "auth-e-03"));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), staff.getPassword());

        if(!authenticated){
            throw new AppException(HttpStatus.BAD_REQUEST, "Login fail", "auth-e-04");
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