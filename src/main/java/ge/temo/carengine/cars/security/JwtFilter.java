package ge.temo.carengine.cars.security;

import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtFilter extends OncePerRequestFilter {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWSVerifier verifier = new MACVerifier(secretKey.getBytes());

            if(!signedJWT.verify(verifier)) {
                chain.doFilter(request, response);
                return;
            }

            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if(expirationTime == null || expirationTime.before(new Date())) {
                System.out.println("JWT is expired");
                chain.doFilter(request, response);
                return;
            }

            String username = signedJWT.getJWTClaimsSet().getSubject();
            List<String> roles = signedJWT.getJWTClaimsSet().getStringListClaim("roles");

            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, null,
                            roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                    .collect(Collectors.toList()));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (Exception e) {
            System.out.println("Error during jwt extraction");
        }

        chain.doFilter(request, response);
    }
}
