package com.savemyreceipt.smr.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessStatus {

    /*
     * 200 OK
     */
    OK(HttpStatus.OK, "OK"),
    GET_MEMBER_SUCCESS(HttpStatus.OK, "사용자 조회 성공"),
    GET_GROUP_SUCCESS(HttpStatus.OK, "그룹 조회 성공"),
    SEARCH_GROUP_SUCCESS(HttpStatus.OK, "그룹 검색 성공"),
    GET_RECEIPT_SUCCESS(HttpStatus.OK, "영수증 조회 성공"),
    GET_NOTIFICATION_LIST_SUCCESS(HttpStatus.OK, "알림 목록 조회 성공"),
    GET_GROUP_MEMBER_SUCCESS(HttpStatus.OK, "그룹 멤버 조회 성공"),

    /*
     * 201 CREATED
     */
    SIGNUP_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),
    CREATE_GROUP_SUCCESS(HttpStatus.CREATED, "그룹 생성 성공"),
    UPLOAD_RECEIPT_SUCCESS(HttpStatus.CREATED, "영수증 업로드 성공"),

    /*
     * 204 NO_CONTENT
     */
    JOIN_GROUP_SUCCESS(HttpStatus.NO_CONTENT, "그룹 가입 성공"),
    UPDATE_MEMBER_SUCCESS(HttpStatus.NO_CONTENT, "사용자 정보 수정 성공"),
    UPDATE_GROUP_SUCCESS(HttpStatus.NO_CONTENT, "그룹 정보 수정 성공"),
    UPDATE_RECEIPT_SUCCESS(HttpStatus.NO_CONTENT, "영수증 수정 성공"),
    DELETE_RECEIPT_SUCCESS(HttpStatus.NO_CONTENT, "영수증 삭제 성공"),
    CHECK_NOTIFICATION_SUCCESS(HttpStatus.NO_CONTENT, "알림 확인 성공"),
    LEAVE_GROUP_SUCCESS(HttpStatus.NO_CONTENT, "그룹 탈퇴 성공"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
