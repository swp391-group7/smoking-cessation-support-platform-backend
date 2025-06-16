package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.UUID;

import static org.testng.Assert.*;

public class JWTServiceTest {

    private JWTService jwtService;

    // Giá trị giống config của bạn
    private final String secretKey = "Lh7K!v9@83ksn238JHJh23hjshJH23#asd";
    private final long expirationTime = 3_600_000L; // 1 giờ

    @BeforeMethod
    public void setUp() {
        jwtService = new JWTService();
        // Inject giá trị vào private fields
        ReflectionTestUtils.setField(jwtService, "secretKey", secretKey);
        ReflectionTestUtils.setField(jwtService, "expirationTime", expirationTime);
    }

    @Test
    public void testGenerateAndValidateToken() {
        UUID userId = UUID.randomUUID();

        // Generate token
        String token = jwtService.generateToken(userId);
        assertNotNull(token, "Token không được null");
        assertTrue(jwtService.validateToken(token), "Token vừa tạo phải hợp lệ");
    }

    @Test
    public void testExtractId() {
        UUID userId = UUID.randomUUID();
        String token = jwtService.generateToken(userId);

        // Extract lại ID từ token
        UUID extracted = jwtService.extractId(token);
        assertEquals(extracted, userId, "ID trích ra phải đúng với ID gốc");
    }

    @Test
    public void testValidateInvalidToken() {
        // Với token sai định dạng hoặc signature không khớp, validateToken phải trả về false
        assertFalse(jwtService.validateToken("this.is.an.invalid.token"));
    }

    @Test(expectedExceptions = JwtException.class)
    public void testExtractIdInvalidToken() {
        // Với token không parse được, extractId sẽ ném JwtException
        jwtService.extractId("invalid.token");
    }
}
