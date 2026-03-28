package com.example.matcher.userservice.controllers;

import com.example.matcher.userservice.aspect.AspectAnnotation;
import com.example.matcher.userservice.exception.ResourceNotFoundException;
import com.example.matcher.userservice.model.JwtAuthenticationResponse;
import com.example.matcher.userservice.model.RefreshToken;
import com.example.matcher.userservice.model.User;
import com.example.matcher.userservice.repository.RefreshTokenRepository;
import com.example.matcher.userservice.service.AuthenticationService;
import com.example.matcher.userservice.service.JwtService;
import com.example.matcher.userservice.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/UserService/auth")
public class AuthController {

    private AuthenticationService authenticationService;
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Operation(summary = "Обновить токен доступа",
            description = "Метод обновляет токен доступа и возвращает новый набор токенов")
    @ApiResponse(responseCode = "200", description = "Успешный ответ")
    @ApiResponse(responseCode = "401", description = "Токен не валиден", content = @Content())
    @PostMapping("/updateRefreshToken")
    public ResponseEntity<JwtAuthenticationResponse> updateAccessAndRefreshToken(@RequestParam("refreshToken") String refreshToken) {
        return new ResponseEntity<>(jwtService.updateAccessAndRefreshToken(refreshToken), HttpStatus.OK);
    }

    @GetMapping("/testInfo")
    public String testInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getPrincipal().toString();
    }

    @Operation(summary = "Ввод емайла при логине и регистрации для получения кода",
            description = "Метод отправляет код подтверждения на указанный емайл при регистрации и логине!")
    @ApiResponse(responseCode = "200", description = "Успешный ответ", content = @Content())
    @ApiResponse(responseCode = "409", description = "Пользователь с такой почтой уже зарегистрирован", content = @Content())
    @AspectAnnotation
    @PostMapping("/email/sendToken")
    public ResponseEntity<String> sendEmailRegistrationToken(@RequestParam("email") String email) {
        authenticationService.sendAuthTokenEmail(email);
        return new ResponseEntity<>("Check email", HttpStatus.OK);
    }

    @Operation(summary = "Ввод кода отправленного на почту",
            description = "Метод сверяет код отправленный на почту и введённый, после чего возвращает набор токенов!")
    @ApiResponse(responseCode = "200", description = "Успешный ответ")
    @ApiResponse(responseCode = "401", description = "Токен не валиден", content = @Content())
    @PostMapping("/email/confirmationEmail")
    public ResponseEntity<JwtAuthenticationResponse> confirmationEmail(@RequestParam("email") String email, @RequestParam("token") String token) {
        return new ResponseEntity<>(authenticationService.confirmationEmail(email, token), HttpStatus.OK);
    }

//    @GetMapping("/telegram/authorize")
//    public ResponseEntity<JwtAuthenticationResponse> telegramAuthLink(@RequestParam("token") String token){
//        return new ResponseEntity<>(authenticationService.confirmationTelegramAuthToken(token), HttpStatus.OK);
//    }

}
