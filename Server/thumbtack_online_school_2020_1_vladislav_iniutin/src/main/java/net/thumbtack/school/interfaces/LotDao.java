package net.thumbtack.school.interfaces;

import net.thumbtack.school.model.Lot;
import net.thumbtack.school.model.Seller;
import net.thumbtack.school.server.ServerException;

import java.util.Collection;
import java.util.List;

public interface LotDao {
    void addLot(Seller seller, Lot lot) throws ServerException;

    void removeLot(Lot lot) throws ServerException;

    Lot getLotByID(int lotID) throws ServerException;

    List<String> getCategoriesList()throws ServerException;

    Collection<Lot> getAllLotsOfCategory(String category);
}
