package net.thumbtack.school.server;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    WRONG_JSON("wrong json string"),
    NULL_OR_EMPTY_FIELD("null or empty field detected"),
    USER_WRONG_NAME("wrong name"),
    USER_WRONG_SURNAME("wrong surname"),
    USER_WRONG_LOGIN("wrong login"),
    USER_WRONG_PASSWORD("wrong password"),
    USER_WRONG_STATUS("wrong user status (buyer or seller)"),
    USER_IS_ALREADY_REGISTERED("the user is already registered"),
    USER_NOT_FOUND("user not found"),
    LOT_IS_ALREADY_REGISTERED("the lot is already registered"),
    LOT_WRONG_NAME("wrong lot name"),
    LOT_NOT_FOUND("lot not found"),
    LOT_WRONG_OWNER("the lot does not belong to you"),
    LOT_WRONG_PRICE("wrong price"),
    LOT_WRONG_CATEGORY("wrong category"),
    LOT_NOT_ACCEPTING_BIDS("bids for the lot are not accepted"),
    LOT_IS_ALREADY_SOLD("the lot is already sold"),
    BID_WRONG_OFFER("the offer must be higher then the current price"),
    FILE_READING_ERROR("file reading error");

    private final String errorString;
}
