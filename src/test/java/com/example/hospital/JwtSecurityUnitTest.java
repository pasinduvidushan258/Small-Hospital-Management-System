package com.example.hospital;

import com.example.hospital.model.Role;
import com.example.hospital.security.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
class JwtSecurityUnitTest {

    @Autowired
    private JwtService jwtService;

    @Test
    void testTokenGenerationAndValidation() {
        String token = jwtService.generateToken("testuser", Role.PATIENT.name());
        assertNotNull(token);

        assertEquals("testuser", jwtService.extractUsername(token));
        assertEquals("PATIENT", jwtService.extractRole(token));
    }

    @Test
    void testTamperedTokenFailsValidation() {
        String token = jwtService.generateToken("PS/2022/001", Role.PATIENT.name());
        assertNotNull(token);

        // Tamper with the token by modifying the payload part (between the two dots)
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length);

        // Decode payload
        String payload = new String(java.util.Base64.getUrlDecoder().decode(parts[1]));
        assertTrue(payload.contains("PATIENT"));

        // Replace PATIENT with ADMIN in the payload
        String tamperedPayload = payload.replace("PATIENT", "ADMIN");
        String encodedTamperedPayload = java.util.Base64.getUrlEncoder().withoutPadding()
                .encodeToString(tamperedPayload.getBytes());

        // Reassemble tampered token with original signature
        String tamperedToken = parts[0] + "." + encodedTamperedPayload + "." + parts[2];

        // Attempting to extract username or validate must throw SignatureException /
        // fail
        assertThrows(io.jsonwebtoken.security.SignatureException.class, () -> {
            jwtService.extractUsername(tamperedToken);
        });
    }
}