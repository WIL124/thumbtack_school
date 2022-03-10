package net.thumbtack.school.service;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.thumbtack.school.daoimpl.LotDaoImpl;
import net.thumbtack.school.dto.request.LotDtoRequest;
import net.thumbtack.school.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.interfaces.LotDao;
import net.thumbtack.school.server.ErrorCode;
import net.thumbtack.school.model.*;
import net.thumbtack.school.server.ServerException;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;

public class ServiceTools {


    static <T> T fromJsonToClass(String jsonString, Type c) throws ServerException {
        try {
            Gson gson = new Gson();
            return gson.fromJson(jsonString, c);
        } catch (JsonSyntaxException ex) {
            throw new ServerException(ErrorCode.WRONG_JSON);
        }
    }

    static void userValidate(RegisterUserDtoRequest request) throws ServerException {
        if (request.getName() == null || request.getName().length() == 0)
            throw new ServerException(ErrorCode.USER_WRONG_NAME);
        if (request.getSurname() == null || request.getSurname().length() == 0)
            throw new ServerException(ErrorCode.USER_WRONG_SURNAME);
        if (request.getLogin() == null || request.getLogin().length() < 5)
            throw new ServerException(ErrorCode.USER_WRONG_LOGIN);
        if (request.getPassword() == null || request.getPassword().length() < 5) {
            throw new ServerException(ErrorCode.USER_WRONG_PASSWORD);
        }
        if (request.getPassword().equals(request.getPassword().toLowerCase()) || request.getPassword().equals(request.getPassword().toUpperCase())) {
            throw new ServerException(ErrorCode.USER_WRONG_PASSWORD);
        }
        if (!request.getStatus().equals("seller") && !request.getStatus().equals("buyer"))
            throw new ServerException(ErrorCode.USER_WRONG_STATUS);
    }

    static boolean isNull(String str) {
        return str == null || str.length() == 0;
    }

    static boolean isSeller(User user) {
        return user.getClass().equals(Seller.class);
    }

    static boolean isBuyer(User user) {
        return user.getClass().equals(Buyer.class);
    }

    static boolean isAuctioneer(User user) {
        return Auctioneer.AUCTIONEER == user;
    }

    static void lotValidate(LotDtoRequest request) throws ServerException {
        if (request.getName() == null || request.getName().length() == 0)
            throw new ServerException(ErrorCode.LOT_WRONG_NAME);
        if (request.getCategory() == null)
            throw new ServerException(ErrorCode.LOT_WRONG_CATEGORY);
        if (request.getStartPrice() <= 0 || request.getMinPrice() <= 0 || request.getMaxPrice() <= request.getMinPrice() || request.getMaxPrice() <= request.getStartPrice()) {
            throw new ServerException(ErrorCode.LOT_WRONG_PRICE);
        }
        LotDao lotDao = new LotDaoImpl();
        List<String> categories = lotDao.getCategoriesList();
        if(!categories.containsAll(Arrays.asList(request.getCategory()))) throw new ServerException(ErrorCode.LOT_WRONG_CATEGORY);
    }

    static void isNotNullValidate(Integer i) throws ServerException {
        if (i == null)
            throw new ServerException(ErrorCode.NULL_OR_EMPTY_FIELD);
    }
}
