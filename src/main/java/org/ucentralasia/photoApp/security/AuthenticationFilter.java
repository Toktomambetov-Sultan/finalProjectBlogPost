package org.ucentralasia.photoApp.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.ucentralasia.photoApp.SpringAppContext;
import org.ucentralasia.photoApp.service.UserService;
import org.ucentralasia.photoApp.shared.dto.UserDto;
import org.ucentralasia.photoApp.ui.model.request.UserLoginRequestModel;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

public class AuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            UserLoginRequestModel credentials =
                    new ObjectMapper().readValue(request.getInputStream(), UserLoginRequestModel.class);

            return getAuthenticationManager().authenticate(
                    new UsernamePasswordAuthenticationToken(credentials.getEmail(), credentials.getPassword(), new ArrayList<>()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult)
            throws IOException, ServletException {
        byte[] secretKeyBytes = Base64.getEncoder().encode(SecurityConstants.getTokenSecret().getBytes());
        SecretKey secretKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
        Instant now = Instant.now();

        String userName = ((User) authResult.getPrincipal()).getUsername();

        String token = Jwts.builder()
                .subject(userName)
                .expiration(Date.from(now.plusMillis(SecurityConstants.EXPIRATION_TIME)))
                .issuedAt(Date.from(now)).signWith(secretKey, SignatureAlgorithm.HS512).compact();

        response.addHeader(SecurityConstants.HEADER_STRING, SecurityConstants.TOKEN_PREFIX + token);

        UserService userService = (UserService) SpringAppContext.getBean("userServiceImpl");
        UserDto userDto = userService.getUser(userName);
        response.addHeader("UserId", userDto.getUserId());
    }
}