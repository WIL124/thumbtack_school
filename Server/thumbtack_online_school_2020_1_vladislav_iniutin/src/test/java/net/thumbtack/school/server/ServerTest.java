package net.thumbtack.school.server;

import com.google.gson.Gson;
import net.thumbtack.school.dto.request.*;
import net.thumbtack.school.dto.response.LoginDtoResponse;
import net.thumbtack.school.dto.response.LotIdDtoResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServerTest {
    final Gson gson = new Gson();
    final Server server = new Server();
    ServerResponse serverResponse;
    ErrorCode errorCode;


    private boolean responseEqualError(ErrorCode response) {
        errorCode = gson.fromJson(serverResponse.getResponseData(), ErrorCode.class);
        return response == errorCode;
    }

    @Test
    void TestRegisterUser() {
        //try to register user with null name
        RegisterUserDtoRequest request = new RegisterUserDtoRequest(null, "Conor", "", "JonConor1", "jonConor123", "seller");
        serverResponse = server.registerUser(gson.toJson(request));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_NAME));

        //try to register user with wrong login
        request = new RegisterUserDtoRequest("Jon", "Conor", "", "Jon", "jonConor123", "seller");
        serverResponse = server.registerUser(gson.toJson(request));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_LOGIN));

        //try to register user with wrong status
        request = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor1", "jonConor123", "seller  ");
        serverResponse = server.registerUser(gson.toJson(request));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_STATUS));

        //try to register user with null login
        request = new RegisterUserDtoRequest("Jon", "Conor", "", null, "jonConor123", "seller");
        serverResponse = server.registerUser(gson.toJson(request));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_LOGIN));

        //try to register user with wrong password
        request = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor1", "jonconor123", "seller");
        serverResponse = server.registerUser(gson.toJson(request));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_PASSWORD));

        //success register
        RegisterUserDtoRequest userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor1", "jonConor123", "seller");
        String jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());


        //try to register userJon twice
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_IS_ALREADY_REGISTERED));

        // try to register another user with the same login
        RegisterUserDtoRequest userJonCopy = new RegisterUserDtoRequest("David", "Star", "", "JonConor1", "David123", "seller");
        serverResponse = server.registerUser(gson.toJson(userJonCopy));
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_IS_ALREADY_REGISTERED));
    }

    @Test
    void TestLoginUser() {
        //success register
        RegisterUserDtoRequest userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor2", "jonConor123", "seller");
        String jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to login with false login
        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("JonConor121", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_LOGIN));

        //try to login with false password
        loginDtoRequest = new LoginDtoRequest("JonConor2", "jonConor1234");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_PASSWORD));

        //userJon is registered now. Try to login
        loginDtoRequest = new LoginDtoRequest("JonConor2", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        String token = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class).getToken();

        //Try to login twice and compare tokens
        serverResponse = server.loginUser(jsonRequest);
        String anotherToken = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class).getToken();
        assertNotEquals(token, anotherToken);

        //Try to logout userJon
        LogoutDtoRequest logoutDtoRequest = new LogoutDtoRequest(anotherToken);
        jsonRequest = gson.toJson(logoutDtoRequest);
        serverResponse = server.logoutUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to logout twice
        serverResponse = server.logoutUser(jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_NOT_FOUND));
    }

    @Test
    void TestAddLot() {
        //register and login new user
        RegisterUserDtoRequest userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor3", "jonConor123", "seller");
        String jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("JonConor3", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        LoginDtoResponse loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String token = loginDtoResponse.getToken();

        //add lot
        LotDtoRequest lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture", "antiques"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        Integer lotID = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        //add lot with wrong category
        lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"lalala"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.LOT_WRONG_CATEGORY));

        //add lot with one wrong category
        lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture", "lalala"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.LOT_WRONG_CATEGORY));

        //add lot with wrong price (max > min)
        lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture"}, 1000, 1000, 999);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.LOT_WRONG_PRICE));

        //add lot with null prices
        lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture"}, 0, 0, 0);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.LOT_WRONG_PRICE));

        //try to add lot by buyer
        RegisterUserDtoRequest userJonBuyer = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor4", "jonConor123", "buyer");
        jsonRequest = gson.toJson(userJonBuyer);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        LoginDtoRequest loginDtoRequest1 = new LoginDtoRequest("JonConor4", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest1);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        token = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class).getToken();

        lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
        assertTrue(responseEqualError(ErrorCode.USER_WRONG_STATUS));
    }

    @Test
    void TestRemoveLot() {
        //register and login new user
        RegisterUserDtoRequest userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor5", "jonConor123", "seller");
        String jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("JonConor5", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        LoginDtoResponse loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String token = loginDtoResponse.getToken();

        //add lot
        LotDtoRequest lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture", "antiques"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(token, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        int lotToken = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        //register and login another user
        userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor6", "jonConor123", "seller");
        jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        loginDtoRequest = new LoginDtoRequest("JonConor6", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String tokenAnother = loginDtoResponse.getToken();

        //try to remove lot by another user
        LotIdDtoRequest lotIdDtoRequest = new LotIdDtoRequest(lotToken);
        jsonRequest = gson.toJson(lotIdDtoRequest);
        serverResponse = server.removeLot(tokenAnother, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //remove lot
        lotIdDtoRequest = new LotIdDtoRequest(lotToken);
        jsonRequest = gson.toJson(lotIdDtoRequest);
        serverResponse = server.removeLot(token, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to remove lot twice
        serverResponse = server.removeLot(token, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());
    }

    @Test
    void TestGetLotsInformation() {
        //register and login new user SELLER
        RegisterUserDtoRequest seller = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor7", "jonConor123", "seller");
        String jsonRequest = gson.toJson(seller);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("JonConor7", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        LoginDtoResponse loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String sellerToken = loginDtoResponse.getToken();

        //register and login new user BUYER
        RegisterUserDtoRequest buyer = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor8", "jonConor123", "buyer");
        jsonRequest = gson.toJson(buyer);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        loginDtoRequest = new LoginDtoRequest("JonConor8", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String buyerToken = loginDtoResponse.getToken();

        //add 3 lots by SELLER in any categories
        LotDtoRequest lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture", "antiques", "banknotes and coins"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(sellerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        int lotID1 = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        lotDtoRequest = new LotDtoRequest("Чёрный квадрат", "просто квадрат, ничего более", new String[]{"picture"}, 1111, 1111, 11111);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(sellerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        int lotID2 = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        lotDtoRequest = new LotDtoRequest("Золотой рубль 1703г", "золото 999 пробы", new String[]{"banknotes and coins", "antiques"}, 222, 222, 2222);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(sellerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        int lotID3 = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        //get inf about lot by BUYER
        LotIdDtoRequest lotIdDtoRequest = new LotIdDtoRequest(lotID1);
        jsonRequest = gson.toJson(lotIdDtoRequest);
        serverResponse = server.getLotInformation(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //get all lots "picture"
        LotCategoryDtoRequest lotCategoryDtoRequest = new LotCategoryDtoRequest("picture");
        jsonRequest = gson.toJson(lotCategoryDtoRequest);
        serverResponse = server.getAllLotsByCategory(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to get all lots "lalala"
        lotCategoryDtoRequest = new LotCategoryDtoRequest("lalala");
        jsonRequest = gson.toJson(lotCategoryDtoRequest);
        serverResponse = server.getAllLotsByCategory(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //get union
        LotCategoriesDtoRequest lotCategoriesDtoRequest = new LotCategoriesDtoRequest(new String[]{"antiques", "banknotes and coins"});
        jsonRequest = gson.toJson(lotCategoriesDtoRequest);
        serverResponse = server.getAllLotsByCategoryUnion(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //get wrong union
        lotCategoriesDtoRequest = new LotCategoriesDtoRequest(new String[]{"antiques", "lalala and coins"});
        jsonRequest = gson.toJson(lotCategoriesDtoRequest);
        serverResponse = server.getAllLotsByCategoryUnion(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //get intersection
        lotCategoriesDtoRequest = new LotCategoriesDtoRequest(new String[]{"antiques", "banknotes and coins"});
        jsonRequest = gson.toJson(lotCategoriesDtoRequest);
        serverResponse = server.getAllLotsByCategoryIntersection(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //get wrong intersection
        lotCategoriesDtoRequest = new LotCategoriesDtoRequest(new String[]{"antiques", "banknotes and lalala"});
        jsonRequest = gson.toJson(lotCategoriesDtoRequest);
        serverResponse = server.getAllLotsByCategoryIntersection(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

    }

    @Test
    void TestBidsAndAuctioneer() {
        //register and login new user SELLER
        RegisterUserDtoRequest seller = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor9", "jonConor123", "seller");
        String jsonRequest = gson.toJson(seller);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        LoginDtoRequest loginDtoRequest = new LoginDtoRequest("JonConor9", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        LoginDtoResponse loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String sellerToken = loginDtoResponse.getToken();

        //register and login new user BUYER
        RegisterUserDtoRequest userJon = new RegisterUserDtoRequest("Jon", "Conor", "", "JonConor10", "jonConor123", "buyer");
        jsonRequest = gson.toJson(userJon);
        serverResponse = server.registerUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        loginDtoRequest = new LoginDtoRequest("JonConor10", "jonConor123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        loginDtoResponse = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class);
        String buyerToken = loginDtoResponse.getToken();

        //add lot by SELLER
        LotDtoRequest lotDtoRequest = new LotDtoRequest("Джоконда", "или Мона Лиза", new String[]{"picture", "antiques"}, 1000, 1000, 100000);
        jsonRequest = gson.toJson(lotDtoRequest);
        serverResponse = server.addLot(sellerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        int lotID1 = gson.fromJson(serverResponse.getResponseData(), LotIdDtoResponse.class).getLotID();

        //submit bid with wrong offer
        BidDtoRequest bidDtoRequest = new BidDtoRequest(5, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //submit bid with wrong lotId
        bidDtoRequest = new BidDtoRequest(5, 1234);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //submit bid
        bidDtoRequest = new BidDtoRequest(2000, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to submit bid with offer < currentPrice
        bidDtoRequest = new BidDtoRequest(1999, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //try to submit bid with offer == currentPrice
        bidDtoRequest = new BidDtoRequest(2000, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //submit bid with another offer
        bidDtoRequest = new BidDtoRequest(2500, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to stop accepting bids by another user
        LotIdDtoRequest lotIdDtoRequest = new LotIdDtoRequest(lotID1);
        jsonRequest = gson.toJson(lotIdDtoRequest);
        serverResponse = server.stopAcceptingBids(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //stop accepting bids
        lotIdDtoRequest = new LotIdDtoRequest(lotID1);
        jsonRequest = gson.toJson(lotIdDtoRequest);
        serverResponse = server.stopAcceptingBids(sellerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //try to submit bid
        bidDtoRequest = new BidDtoRequest(3000, lotID1);
        jsonRequest = gson.toJson(bidDtoRequest);
        serverResponse = server.submitBid(buyerToken, jsonRequest);
        assertEquals(400, serverResponse.getResponseCode());

        //login auctioneer
        loginDtoRequest = new LoginDtoRequest("auctioneer", "passWord123");
        jsonRequest = gson.toJson(loginDtoRequest);
        serverResponse = server.loginUser(jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
        String auctioneerToken = gson.fromJson(serverResponse.getResponseData(), LoginDtoResponse.class).getToken();

        //get full lotInfo by auctioneer
        LotIdDtoRequest lotIdDtoRequest2 = new LotIdDtoRequest(lotID1);
        jsonRequest = gson.toJson(lotIdDtoRequest2);
        serverResponse = server.getLotInformation(auctioneerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());

        //close the auction
        LotIdDtoRequest lotIdDtoRequest1 = new LotIdDtoRequest(lotID1);
        jsonRequest = gson.toJson(lotIdDtoRequest1);
        serverResponse = server.closeAuction(auctioneerToken, jsonRequest);
        assertEquals(200, serverResponse.getResponseCode());
    }
}