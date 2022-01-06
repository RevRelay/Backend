package com.revature.RevRelay.utils;

import com.revature.RevRelay.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class JwtUtilTest {

    JwtUtil jwtUtil;
    User mockUser;
    String token;
    Map<String, Object> customClaims;
    final String testUsername = "testusername";
    final String testPassword = "testpassword";
    final String badToken = "badToken";

    @BeforeEach
    void setup() {
        jwtUtil = new JwtUtil();
        customClaims = new HashMap<>();
        token = null;
        mockUser = Mockito.mock(User.class);
        when(mockUser.getUsername()).thenReturn(testUsername);
        when(mockUser.getPassword()).thenReturn(testPassword);
    }

    @Test
    void createTokenAndExtractUsername() {
        assertEquals(jwtUtil.extractUsername(jwtUtil.generateToken(mockUser)), testUsername);
    }

    @Test
    void createTokenAndValidate() {
        assertTrue(jwtUtil.validateToken(jwtUtil.generateToken(mockUser), mockUser));
    }

    @Test
    void attemptExtractUsernameFromBadTokenAndGetNull() {
        assertNull(jwtUtil.extractUsername(badToken));
    }

    @Test
    void attemptExtractUsernameFromEmptyTokenAndReturnNull() {
        assertNull(jwtUtil.extractUsername(""));
    }

    @Test
    void createTokenWithCustomClaimsAndExtractClaims() {
        customClaims.put("cl1", "claim1");
        customClaims.put("cl2", "claim2");
        customClaims.put("cl3", "claim3");
        token = jwtUtil.generateToken(mockUser, customClaims);
        String claim1Extracted = jwtUtil.extractClaim(token, "cl1", String.class);
        String claim2Extracted = jwtUtil.extractClaim(token, "cl2", String.class);
        String claim3Extracted = jwtUtil.extractClaim(token, "cl3", String.class);
        assertEquals("claim1", claim1Extracted);
        assertEquals("claim2", claim2Extracted);
        assertEquals("claim3", claim3Extracted);
    }

    @Test
    void createTokenWithCustomClaimsAndExtractButFailByAccessingBadKey() {
        customClaims.put("cl1", "claim1");
        token = jwtUtil.generateToken(mockUser, customClaims);
        assertNull(jwtUtil.extractClaim(token, "cl2", String.class));
    }

    @Test
    void createTokenWithCustomClaimsAndExtractButFailByAccessingNullKey() {
        customClaims.put("cl1", "claim1");
        token = jwtUtil.generateToken(mockUser, customClaims);
        assertNull(jwtUtil.extractClaim(token, null, String.class));
    }

    @Test
    void createTokenWithCustomClaimsAndExtractButFailByAccessingNullClass() {
        customClaims.put("cl1", "claim1");
        token = jwtUtil.generateToken(mockUser, customClaims);
        assertNull(jwtUtil.extractClaim(token, "cl1", null));
    }
}
