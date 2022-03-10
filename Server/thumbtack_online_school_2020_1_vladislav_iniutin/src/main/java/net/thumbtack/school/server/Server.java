package net.thumbtack.school.server;

import net.thumbtack.school.service.LotService;
import net.thumbtack.school.service.UserService;


public class Server {
    private final LotService lotService;
    private final UserService userService;

    public Server() {
        userService = new UserService();
        lotService = new LotService();
    }

    //////////////////////////////////for User////////////////////////////////////////////////////////

    public ServerResponse registerUser(String requestJsonString) {
        return userService.registerUser(requestJsonString);
    }

    public ServerResponse loginUser(String requestJsonString) {
        return userService.loginUser(requestJsonString);
    }

    public ServerResponse logoutUser(String requestJsonString) {
        return userService.logoutUser(requestJsonString);
    }


    //////////////////////////////////for Seller////////////////////////////////////////////////////////
    public ServerResponse addLot(String token, String requestJsonString) {
        return lotService.addLot(token, requestJsonString);
    }

    public ServerResponse removeLot(String token, String requestJsonString) {
        return lotService.removeLot(token, requestJsonString);
    }

    public ServerResponse stopAcceptingBids(String token, String requestJsonString) {
        return lotService.switchAcceptingBids(token, requestJsonString);
    }


    //////////////////////////////////for Buyer////////////////////////////////////////////////////////
    public ServerResponse getLotInformation(String token, String requestJsonString) {
        return lotService.getLotInfo(token, requestJsonString);
    }

    public ServerResponse getAllLotsByCategory(String token, String requestJsonString) {
        return lotService.getAllLotsByCategory(token, requestJsonString);
    }

    public ServerResponse getAllLotsByCategoryIntersection(String token, String requestJsonString) {
        return lotService.getAllLotsByCategoryIntersection(token, requestJsonString);
    }

    public ServerResponse getAllLotsByCategoryUnion(String token, String requestJsonString) {
        return lotService.getAllLotsByCategoryUnion(token, requestJsonString);
    }

    public ServerResponse submitBid(String token, String requestJsonString) {
        return lotService.submitBid(token, requestJsonString);
    }

    //////////////////////////////////for Auctioneer////////////////////////////////////////////////////////
    public ServerResponse closeAuction(String token, String requestJsonString) {
        return lotService.closeAuction(token, requestJsonString);
    }
}
