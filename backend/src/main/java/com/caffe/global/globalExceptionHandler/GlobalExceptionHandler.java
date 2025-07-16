package com.caffe.global.globalExceptionHandler;

import com.caffe.global.rsData.RsData;
import lombok.RequiredArgsConstructor;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    /**
     * ID 등으로 데이터 조회 실패 시 발생하는 예외를 처리합니다.
     * (예: findById(id).orElseThrow())
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<RsData<Void>> handle(NoSuchElementException ex) {
        // HTTP 404 Not Found 상태와 함께 에러 응답을 반환합니다.
        return new ResponseEntity<>(
                new RsData<>(
                        "404-1",
                        ex.getMessage()
                ),
                NOT_FOUND
        );
    }

    /**
     *  Validated 어노테이션을 통해 PathVariable 또는 RequestParam 유효성 검증에 실패한 경우를 처리합니다.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<RsData<Void>> handle(ConstraintViolationException ex) {
        // 여러 유효성 검증 실패 메시지를 조합하여 하나의 문자열로 만듭니다.
        String message = ex.getConstraintViolations()
                .stream()
                .map(violation -> {
                    // 에러가 발생한 필드명을 추출합니다. (예: "create.dto.name" -> "name")
                    String field = violation.getPropertyPath().toString().split("\\.", 2)[1];
                    // 메시지 템플릿에서 에러 코드를 추출합니다.
                    String[] messageTemplateBits = violation.getMessageTemplate().split("\\.");
                    String code = messageTemplateBits[messageTemplateBits.length - 2];
                    // 실제 에러 메시지를 가져옵니다.
                    String _message = violation.getMessage();
                    // "필드명-코드-메시지" 형식으로 에러 메시지를 조합합니다.
                    return "%s-%s-%s".formatted(field, code, _message);
                })
                // 필드명 기준으로 정렬하여 응답의 일관성을 유지합니다.
                .sorted(Comparator.comparing(String::toString))
                // 각 메시지를 줄바꿈 문자로 연결합니다.
                .collect(Collectors.joining("\n"));

        // HTTP 400 Bad Request 상태와 함께 조합된 에러 메시지를 반환합니다.
        return new ResponseEntity<>(
                new RsData<>(
                        "400-1",
                        message
                ),
                BAD_REQUEST
        );
    }

    //Valid 에서 RequestBody로 받은 DTO 유효성 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RsData<Void>> handle(MethodArgumentNotValidException ex) {
        String message = ex.getBindingResult()
                .getAllErrors()
                .stream()
                .filter(error -> error instanceof FieldError)
                .map(error -> (FieldError) error)
                .map(error -> error.getField() + "-" + error.getCode() + "-" + error.getDefaultMessage())
                .sorted(Comparator.comparing(String::toString))
                .collect(Collectors.joining("\n"));

        return new ResponseEntity<>(
                new RsData<>(
                        "400-1",
                        message
                ),
                BAD_REQUEST
        );
    }

    //요청 본문의 JSON 형식이 잘못되었을 떄
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RsData<Void>> handle(HttpMessageNotReadableException ex) {
        return new ResponseEntity<>(
                new RsData<>(
                        "400-1",
                        ex.getMessage()
                ),
                BAD_REQUEST
        );
    }

    @ExceptionHandler(MissingRequestHeaderException.class)
    public ResponseEntity<RsData<Void>> handle(MissingRequestHeaderException ex) {
        return new ResponseEntity<>(
                new RsData<>(
                        "400-1",
                        "%s-%s-%s".formatted(
                                ex.getHeaderName(),
                                "NotBlank",
                                ex.getLocalizedMessage()
                        )
                ),
                BAD_REQUEST
        );
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RsData<Void>> handle(IllegalArgumentException ex) {
        return new ResponseEntity<>(
                new RsData<>(
                        "400-2",  // 기존 400-1과 구분하기 위해 400-2 사용
                        ex.getMessage()
                ),
                BAD_REQUEST
        );
    }
}
