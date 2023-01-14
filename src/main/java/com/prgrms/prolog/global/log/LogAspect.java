package com.prgrms.prolog.global.log;

import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
public class LogAspect {

	@Around("within(com.prgrms.prolog.domain..api.*Controller)")
	public Object logging(ProceedingJoinPoint joinPoint) throws Throwable {
		RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		try {
			HttpServletRequest httpServletRequest = ((ServletRequestAttributes)
				Objects.requireNonNull(requestAttributes)).getRequest();
			CustomHttpServletRequestWrapper request = new CustomHttpServletRequestWrapper(httpServletRequest);

			log.info("Request -----> {} -> ({} {}), ({})",
				joinPoint.getSignature(), request.getMethod(), request.getRequestURI(), request.getCachedBody());
		} catch (Exception e) {
			log.info("LogAspect Error ", e);
		}
		return joinPoint.proceed();
	}

}
