package com.qkforest.apigatewayservice.filter;

import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@Slf4j
public class AuthorizationFilter extends AbstractGatewayFilterFactory<AuthorizationFilter.Config> {
        Environment env;

    public AuthorizationFilter(Environment env) {
            super(Config.class);
            this.env = env;
        }

        public static class Config {

        }

        @Override
        public GatewayFilter apply(Config config) {
            return (exchange, chain) -> {
                ServerHttpRequest request = exchange.getRequest();

                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    return onError(exchange, "authorization 헤더가 비어 있습니다.", HttpStatus.UNAUTHORIZED);
                }

                String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                String jwt = authorizationHeader.replace("Bearer", "");

                if (!isJwtValid(jwt)) {
                    return onError(exchange, "유효하지 않은 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED);
                }

                return chain.filter(exchange);
            };
        }

        private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
            ServerHttpResponse response = exchange.getResponse();
            response.setStatusCode(httpStatus);
            log.error(err);

            byte[] bytes = err.getBytes(StandardCharsets.UTF_8);
            DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
            return response.writeWith(Flux.just(buffer));
        }

        private boolean isJwtValid(String jwt) {
            byte[] secretKeyBytes = Base64.getEncoder().encode(env.getProperty("token.secret").getBytes());
            SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

            boolean returnValue = true;
            String subject = null;

            try {
                JwtParser jwtParser = Jwts.parserBuilder()
                        .setSigningKey(signingKey)
                        .build();

                subject = jwtParser.parseClaimsJws(jwt).getBody().getSubject();
            } catch (Exception ex) {
                returnValue = false;
            }

            if (subject == null || subject.isEmpty()) {
                returnValue = false;
            }

            return returnValue;
        }

    }
