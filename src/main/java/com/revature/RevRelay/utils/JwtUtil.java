package com.revature.RevRelay.utils;

import com.revature.RevRelay.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Provides JWT functionality for creating and reading JWTs.
 * For more information, see the Java JWT GitHub at https://github.com/jwtk/jjwt.
 *
 * @author Nathan Luther
 */
@Service
public class JwtUtil {

    //this will obviously need to not be in the source code in the project deliverable.
//    @Value("${jwt.secret}")
//    private String SECRET_KEY;
    private final String SECRET_KEY;

    //    @Value("#{24*60*60*1000}")
    private int expiration;

    //    this is currently nonfunctional, but should be implemented as part of getting rid of the deprecated method below.
    private final SignatureAlgorithm sigAlg;
    private Key signingKey;

    public JwtUtil() {
        SECRET_KEY = "secretsecretsecretsecretsecretsecretsecretsecretsecretsecret";
        expiration = 86_400_000;
        sigAlg = SignatureAlgorithm.HS256;
        createSigningKey();
    }

    private void createSigningKey() {
        byte[] saltBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        signingKey = new SecretKeySpec(saltBytes, sigAlg.getJcaName());
    }

    /**
     * Extracts Username from token via ExtractClaim
     *
     * @param token JWT
     * @return Returns Username as String
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts Expiration from token via ExtractClaim
     *
     * @param token JWT
     * @return Returns expiration as Date.
     */
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extracts a specified Claim or Claims from token via ExtractClaim
     *
     * @param token          JWT
     * @param claimsResolver A Function object defining the specific claims to be extract.
     * @param <T>
     * @return An Object containing extracted claims.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws NullPointerException {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        } catch (NullPointerException e) {
            return null;
        }
    }

    public <T> T extractClaim(String token, String key, Class<T> T) {
        try {
            final Claims claims = extractAllClaims(token);
            return claims.get(key, T);
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Extracts all Claims from a token generically. Used for other claim extraction functions.
     *
     * @param token JWT
     * @return A Claims object containing all claims of the token.
     */
    private Claims extractAllClaims(String token) throws ExpiredJwtException {
        Claims claims;
        try {
//            claims = Jwts.parserBuilder().setSigningKey(signingKey).build().parseClaimsJws(token).getBody();
            claims = Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
            return claims;
        } catch (IllegalArgumentException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
            return null;
        }
    }

    /**
     * Extracts Expiration claim via extractExpiration and compares to current Date().
     *
     * @param token JWT
     * @return True if Expiration is before current Date(), false otherwise.
     */
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Generates a token with a User object using an empty map of claims via createToken.
     *
     * @param user User object.
     * @return A JWT as a string via CreateToken.
     */
    public String generateToken(User user) {
        //a "map of claims"
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, user.getUsername());
    }

    /**
     * Generates a token with a User object using a defined map of claims via createToken.
     *
     * @param user   User object.
     * @param claims A Map of key-value pairs corresponding to claim name (i.e. subject,
     *               issuer, expiration). Use this field for setting custom claims.
     *               Claims object can be any JSON-serializable object (i.e. String, Date).
     *               For more information see https://github.com/jwtk/jjwt#jws-create-claims.
     * @return A JWT as a string via CreateToken.
     */
    public String generateToken(User user, Map<String, Object> claims) {
        // takes a "map of claims" as input in addition to a User object.
        return createToken(claims, user.getUsername());
    }

    /**
     * Creates a JWT using provided claims and subject.
     *
     * @param claims  A Map of Claims provided by generateToken.
     * @param subject A Subject (here always a User object).
     * @return A JWT as a string via Jwts.builder()...
     */
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder().setClaims(claims).setSubject(subject).setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
//                deprecated method will need to be changed.
//                .signWith(signingKey).compact();
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY).compact();
    }

    /**
     * Validates a token against a provided User object.
     *
     * @param token JWT of unverified validity and source.
     * @param user  User object supposedly corresponding to the above JWT.
     * @return True if token can be parsed correctly, token is accurate to User, and token is not expired; else false.
     */
    public Boolean validateToken(String token, User user) {
        String username = extractUsername(token);
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }
}
