package com.Swp_391_gr7.smoking_cessation_support_platform_backend.services.jwt;

import io.jsonwebtoken.JwtException;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.annotations.*;

import java.util.UUID;

import static org.testng.Assert.*;

@SpringBootTest
public class JWTServiceTests extends AbstractTestNGSpringContextTests {



    private JWTService jwtService;

    private final String secretKey = "Lh7K!v9@83ksn238JHJh23hjshJH23#asd";
    private final long expirationTime = 3_600_000L; // 1 giờ

    @BeforeSuite
    public void beforeSuite() {
        // Thiết lập môi trường hoặc cấu hình cần thiết trước khi chạy các test
        System.out.println("Bắt đầu chạy các test cho cả suite.");
    }
    @BeforeTest
    public void beforeTest() {
        // Thiết lập môi trường hoặc cấu hình cần thiết trước khi chạy các test trong test suite
        System.out.println("Bắt đầu chạy các test cho test name All.");
    }
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