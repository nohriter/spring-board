package com.nori.springboard.config;

import static com.nori.springboard.config.SessionConst.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class LoginMemberArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		log.info("supportsParameter 실행");
		boolean hasLoginAnnotation = parameter.hasParameterAnnotation(Login.class);
		boolean hasLongType = Long.class.isAssignableFrom(parameter.getParameterType());

		return hasLoginAnnotation && hasLongType;
	}

	@Override
	public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer,
		NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {

		log.info("resolveArgument 실행");

		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

		HttpSession session = request.getSession(false);

		if (session == null) {
			log.info("session == null)");
			throw new IllegalStateException("세션이 존재하지 않습니다.");
		}

		return session.getAttribute(LOGIN_MEMBER);
	}
}
