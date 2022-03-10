package net.thumbtack.school.database;

import net.thumbtack.school.server.ErrorCode;
import net.thumbtack.school.model.Auctioneer;
import net.thumbtack.school.model.Lot;
import net.thumbtack.school.model.Seller;
import net.thumbtack.school.server.ServerException;
import net.thumbtack.school.model.User;
import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.apache.commons.collections4.multimap.ArrayListValuedHashMap;

import java.util.*;

import static net.thumbtack.school.database.ReadCategoriesFromFile.read;

public class Database {
    public static final Database DATABASE = new Database();
    private final BidiMap<String, User> tokenUserMap = new DualHashBidiMap<>(new HashMap<>());
    private final Map<Integer, Lot> idLotMap = new HashMap<>();
    private final Map<String, User> registeredUsers = new HashMap<>(Collections.singletonMap(Auctioneer.AUCTIONEER.getLogin(), Auctioneer.AUCTIONEER));
    private final Map<Integer, User> usersID = new HashMap<>(Collections.singletonMap(Auctioneer.AUCTIONEER.getId(), Auctioneer.AUCTIONEER));
    private final MultiValuedMap<String, Lot> lotByCategories = new ArrayListValuedHashMap<>(read());
    private Integer maxLotID = 1;

    public User getUserByToken(String userToken) throws ServerException {
        User user = tokenUserMap.get(userToken);
        if (user == null) {
            throw new ServerException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public User getUserById(int id) throws ServerException {
        User user = usersID.get(id);
        if (user == null) {
            throw new ServerException(ErrorCode.USER_NOT_FOUND);
        }
        return user;
    }

    public Collection<Lot> getAllLotsOfCategory(String category) {
        return lotByCategories.get(category);
    }

    public List<String> getCategoriesList() {
        return new ArrayList<>(lotByCategories.keySet());
    }

    public void registerUser(User user) throws ServerException {
        if (registeredUsers.putIfAbsent(user.getLogin(), user) != null) {
            throw new ServerException(ErrorCode.USER_IS_ALREADY_REGISTERED);
        }
        user.setId(registeredUsers.size());
        usersID.put(user.getId(), user);
    }

    public String loginUser(String login, String password) throws ServerException {
        User user = registeredUsers.get(login);
        if (user == null) {
            throw new ServerException(ErrorCode.USER_WRONG_LOGIN);
        }
        if (!user.getPassword().equals(password)) throw new ServerException(ErrorCode.USER_WRONG_PASSWORD);
        tokenUserMap.inverseBidiMap().remove(user);
        String token = UUID.randomUUID().toString();
        tokenUserMap.put(token, user);
        return token;

    }

    public void logoutUser(String token) throws ServerException {
        if (tokenUserMap.remove(token) == null) throw new ServerException(ErrorCode.USER_NOT_FOUND);
    }

    public void addLot(Seller seller, Lot lot) {
        Collection<Lot> category;
        for (String categoryName : lot.getCategory()) {
            category = lotByCategories.get(categoryName);
            category.add(lot);
            lot.setId(maxLotID);
            lot.setSeller(seller);
            idLotMap.put(maxLotID++, lot);
        }
        seller.getUserLots().add(lot);
    }

    public void removeLot(Lot lot) {
        Seller seller = lot.getSeller();
        idLotMap.remove(lot.getId());
        seller.getUserLots().remove(lot);
        for (String categoryName : lot.getCategory()) {
            lotByCategories.get(categoryName).remove(lot);
        }

    }

    public Lot getLotById(int lotID) throws ServerException {
        if (idLotMap.get(lotID) == null) throw new ServerException(ErrorCode.LOT_NOT_FOUND);
        return idLotMap.get(lotID);
    }

    public void isSellerValidate(User user) throws ServerException {
        if (!user.getClass().equals(Seller.class)) throw new ServerException(ErrorCode.USER_WRONG_STATUS);
    }
}
