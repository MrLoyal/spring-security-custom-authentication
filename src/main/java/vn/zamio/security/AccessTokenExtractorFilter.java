package vn.zamio.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class AccessTokenExtractorFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(AccessTokenExtractorFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.debug("Filtering request");
        Authentication authentication = getAuthentication(request);
        if (authentication == null){
            logger.debug("Continuing filtering process without an authentication");
            filterChain.doFilter(request, response);
        } else {
            logger.debug("Now set authentication on the request");
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);
        }
    }

    private Authentication getAuthentication(HttpServletRequest request) {
        String accessToken = request.getHeader("Authorization");
        if (accessToken != null){
            logger.debug("An access token found in request header");
            List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("USER"));
            return new OktaTokenAuthenticationToken(accessToken, authorities);
        }

        logger.debug("No access token found in request header");
        return null;
    }
}
