package com.savemyreceipt.smr.DTO;

import com.savemyreceipt.smr.exception.ErrorStatus;
import com.savemyreceipt.smr.exception.SuccessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.MethodArgumentNotValidException;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ApiResponseDto<T> {

    private final int code;
    private final String message;
    private T data;

    public static ApiResponseDto<?> success(SuccessStatus successStatus) {
        return new ApiResponseDto<>(successStatus.getHttpStatus().value(),
            successStatus.getMessage());
    }

    public static <T> ApiResponseDto<T> success(SuccessStatus successStatus, T data) {
        return new ApiResponseDto<>(successStatus.getHttpStatus().value(),
            successStatus.getMessage(), data);
    }

    public static ApiResponseDto<?> error(ErrorStatus errorStatus) {
        return new ApiResponseDto<>(errorStatus.getHttpStatus().value(), errorStatus.getMessage());
    }

    public static ApiResponseDto<?> error(MethodArgumentNotValidException e, String message) {
        return new ApiResponseDto<>(e.getStatusCode().value(), message);
    }


}
