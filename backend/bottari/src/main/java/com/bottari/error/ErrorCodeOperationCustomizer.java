package com.bottari.error;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springframework.http.HttpStatus;
import org.springframework.web.method.HandlerMethod;

/**
 * Spring Doc에서 에러 코드를 Swagger 문서에 자동으로 추가하는 커스터마이저입니다.
 *
 * <p>이 클래스는 {@link ApiErrorCodes} 어노테이션이 붙은 컨트롤러 메서드에서
 * 에러 코드 정보를 추출하여 Swagger OpenAPI 문서의 응답 섹션에 자동으로 추가합니다.</p>
 *
 * <h3>주요 기능:</h3>
 * <ul>
 *   <li>컨트롤러 메서드의 {@code @ApiErrorCodes} 어노테이션에서 에러 코드 추출</li>
 *   <li>HTTP 상태별로 에러 코드 그룹화</li>
 *   <li>RFC 7807 Problem Details 형식의 에러 응답 예시 생성</li>
 *   <li>Swagger UI에서 각 에러 코드별 예시 데이터 제공</li>
 * </ul>
 *
 * <h3>사용 예시:</h3>
 * <pre>{@code
 * @RestController
 * public class MemberController {
 *
 *     @PutMapping("/members/me")
 *     @ApiErrorCodes({MEMBER_NOT_FOUND, MEMBER_NAME_TOO_LONG})
 *     public ResponseEntity<Void> update(
 *             @RequestBody final UpdateMemberRequest request
 *     ) {
 *         // 메서드 구현
 *     }
 * }
 * }</pre>
 *
 * <p>위 예시에서 {@code MEMBER_NOT_FOUND}는 404 응답으로,
 * {@code MEMBER_NAME_TOO_LONG}은 400 응답으로 자동 분류되어
 * Swagger 문서에 추가됩니다.</p>
 *
 * @see ApiErrorCodes
 * @see ErrorCode
 * @see OperationCustomizer
 */
public class ErrorCodeOperationCustomizer implements OperationCustomizer {

    private static final String APPLICATION_PROBLEM_JSON = "application/problem+json";

    @Override
    public Operation customize(
            final Operation operation,
            final HandlerMethod handlerMethod
    ) {
        final List<ErrorCode> errorCodes = extractErrorCodesFromAnnotation(handlerMethod);
        if (!errorCodes.isEmpty()) {
            addErrorResponsesToOperation(operation, errorCodes);
        }
        return operation;
    }

    private List<ErrorCode> extractErrorCodesFromAnnotation(final HandlerMethod handlerMethod) {
        return Optional.ofNullable(handlerMethod.getMethodAnnotation(ApiErrorCodes.class))
                .map(ApiErrorCodes::value)
                .map(Arrays::asList)
                .orElse(Collections.emptyList());
    }

    private void addErrorResponsesToOperation(
            final Operation operation,
            final List<ErrorCode> errorCodes
    ) {
        final Map<HttpStatus, List<ErrorCode>> errorsByStatus = groupErrorCodesByStatus(errorCodes);
        final ApiResponses responses = operation.getResponses();
        errorsByStatus.forEach((httpStatus, codes) ->
                responses.addApiResponse(String.valueOf(httpStatus.value()), createApiErrorResponse(codes))
        );
    }

    private Map<HttpStatus, List<ErrorCode>> groupErrorCodesByStatus(final List<ErrorCode> errorCodes) {
        return errorCodes.stream()
                .collect(Collectors.groupingBy(ErrorCode::getStatus));
    }

    private ApiResponse createApiErrorResponse(final List<ErrorCode> errorCodes) {
        final String description = errorCodes.stream()
                .map(code -> "- **%s**: %s".formatted(code.name(), code.getMessage()))
                .collect(Collectors.joining("\n"));
        final MediaType mediaType = new MediaType();
        errorCodes.forEach(errorCode -> mediaType.addExamples(errorCode.name(), createErrorExample(errorCode)));
        final Content content = new Content().addMediaType(APPLICATION_PROBLEM_JSON, mediaType);

        return new ApiResponse()
                .description(description)
                .content(content);
    }

    private Example createErrorExample(final ErrorCode code) {
        return new Example()
                .summary(code.name())
                .value(Map.of(
                        "type", "about:blank",
                        "title", code.name(),
                        "status", code.getStatus().value(),
                        "detail", code.getMessage(),
                        "instance", "요청 URI"
                ));
    }
}
