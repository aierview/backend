package com.aierview.backend.auth.infra.token;

import com.aierview.backend.auth.domain.entity.UserRef;
import com.aierview.backend.auth.domain.token.ITokenGenerator;
import com.aierview.backend.auth.infra.mapper.UserMapper;
import com.aierview.backend.auth.infra.persisntence.jpa.entity.UserJpaEntity;
import com.aierview.backend.shared.testdata.AuthTestFixture;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.security.Key;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class JwtTokenAdapterTests {
    private final String secretKey = "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007w";
    private final long expiration = 1000 * 60 * 60;
    private ITokenGenerator encoder;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        this.userMapper = mock(UserMapper.class);
        this.encoder = new JwtTokenAdapter(this.userMapper);

        var secretField = JwtTokenAdapter.class.getDeclaredField("secretKey");
        secretField.setAccessible(true);
        secretField.set(encoder, secretKey);

        var expField = JwtTokenAdapter.class.getDeclaredField("jwtExpiration");
        expField.setAccessible(true);
        expField.set(encoder, expiration);
    }

    @Test
    @DisplayName("Should generate token with user details")
    void shouldGenerateTokenWithUserDetails() {
        UserRef savedUserRef = AuthTestFixture.anySavedUserRef();
        UserJpaEntity savedUserJpaEntity = AuthTestFixture.anyUserJpaEntity(savedUserRef);

        when(this.userMapper.userRefToUserJpaEntity(savedUserRef)).thenReturn(savedUserJpaEntity);

        String token = this.encoder.generate(savedUserRef);

        assertNotNull(token);
        Claims claims = parseToken(token);
        Assertions.assertEquals(claims.getSubject(), savedUserRef.getEmail());
        Mockito.verify(this.userMapper, Mockito.times(1)).userRefToUserJpaEntity(any());
    }

    private Claims parseToken(String token) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
