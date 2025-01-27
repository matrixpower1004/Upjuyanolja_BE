package com.backoffice.upjuyanolja.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // Member
    DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST, 1000, "중복된 이메일입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, 1001, "유효하지 않는 리프레시 토큰입니다."),
    LOGGED_OUT(HttpStatus.UNAUTHORIZED, 1002, "로그아웃한 회원입니다."),
    INCORRECT_EMAIL(HttpStatus.BAD_REQUEST, 1003, "이메일이 일치하지 않습니다."),
    INCORRECT_PASSWORD(HttpStatus.BAD_REQUEST, 1004, "비밀번호가 일치하지 않습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, 1005, "회원 정보를 찾을 수 없습니다."),
    AUTHORIZE_INFO_NOT_FOUND(HttpStatus.UNAUTHORIZED, 1006, "인증 정보를 찾을 수 없습니다."),
    EMAIL_SEND_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1007, "이메일 인증코드 발송 중 오류가 발생했습니다"),
    CREATE_VERIFICATION_CODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 1008,
        "이메일 인증코드 생성 중 오류가 발생했습니다"),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, 1009, "존재하지 않는 이메일입니다."),
    INCORRECT_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, 1010, "이메일 인증코드가 일치하지 않습니다."),
    NOT_REGISTERED_EMAIL(HttpStatus.BAD_REQUEST, 1011, "입점 DB에 등록된 이메일이 아닙니다."),
    INVALID_ROLE(HttpStatus.BAD_REQUEST, 1012, "유효한 권한이 아닙니다. 올바른 경로로 회원가입을 시도해주세요."),
    INVALID_SIGNUP_PROCESS(HttpStatus.BAD_REQUEST, 1013, "이메일 인증을 진행 한 뒤 회원가입을 시도하세요."),

    // Accommodation
    ACCOMMODATION_NOT_FOUND(HttpStatus.NOT_FOUND, 2000, "숙소 정보를 찾을 수 없습니다."),
    RESERVATION_NOT_POSSIBLE(HttpStatus.BAD_REQUEST, 2001, "예약이 불가능한 방입니다."),
    WRONG_CATEGORY(HttpStatus.BAD_REQUEST, 2002, "알맞지 않은 카테고리입니다."),
    ACCOMMODATION_IMAGE_NOT_EXISTS(HttpStatus.BAD_REQUEST, 2003, "숙소 이미지를 1개 이상 등록해야 합니다."),
    FAILED_SAVE_IMAGE(HttpStatus.BAD_REQUEST, 2004, "이미지 저장에 실패했습니다."),
    ACCOMMODATION_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, 2005, "숙소 옵션 정보를 찾을 수 없습니다."),

    // Room
    ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, 3000, "객실 정보를 찾을 수 없습니다."),
    DUPLICATED_ROOM_NAME(HttpStatus.BAD_REQUEST, 3001, "중복된 객실 이름입니다."),
    INVALID_TIME_FORMAT(HttpStatus.BAD_REQUEST, 3002, "올바르지 않은 형식의 시간입니다.(HH:mm)"),
    INVALID_ROOM_STATUS(HttpStatus.BAD_REQUEST, 3003, "올바르지 않은 형식의 객실 상태입니다."),
    ROOM_STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, 3004, "객실 재고 정보를 찾을 수 없습니다."),
    ROOM_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, 3005, "객실 이미지 정보를 찾을 수 없습니다."),
    ROOM_NOT_EXISTS(HttpStatus.BAD_REQUEST, 3006, "객실을 1개 이상 등록해야 합니다."),
    ROOM_IMAGE_NOT_EXISTS(HttpStatus.BAD_REQUEST, 3007, "객실 이미지를 1개 이상 등록해야 합니다."),
    LAST_ROOM(HttpStatus.BAD_REQUEST, 3008, "마지막 객실은 삭제할 수 없습니다."),
    ROOM_OPTION_NOT_FOUND(HttpStatus.NOT_FOUND, 3009, "객실 옵션 정보를 찾을 수 없습니다."),
    ROOM_PRICE_NOT_FOUND(HttpStatus.NOT_FOUND, 3010, "객실 가격 정보를 찾을 수 없습니다."),

    // Reservation
    RESERVATION_PAYMENT_FAILED(HttpStatus.BAD_REQUEST, 4000, "결제에 실패 했습니다."),
    INVALID_COUPON(HttpStatus.BAD_REQUEST, 4001, "쿠폰이 유효하지 않습니다."),
    INVALID_RESERVATION_INFO(HttpStatus.BAD_REQUEST, 4002, "예약 정보가 유효하지 않습니다."),
    RESERVATION_ROOM_NOT_FOUND(HttpStatus.NOT_FOUND, 4003, "예약 숙소 정보를 찾을 수 없습니다."),
    INVALID_VISITOR_NAME(HttpStatus.BAD_REQUEST, 4004, "유효하지 않는 방문자 전화번호 입니다."),
    INVALID_VISITOR_PHONE_NUMBER(HttpStatus.BAD_REQUEST, 4005, "유효하지 않는 방문자 전화번호입니다."),
    RESERVATION_NOT_FOUND(HttpStatus.BAD_REQUEST, 4006, "예약 정보를 찾을 수 없습니다."),

    // Coupon
    INVALID_COUPON_INFO(HttpStatus.BAD_REQUEST, 5000, "쿠폰 정보가 올바르지 않습니다."),
    INSUFFICIENT_COUPON_STOCK(HttpStatus.BAD_REQUEST, 5001, "쿠폰의 재고가 부족합니다."),

    // Point
    TOSS_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 6000, "토스 API 요청에 실패 했습니다."),
    PAYMENT_AUTHORIZATION_FAILED(HttpStatus.BAD_REQUEST, 6001, "결제 승인에 실패 했습니다."),
    WRONG_REFUND_INFO(HttpStatus.BAD_REQUEST, 6002, "포인트 환불 요청 정보가 잘못 됐습니다."),
    POINT_NOT_FOUND(HttpStatus.NOT_FOUND, 6003, "포인트 정보를 찾을 수 없습니다."),
    POINT_INSUFFICIENT(HttpStatus.BAD_REQUEST, 6004, "포인트 잔액이 부족합니다."),
    POINT_TRADE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, 6005, "포인트 거래 중 에러가 발생 했습니다."),

    // OpenAPI
    OPEN_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 8000, "오픈 API를 이용하는 중 에러가 발생했습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, 8001, "오픈 API에서 가져온 숙소 데이터에서 필요한 정보가 없어, 다음 숙소를 조회합니다."),

    // Common
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9000, "데이터베이스 오류가 발생했습니다."),
    INVALID_REQUEST_BODY(HttpStatus.BAD_REQUEST, 9001, "유효하지 않은 요청 바디입니다."),
    SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 9002, "서버에 알 수 없는 에러가 발생했습니다."),
    INVALID_DATE(HttpStatus.BAD_REQUEST, 9003, "유효하지 않는 날짜입니다."),
    NOT_OWNER(HttpStatus.FORBIDDEN, 9004, "숙소의 업주가 아닙니다.");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, int code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }
}
