package com.bottari.config;

import com.bottari.error.BusinessException;
import com.bottari.error.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class MemberIdentifierArgumentResolver implements HandlerMethodArgumentResolver {

    public static final String MEMBER_IDENTIFIER_HEADER = "ssaid";

    @Override
    public boolean supportsParameter(final MethodParameter parameter) {
        return parameter.hasParameterAnnotation(MemberIdentifier.class);
    }

    @Override
    public Object resolveArgument(
            final MethodParameter parameter,
            final ModelAndViewContainer mavContainer,
            final NativeWebRequest webRequest,
            final WebDataBinderFactory binderFactory
    ) {
        final HttpServletRequest request = webRequest.getNativeRequest(HttpServletRequest.class);
        final String memberIdentifier = request.getHeader(MEMBER_IDENTIFIER_HEADER);
        if (memberIdentifier == null || memberIdentifier.isBlank()) {
            throw new BusinessException(ErrorCode.MEMBER_IDENTIFIER_NOT_FOUND_IN_REQUEST, MEMBER_IDENTIFIER_HEADER);
        }
        return memberIdentifier;
    }
}
