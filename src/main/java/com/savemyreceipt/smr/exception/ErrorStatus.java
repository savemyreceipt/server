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
    ALREADY_JOINED_GROUP(HttpStatus.BAD_REQUEST, "이미 가입한 그룹입니다."),
    RECEIPT_ALREADY_CHECKED(HttpStatus.BAD_REQUEST, "이미 확인한 영수증입니다."),
    ACCOUNTANT_CANNOT_LEAVE_GROUP(HttpStatus.BAD_REQUEST, "회계담당자는 그룹을 탈퇴할 수 없습니다."),

    /*
     * 401 Unauthorized
     */
    UNAUTHORIZED_MEMBER_ACCESS(HttpStatus.UNAUTHORIZED, "해당 회원은 권한이 없습니다."),

    /*
     * 404 Not Found
     */
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "회원 정보를 찾을 수 없습니다."),
    GROUP_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 정보를 찾을 수 없습니다."),
    GROUP_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "그룹 멤버 정보를 찾을 수 없습니다."),
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지를 찾을 수 없습니다."),
    RECEIPT_NOT_FOUND(HttpStatus.NOT_FOUND, "영수증을 찾을 수 없습니다."),
    NOTIFICATION_NOT_FOUND(HttpStatus.NOT_FOUND, "알림을 찾을 수 없습니다."),

    /*
     * 500 Internal Server Error
     */
    IMAGE_UPLOAD_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 업로드에 실패했습니다."),
    IMAGE_PROCESS_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "이미지 처리에 실패했습니다. (Gemini)"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }

}
