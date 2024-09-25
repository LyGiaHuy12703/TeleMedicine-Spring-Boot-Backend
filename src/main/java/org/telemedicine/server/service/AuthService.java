package org.telemedicine.server.service;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.telemedicine.server.dto.request.*;
import org.telemedicine.server.dto.response.AuthResponse;
import org.telemedicine.server.dto.response.IntrospectResponse;
import org.telemedicine.server.dto.response.PatientResponse;
import org.telemedicine.server.entity.Token;
import org.telemedicine.server.entity.MedicalStaff;
import org.telemedicine.server.entity.Patients;
import org.telemedicine.server.enums.Role;
import org.telemedicine.server.exception.AppException;
import org.telemedicine.server.exception.ErrorCode;
import org.telemedicine.server.mapper.PatientMapper;
import org.telemedicine.server.repository.TokenRepository;
import org.telemedicine.server.repository.MedicalStaffRepository;
import org.telemedicine.server.repository.PatientRepository;
import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;


@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthService {
    PatientRepository patientRepository;
    PatientMapper patientMapper;

    PasswordEncoder passwordEncoder;
    MedicalStaffRepository medicalStaffRepository;

    TokenRepository tokenRepository;

    @NonFinal
    @Value("${jwt.accessToken}")
    protected String SIGN_KEY;
    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;
    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public PatientResponse signUp(PatientCreationRequest request){
        //tim email tồn tại không cho đăng ký
        if(patientRepository.existsByEmail(request.getEmail()))
            throw new AppException(ErrorCode.USER_EXISTED);

        Patients patient = patientMapper.toPatient(request);

//        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        patient.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<String> roles = new HashSet<>();

        roles.add(Role.USER.toString());

        patient.setRoles(roles);

        return patientMapper.toPatientResponse(patientRepository.save(patient));
    }

    //authentication user
    public AuthResponse authenticateUser(AuthRequest request){
        var patient = patientRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), patient.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateTokenUser(patient);
        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    //authentication staff
    public AuthResponse authenticateStaff(AuthRequest request){
        var staff = medicalStaffRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.STAFF_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), staff.getPassword());

        if(!authenticated){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        var token = generateTokenStaff(staff);
        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }
    //generate token cho user
    private String generateTokenUser(Patients patients){
        //nội dung thuật toán được sử dụng HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //nội dung thiết yếu
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(patients.getEmail())
                .issuer("telemedicine")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()//token hết hạn sau 1h
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScopeUser(patients))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        //thuật toán ký
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }
    //build scope for user
    private String buildScopeUser(Patients patients){
        StringJoiner stringJoiner = new StringJoiner(" ");//phân cách bằng dấu cách
        if(!CollectionUtils.isEmpty(patients.getRoles())){
            patients.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }

    //generate token for staff
    private String generateTokenStaff(MedicalStaff staff){
        //nội dung thuật toán được sử dụng HS512
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        //nội dung thiết yếu
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(staff.getEmail())
                .issuer("telemedicine")
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()//token hết hạn sau 1h
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScopeStaff(staff))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        //thuật toán ký
        try {
            jwsObject.sign(new MACSigner(SIGN_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("cannot create token", e);
            throw new RuntimeException(e);
        }
    }

    //build scope for staff
    private String buildScopeStaff(MedicalStaff staff){
        StringJoiner stringJoiner = new StringJoiner(" ");//phân cách bằng dấu cách
        if(!CollectionUtils.isEmpty(staff.getRoles())){
            staff.getRoles().forEach(stringJoiner::add);
        }

        return stringJoiner.toString();
    }

    public IntrospectResponse introspect(IntrospectRequest request) throws JOSEException, ParseException {
        var token = request.getToken();

        boolean isValid = true;

        try {
            verifyToken(token, false);
        }catch (AppException e){
            isValid = false;
        }
        //thời gian hết hạn sau thời gian hiện tại
        return IntrospectResponse.builder()
                .valid(isValid)
                .build();
    }

    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);
            String jit = signToken.getJWTClaimsSet().getJWTID();

            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            Token token = Token.builder()
                    .id(jit)
                    .expiryTime(expiryTime)
                    .build();

            tokenRepository.save(token);
        }catch (AppException e){
            log.info("Token already expired");
        }
    }

    private SignedJWT verifyToken(String token, boolean isRefersh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGN_KEY.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        //lấy thời gian hết hạn
        Date expityTime = (isRefersh)
                ?new Date(signedJWT.getJWTClaimsSet().getIssueTime()
                    .toInstant().plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                :signedJWT.getJWTClaimsSet().getExpirationTime();

        //trả về true or false token hết hạn
        boolean verified = signedJWT.verify(verifier);
        if(!(verified && expityTime.after(new Date()))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        if(tokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }
    public AuthResponse refreshToken(RefreshRequest request) throws ParseException, JOSEException {
        //Kiểm tra hiệu lực token
        var signJWT = verifyToken(request.getToken(), true);

        var jit = signJWT.getJWTClaimsSet().getJWTID();

        var expiryTime = signJWT.getJWTClaimsSet().getExpirationTime();

        Token tokenInvalid = Token.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        tokenRepository.save(tokenInvalid);

        var email = signJWT.getJWTClaimsSet().getSubject();

        var user = patientRepository.findByEmail(email).orElse(null);

        if(user == null){
            var staff = medicalStaffRepository.findByEmail(email).orElseThrow(() -> new AppException(ErrorCode.UNAUTHENTICATED));

            var token = generateTokenStaff(staff);
            return AuthResponse.builder()
                    .token(token)
                    .authenticated(true)
                    .build();
        }

        var token = generateTokenUser(user);
        return AuthResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

}

//    //staff signin
//    public AuthResponse staffAuth(AuthRequest request){
//        var staff = medicalStaffRepository.findByEmail(request.getEmail())
//                .orElseThrow(() -> new AppException(ErrorCode.STAFF_EXISTED));
//
////        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
//        boolean authenticated = passwordEncoder.matches(request.getPassword(), staff.getPassword());
//
//        if(!authenticated){
//            throw new AppException(ErrorCode.UNAUTHENTICATED);
//        }
//
//        var token = generateTokenStaff(staff);
//        return AuthResponse.builder()
//                .token(token)
//                .authenticated(true)
//                .build();
//    }