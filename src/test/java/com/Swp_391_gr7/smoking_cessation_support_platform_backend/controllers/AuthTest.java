//package com.Swp_391_gr7.smoking_cessation_support_platform_backend.controllers;
//
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.LoginRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.dto.auth.SigninRequest;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.models.entity.User;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.repositories.UserRepository;
//import com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.jwt.JWTService;
//import org.mockito.*;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.dao.DataIntegrityViolationException;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
//import org.testng.annotations.BeforeMethod;
//import org.testng.annotations.Test;
//
//import static org.mockito.Mockito.when;
//import static org.mockito.Mockito.any;
//import static org.mockito.Mockito.verify;
//import static org.testng.Assert.*;
//
//import java.util.*;
//
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//public class AuthTest  {
//
//    private AuthController authController;
//    private UserRepository userRepository;
//    private JWTService jwtService;
//    private PasswordEncoder passwordEncoder;
//
//    @BeforeMethod
//    public void setup() {
//        userRepository  = Mockito.mock(UserRepository.class);
//        jwtService      = Mockito.mock(JWTService.class);
//        passwordEncoder = Mockito.mock(PasswordEncoder.class);
//        // Khởi tạo controller *bằng tay* với các mock
//        authController  = new AuthController(userRepository, jwtService, passwordEncoder);
//    }
//
//    @Test
//    public void testSignupSuccess() {
//        // Arrange
//        SigninRequest req = new SigninRequest();
//        req.setUsername("john");
//        req.setPassword("rawPass");
//        req.setFullName("John Doe");
//        req.setEmail("john@example.com");
//
//        when(passwordEncoder.encode("rawPass")).thenReturn("hashedPass");
//
//        UUID userId = UUID.randomUUID();
//        // Bắt đúng save(User) và gán ID
//        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
//            User u = invocation.getArgument(0);
//            u.setId(userId);
//            return u;
//        });
//
//        when(jwtService.generateToken(userId)).thenReturn("jwt-token");
//
//        // Act
//        ResponseEntity<Map<String, String>> resp = authController.signup(req);
//
//        // Assert
//        assertEquals(resp.getStatusCodeValue(), 200);
//        assertEquals(resp.getBody().get("token"), "jwt-token");
//
//        verify(passwordEncoder).encode("rawPass");
//        verify(userRepository).save(any(User.class));
//        verify(jwtService).generateToken(userId);
//    }
//
//    @Test
//    public void testSignupUsernameExists() {
//        // Arrange
//        SigninRequest req = new SigninRequest();
//        req.setUsername("taken");
//        req.setPassword("p");
//        req.setFullName("X");
//        req.setEmail("x@x");
//
//        when(passwordEncoder.encode(anyString())).thenReturn("h");
//        doThrow(new DataIntegrityViolationException("dup"))
//                .when(userRepository).save(any(User.class));
//
//        // Act
//        ResponseEntity<Map<String, String>> resp = authController.signup(req);
//
//        // Assert
//        assertEquals(resp.getStatusCodeValue(), 400);
//        assertEquals(resp.getBody().get("error"), "Username already existed");
//
//        verify(userRepository).save(any(User.class));
//        verify(jwtService, never()).generateToken(any());
//    }
//
//    @Test
//    public void testLoginSuccess() {
//        LoginRequest req = new LoginRequest();
//        req.setUsername("john");
//        req.setPassword("raw");
//
//        UUID userId = UUID.randomUUID();
//        User user = User.builder()
//                .id(userId)
//                .username("john")
//                .password("hashed")
//                .fullName("John Doe")
//                .avtarPath("/img.png")
//                .build();
//
//        when(userRepository.findByUsernameContainsIgnoreCase("john"))
//                .thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("raw", "hashed")).thenReturn(true);
//        when(jwtService.generateToken(userId)).thenReturn("jwt123");
//
//        ResponseEntity<Map<String, Object>> resp = authController.login(req);
//
//        assertEquals(resp.getStatusCodeValue(), 200);
//        assertEquals(resp.getBody().get("token"), "jwt123");
//
//        @SuppressWarnings("unchecked")
//        Map<String, Object> userPayload = (Map<String, Object>) resp.getBody().get("user");
//        assertEquals(userPayload.get("id"), userId);
//        assertEquals(userPayload.get("full_name"), "John Doe");
//        assertEquals(userPayload.get("avatar_path"), "/img.png");
//
//        verify(userRepository).findByUsernameContainsIgnoreCase("john");
//        verify(passwordEncoder).matches("raw", "hashed");
//        verify(jwtService).generateToken(userId);
//    }
//
//    @Test
//    public void testLoginUserNotFound() {
//        LoginRequest req = new LoginRequest();
//        req.setUsername("missing");
//        req.setPassword("x");
//
//        when(userRepository.findByUsernameContainsIgnoreCase("missing"))
//                .thenReturn(Optional.empty());
//
//        ResponseEntity<Map<String, Object>> resp = authController.login(req);
//
//        assertEquals(resp.getStatusCodeValue(), 401);
//        assertEquals(resp.getBody().get("error"), "Invalid Username or Password");
//
//        verify(passwordEncoder, never()).matches(any(), any());
//        verify(jwtService, never()).generateToken(any());
//    }
//
//    @Test
//    public void testLoginWrongPassword() {
//        LoginRequest req = new LoginRequest();
//        req.setUsername("john");
//        req.setPassword("bad");
//
//        UUID userId = UUID.randomUUID();
//        User user = User.builder()
//                .id(userId)
//                .username("john")
//                .password("hashed")
//                .build();
//
//        when(userRepository.findByUsernameContainsIgnoreCase("john"))
//                .thenReturn(Optional.of(user));
//        when(passwordEncoder.matches("bad", "hashed")).thenReturn(false);
//
//        ResponseEntity<Map<String, Object>> resp = authController.login(req);
//
//        assertEquals(resp.getStatusCodeValue(), 401);
//        assertEquals(resp.getBody().get("error"), "Invalid Username or Password");
//
//        verify(jwtService, never()).generateToken(any());
//    }
//// test testNG
//}
//
