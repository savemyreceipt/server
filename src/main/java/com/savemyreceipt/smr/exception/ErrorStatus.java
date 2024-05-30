package com.savemyreceipt.smr.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorStatus {

    /*
     * 400 Bad Request
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "Bad Request"),
    TOO_MUCH_GROUPS(HttpStatus.BAD_REQUEST, "그룹은 최대 10개까지 가입 가능합니다."),

    /*
     * 401 Unauthorized
     */

    /*
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 정보를 찾을 수 없습니다."),

    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
