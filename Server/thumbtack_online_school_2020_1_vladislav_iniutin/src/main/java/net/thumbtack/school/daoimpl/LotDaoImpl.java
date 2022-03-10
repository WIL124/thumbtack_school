package net.thumbtack.school.daoimpl;

import net.thumbtack.school.database.Database;
import net.thumbtack.school.interfaces.LotDao;
import net.thumbtack.school.model.Lot;
import net.thumbtack.school.model.Seller;
import net.thumbtack.school.server.ServerException;

import java.util.Collection;
import java.util.List;

public class LotDaoImpl implements LotDao {
    @Override
    public void addLot(Seller seller, Lot lot) throws ServerException {
        Database.DATABASE.addLot(seller, lot);
    }

    @Override
    public void removeLot(Lot lot) throws ServerException {
        Database.DATABASE.removeLot(lot);
    }

    @Override
    public Lot getLotByID(int lotID) throws ServerException {
        return Database.DATABASE.getLotById(lotID);
    }

    @Override
    public List<String> getCategoriesList() {
        return Database.DATABASE.getCategoriesList();
    }

    @Override
    public Collection<Lot> getAllLotsOfCategory(String category) {
        return Database.DATABASE.getAllLotsOfCategory(category);
    }
}
