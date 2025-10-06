package com.practice.apigateway.fillter;


import com.practice.apigateway.dto.JwtDto;
import com.practice.apigateway.dto.JwtValidationDto;
import com.practice.apigateway.exception.AuthException;
import com.practice.apigateway.service.AuthService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {
    private RouteFilter routeFilter;
    private AuthService authService;

    public AuthFilter() {
        super(Config.class);
    }

    @Autowired
    public void setRouteFilter(RouteFilter routeFilter){
        this.routeFilter = routeFilter;
    }

    @Autowired
    public void setAuthService(AuthService authService){
        this.authService = authService;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {

            log.info("Request URI: {}", exchange.getRequest().getURI().getPath());

            if(routeFilter.isSecured.test(exchange.getRequest())){
                if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new AuthException("JWT not provided in the Authorization Header", HttpStatus.UNAUTHORIZED);
                }
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).getFirst();

                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    String jwt = authHeader.substring(7);

                    return authService.validateToken(new JwtDto(jwt))
                            .flatMap(res -> {
                                // check JWT is valid or not
                                if(!res.isValid()){
                                    return Mono.error(new AuthException("Invalid JWT provided!", HttpStatus.UNAUTHORIZED));
                                }
                                // check for admin routes
                                if(routeFilter.isAdminEndPoint.test(exchange.getRequest()) && routeFilter.isAdminRequestMethod.test(exchange.getRequest()) && res.getRole().equals("USER")){
                                    return Mono.error(new AuthException("Access denied!", HttpStatus.FORBIDDEN));
                                }
                                return chain.filter(exchange);
                            });
                }
                throw new AuthException("Provide valid Authorization Header", HttpStatus.UNAUTHORIZED);
            }
            return chain.filter(exchange);
        });
    }

    public static class Config{}
}
