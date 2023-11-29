//package com.example.projectdemogit.aspects;
//
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.ProceedingJoinPoint;
//import org.aspectj.lang.annotation.Around;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Pointcut;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//@Aspect
//@Component
//@Slf4j
//public class ServiceLoggingAspect {
//    @Pointcut("within(com.example.projectdemogit.service.impl.*)")
//    public void serviceMethods() {}
//
//    @Around("serviceMethods()")
//    public Object logAroundServiceMethod(ProceedingJoinPoint joinPoint) throws Throwable {
//        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
//        HttpServletRequest request = null;
//        HttpServletResponse response = null;
//
//        if (requestAttributes != null) {
//            request = requestAttributes.getRequest();
//            response = requestAttributes.getResponse();
//        }
//
//        if (request != null) {
//            log.info("Request URL: {}", request.getRequestURL());
//            log.info("Request Method: {}", request.getMethod());
//
//            if (joinPoint.getArgs().length > 0) {
//                log.info("Call Around Before method: {}", joinPoint.getArgs()[0]);
//            }
//        }
//
//        Object result = joinPoint.proceed();
//
//        if (response != null && (isOAuth2Authentication() || isJwtAuthentication(request))) {
//            log.info("Response Status: {}", response.getStatus());
//            log.info("Response Body: {}", result.toString());
//        }
//
//        return result;
//    }
//
//    private boolean isOAuth2Authentication() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        return authentication != null && authentication.getDetails() instanceof DefaultOAuth2User;
//    }
//
//    private boolean isJwtAuthentication(HttpServletRequest request) {
//        // Check if the header or parameter contains a JWT token
//        String authorizationHeader = request.getHeader("Authorization");
//        String jwtTokenParam = request.getParameter("jwtToken");
//        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ") || jwtTokenParam != null;
//    }
//}