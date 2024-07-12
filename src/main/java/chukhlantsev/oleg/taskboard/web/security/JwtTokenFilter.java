package chukhlantsev.oleg.taskboard.web.security;

import chukhlantsev.oleg.taskboard.domain.exception.ResourceNotFoundException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtTokenFilter extends GenericFilter {


    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        String bearerToken = ((HttpServletRequest) servletRequest).getHeader("Authorized");
        if(bearerToken != null && bearerToken.startsWith("Bearer "))
        {
            bearerToken = bearerToken.substring(7);
        }

        if(jwtTokenProvider.validateToken(bearerToken))
            try {
                Authentication authentication = jwtTokenProvider.getAuthentication(bearerToken);
                if(authentication != null)
                {
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
                //перехватываем исключение если пользователь будет не найден
            } catch (ResourceNotFoundException ignored) {

            }
    }
}
