package com.dongVu1105.identity_service.service;

import com.dongVu1105.identity_service.dto.request.AuthenticationRequest;
import com.dongVu1105.identity_service.dto.request.IntrospectRequest;
import com.dongVu1105.identity_service.dto.request.LogoutRequest;
import com.dongVu1105.identity_service.dto.request.RefreshTokenRequest;
import com.dongVu1105.identity_service.dto.response.AuthenticationResponse;
import com.dongVu1105.identity_service.dto.response.IntrospectResponse;
import com.dongVu1105.identity_service.entity.InvalidatedToken;
import com.dongVu1105.identity_service.entity.User;
import com.dongVu1105.identity_service.exception.AppException;
import com.dongVu1105.identity_service.exception.ErrorCode;
import com.dongVu1105.identity_service.repository.InvalidatedTokenRepository;
import com.dongVu1105.identity_service.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    @NonFinal
    @Value("${jwt.signerKey}")
    private String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.access-token-duration}")
    private long ACCESS_TOKEN_DURATION;

    @NonFinal
    @Value("${jwt.refresh-token-duration}")
    private long REFRESH_TOKEN_DURATION;

    InvalidatedTokenRepository invalidatedTokenRepository;
    PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public IntrospectResponse introspect (IntrospectRequest request){
        boolean valid = true;
        try{
            verifyToken(request.getAccessToken(), false);
        } catch (Exception e){
            valid = false;
        }
        return IntrospectResponse.builder().valid(valid).build();
    }

    public AuthenticationResponse authenticate (AuthenticationRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(!user.isStatusAccount()){
            throw new AppException(ErrorCode.UNABLE_ACCOUNT);
        }
        if(!passwordEncoder.matches(request.getPassword(), user.getPassword())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse.builder()
                .valid(true)
                .accessToken(generateAccessToken(user))
                .refreshToken(generateRefreshToken(user))
                .build();
    }

    public AuthenticationResponse refreshToken (RefreshTokenRequest request) throws ParseException, JOSEException {
        SignedJWT refreshToken = verifyToken(request.getRefreshToken(), true);
        invalidatedTokenRepository.save(
                InvalidatedToken.builder()
                        .id(refreshToken.getJWTClaimsSet().getJWTID())
                        .expiryTime(refreshToken.getJWTClaimsSet().getExpirationTime())
                        .build());
        User user = userRepository.findById(refreshToken.getJWTClaimsSet().getSubject())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return AuthenticationResponse.builder()
                .valid(true)
                .refreshToken(generateRefreshToken(user))
                .accessToken(generateAccessToken(user))
                .build();
    }

    public void logout (LogoutRequest request){
        try {
            SignedJWT accessToken = verifyToken(request.getAccessToken(), false);
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(accessToken.getJWTClaimsSet().getJWTID())
                    .expiryTime(accessToken.getJWTClaimsSet().getExpirationTime())
                    .build());
        } catch (Exception e) {
            log.info("access token already expired");
        }

        try {
            SignedJWT refreshToken = verifyToken(request.getRefreshToken(), true);
            invalidatedTokenRepository.save(InvalidatedToken.builder()
                    .id(refreshToken.getJWTClaimsSet().getJWTID())
                    .expiryTime(refreshToken.getJWTClaimsSet().getExpirationTime())
                    .build());
        } catch (Exception e) {
            log.info("refresh token already expired");
        }
    }

    private String generateAccessToken (User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("team16.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(ACCESS_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .subject(user.getId())
                .jwtID(UUID.randomUUID().toString())
                .claim("token-type", "access")
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private String generateRefreshToken (User user){
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS512);
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .issuer("team16.com")
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(REFRESH_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .subject(user.getId())
                .jwtID(UUID.randomUUID().toString())
                .claim("token-type", "refresh")
                .claim("scope", buildScope(user))
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            throw new AppException(ErrorCode.CAN_NOT_CREATE_TOKEN);
        }
    }

    private SignedJWT verifyToken (String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());
        SignedJWT signedJWT = SignedJWT.parse(token);
        String tokenType = (String) signedJWT.getJWTClaimsSet().getClaim("token-type");
        if ((isRefresh && !tokenType.equals("refresh")) || (!isRefresh && !tokenType.equals("access"))){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        boolean verified = signedJWT.verify(verifier);
        if(!(expiryTime.after(new Date()) && verified)){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        if(invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())){
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return signedJWT;
    }

    private String buildScope (User user){
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRole())){
            user.getRole().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getName());
            });
        }
        return stringJoiner.toString();
    }
}
