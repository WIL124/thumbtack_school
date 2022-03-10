package net.thumbtack.school.service;

import com.google.gson.Gson;
import net.thumbtack.school.daoimpl.LotDaoImpl;
import net.thumbtack.school.daoimpl.UserDaoImpl;
import net.thumbtack.school.dto.request.*;
import net.thumbtack.school.dto.response.*;
import net.thumbtack.school.interfaces.LotDao;
import net.thumbtack.school.interfaces.LotMapper;
import net.thumbtack.school.interfaces.UserDao;
import net.thumbtack.school.model.*;
import net.thumbtack.school.server.ErrorCode;
import net.thumbtack.school.server.ServerException;
import net.thumbtack.school.server.ServerResponse;

import java.util.*;

import static net.thumbtack.school.service.ServiceTools.*;


public class LotService {
    private static final Gson gson = new Gson();
    private final LotDao lotDao = new LotDaoImpl();
    private final UserDao userDao = new UserDaoImpl();

    public ServerResponse addLot(String token, String requestJsonString) {
        try {
            LotDtoRequest lotDtoRequest = fromJsonToClass(requestJsonString, LotDtoRequest.class);
            User user = userDao.getUserByToken(token);
            Seller seller = getSellerByUser(user);
            lotValidate(lotDtoRequest);
            Lot lot = LotMapper.INSTANCE.lotFromLotDto(lotDtoRequest);
            lotDao.addLot(seller, lot);
            return new ServerResponse(new LotIdDtoResponse(lot.getId()));
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse removeLot(String token, String requestJsonString) {
        try {
            User user = userDao.getUserByToken(token);
            Seller seller = getSellerByUser(user);
            LotIdDtoRequest lotIdDtoRequest = fromJsonToClass(requestJsonString, LotIdDtoRequest.class);
            int lotId = lotIdDtoRequest.getLotID();
            isNotNullValidate(lotId);
            Lot lot = lotDao.getLotByID(lotId);
            if (!seller.getUserLots().contains(lot)) throw new ServerException(ErrorCode.LOT_WRONG_OWNER);
            isSoldValidate(lot);
            lotDao.removeLot(lot);
            return new ServerResponse(new EmptyDtoResponse());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }


    public ServerResponse getLotInfo(String token, String requestJsonString) {
        try {
            User user = userDao.getUserByToken(token);
            if (isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            LotIdDtoRequest lotIdDtoRequest = fromJsonToClass(requestJsonString, LotIdDtoRequest.class);
            int lotId = lotIdDtoRequest.getLotID();
            isNotNullValidate(lotId);
            Lot lot = lotDao.getLotByID(lotId);
            String response = getInfo(lot, user);
            return new ServerResponse(response);
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse getAllLotsByCategory(String token, String requestJsonString) {
        try {
            User user = userDao.getUserByToken(token);
            if (isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            LotCategoryDtoRequest lotCategoryDtoRequest = fromJsonToClass(requestJsonString, LotCategoryDtoRequest.class);
            String category = lotCategoryDtoRequest.getCategory();
            List<String> registeredCategories = lotDao.getCategoriesList();
            if (!registeredCategories.contains(category))
                throw new ServerException(ErrorCode.LOT_WRONG_CATEGORY);
            Collection<Lot> allLots = lotDao.getAllLotsOfCategory(category);
            StringBuilder response = new StringBuilder();
            for (Lot lot : allLots) {
                if (lot != null)
                    response.append(getInfo(lot, user));
            }
            return new ServerResponse(response.toString());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse getAllLotsByCategoryUnion(String token, String requestJsonString) {
        try {
            User user = userDao.getUserByToken(token);
            if (isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            LotCategoriesDtoRequest lotCategoriesDtoRequest = fromJsonToClass(requestJsonString, LotCategoriesDtoRequest.class);
            String  [] categories = lotCategoriesDtoRequest.getCategories();
            List<String> registeredCategories = lotDao.getCategoriesList();
            if (!registeredCategories.containsAll(Arrays.asList(categories)))
                throw new ServerException(ErrorCode.LOT_WRONG_CATEGORY);
            Set<Lot> set = new HashSet<>();
            for (String category : categories) {
                Collection<Lot> response = lotDao.getAllLotsOfCategory(category);
                set.addAll(response);
            }
            StringBuilder response = new StringBuilder();
            for (Lot lot : set) {
                if (lot != null)
                    response.append(getInfo(lot, user));
            }
            return new ServerResponse(response.toString());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse getAllLotsByCategoryIntersection(String token, String requestJsonString) {
        try {
            User user = userDao.getUserByToken(token);
            if (isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            LotCategoriesDtoRequest lotCategoriesDtoRequest = fromJsonToClass(requestJsonString, LotCategoriesDtoRequest.class);
            String[] categories = lotCategoriesDtoRequest.getCategories();
            List<String> registeredCategories = lotDao.getCategoriesList();
            if (!registeredCategories.containsAll(Arrays.asList(categories)))
                throw new ServerException(ErrorCode.LOT_WRONG_CATEGORY);
            Set<Lot> set = new HashSet<>();
            for (String category1 : categories) {
                Collection<Lot> response = lotDao.getAllLotsOfCategory(category1);
                set.addAll(response);
            }
            for (String category2 : categories) {
                Collection<Lot> response = lotDao.getAllLotsOfCategory(category2);
                set.retainAll(response);
            }
            StringBuilder response = new StringBuilder();
            for (Lot lot : set) {
                if (lot != null)
                    response.append(getInfo(lot, user));
            }
            return new ServerResponse(response.toString());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse submitBid(String token, String requestJsonString) {
        try {
            BidDtoRequest bidDto = fromJsonToClass(requestJsonString, BidDtoRequest.class);
            isNotNullValidate(bidDto.getOffer());
            isNotNullValidate(bidDto.getLotID());
            User user = userDao.getUserByToken(token);
            if (!isBuyer(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            Lot lot = lotDao.getLotByID(bidDto.getLotID());
            isAcceptingBidsValidate(lot);
            isSoldValidate(lot);
            if (bidDto.getOffer() <= lot.getCurrentPrice()) throw new ServerException(ErrorCode.BID_WRONG_OFFER);
            Bid bid = new Bid(bidDto.getOffer(), bidDto.getLotID(), user.getId());
            ((Buyer) user).getBidSet().add(bid);
            lot.setCurrentPrice(bid.getOffer());
            lot.getBids().add(bid);
            if (bid.getOffer() >= lot.getMaxPrice()) {
                lot.sell();
            }
            return new ServerResponse(new EmptyDtoResponse());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse switchAcceptingBids(String token, String requestJsonString) {
        try {
            LotIdDtoRequest lotIdDtoRequest = fromJsonToClass(requestJsonString, LotIdDtoRequest.class);
            isNotNullValidate(lotIdDtoRequest.getLotID());
            User user = userDao.getUserByToken(token);
            if (!isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            Lot lot = lotDao.getLotByID(lotIdDtoRequest.getLotID());
            isSoldValidate(lot);
            if (((Seller) user).getUserLots().contains(lot)) {
                lot.switchAccepting();
            } else {
                throw new ServerException(ErrorCode.LOT_WRONG_OWNER);
            }
            return new ServerResponse(new EmptyDtoResponse());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    public ServerResponse closeAuction(String token, String requestJsonString) {
        try {
            LotIdDtoRequest LotIdDto = fromJsonToClass(requestJsonString, LotIdDtoRequest.class);
            isNotNullValidate(LotIdDto.getLotID());
            User user = userDao.getUserByToken(token);
            Lot lot = lotDao.getLotByID(LotIdDto.getLotID());
            if ((lot.getSeller() == user || !isAuctioneer(user))) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
            isSoldValidate(lot);
            if (lot.getMinPrice() > lot.getCurrentPrice())
                lot.sell();
            else {                                     // Если аукцион не состоялся, лот удаляется
                lotDao.removeLot(lot);
            }
            return new ServerResponse(new EmptyDtoResponse());
        } catch (ServerException ex) {
            return new ServerResponse(ex);
        }
    }

    private String getInfo(Lot lot, User user) throws ServerException {
        if (isAuctioneer(user)) {
            LotFullInfoDtoResponse lotInfo = LotMapper.INSTANCE.lotFullInfDtoFromLot(lot);
            lotInfo.setStatus(lot.getLotStatus(user));
            return gson.toJson(lotInfo);
        } else {
            LotInformationDtoResponse lotInfo = LotMapper.INSTANCE.lotInfDtoFromLot(lot);
            lotInfo.setStatus(lot.getLotStatus(user));
            return gson.toJson(lotInfo);
        }
    }

    private static Seller getSellerByUser(User user) throws ServerException {
        if (!isSeller(user)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
        return (Seller) user;
    }
    private static void isSoldValidate(Lot lot) throws ServerException {
        if(!lot.isOnSale()) throw new ServerException(ErrorCode.LOT_IS_ALREADY_SOLD);
    }
    private static void isAcceptingBidsValidate(Lot lot) throws ServerException {
        if(!lot.isAcceptBids()) throw new ServerException(ErrorCode.LOT_NOT_ACCEPTING_BIDS);
    }
}
