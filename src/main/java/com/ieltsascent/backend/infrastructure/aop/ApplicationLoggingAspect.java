package com.ieltsascent.backend.infrastructure.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
@Slf4j
public class ApplicationLoggingAspect {

	@Pointcut("within(com.ieltsascent.backend.application..*)")
	public void applicationLayer() {
	}

	@Pointcut("!within(com.ieltsascent.backend.infrastructure.aop..*)")
	public void excludeAopLayer() {
	}

	@Around("applicationLayer() && excludeAopLayer()")
	public Object logApplicationCalls(ProceedingJoinPoint joinPoint) throws Throwable {
		return logWithContext(joinPoint);
	}

	private Object logWithContext(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = System.currentTimeMillis();
		String method = joinPoint.getSignature().toShortString();
		HttpServletRequest request = getCurrentRequest();
		String httpMethod = request != null ? safeText(request.getMethod()) : "N/A";
		String requestUri = request != null ? safeText(request.getRequestURI()) : "N/A";

		String argsText = !isSensitiveEndpoint(requestUri) && !isSensitiveMethod(method)
			? formatArguments(joinPoint.getArgs())
			: "[REDACTED]";

		log.info("-> {} {} | Enter {} | args={}", httpMethod, requestUri, method, argsText);

		try {
			Object result = joinPoint.proceed();
			long elapsedMs = System.currentTimeMillis() - start;
			log.info("<- {} {} | Exit {} | {} ms", httpMethod, requestUri, method, elapsedMs);
			return result;
		} catch (Throwable ex) {
			long elapsedMs = System.currentTimeMillis() - start;
			log.warn("xx {} {} | Fail {} | {} ms | {}", httpMethod, requestUri, method, elapsedMs, ex.getMessage());
			throw ex;
		}
	}

	private HttpServletRequest getCurrentRequest() {
		ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
		return attrs == null ? null : attrs.getRequest();
	}

	private boolean isSensitiveEndpoint(String requestUri) {
		if (requestUri == null) {
			return false;
		}
		String path = requestUri.toLowerCase();
		return path.contains("/api/auth/login")
			|| path.contains("/api/auth/register")
			|| path.contains("/api/auth/refresh");
	}

	private boolean isSensitiveMethod(String method) {
		if (method == null) {
			return false;
		}
		String value = method.toLowerCase();
		return value.contains("login(") || value.contains("register(") || value.contains("refresh(");
	}

	private String formatArguments(Object[] args) {
		if (args == null || args.length == 0) {
			return "[]";
		}
		StringBuilder builder = new StringBuilder("[");
		for (int i = 0; i < args.length; i++) {
			builder.append(safeToString(args[i]));
			if (i < args.length - 1) {
				builder.append(", ");
			}
		}
		builder.append(']');
		return builder.toString();
	}

	private String safeToString(Object value) {
		if (value == null) {
			return "null";
		}
		String text = String.valueOf(value);
		if (text.length() > 200) {
			return text.substring(0, 197) + "...";
		}
		return text;
	}

	private String safeText(String value) {
		return (value == null || value.isBlank()) ? "N/A" : value;
	}
}




