package vn.zamio.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

public class CustomAuthenticationProvider implements AuthenticationProvider {

    private static final Logger logger = LoggerFactory.getLogger(CustomAuthenticationProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        logger.debug("Authenticating authenticationToken");
        OktaTokenAuthenticationToken auth = (OktaTokenAuthenticationToken) authentication;
        String accessToken = auth.getToken();

        // You should make a POST request to ${oktaBaseUrl}/v1/introspect
        // to determine if the access token is good or bad

        // I just put a dummy if here

        if ("ThanhLoyal".equals(accessToken)){
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
            logger.debug("Good access token");
            return new UsernamePasswordAuthenticationToken(auth.getPrincipal(), "[ProtectedPassword]", authorities);
        }
        logger.debug("Bad access token");
        return null;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz == OktaTokenAuthenticationToken.class;
    }
}
