package net.thumbtack.school.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
@AllArgsConstructor
public class Lot {
    private int id;
    private String name;
    private String description;
    private List<String> category;
    private int startPrice;
    private int currentPrice;
    private int minPrice;
    private int maxPrice;
    private Seller seller;
    private boolean isAcceptBids;
    private boolean onSale;
    private Set<Bid> bids;

    public void sell() {
        onSale = false;
        isAcceptBids = false;
    }

    public void switchAccepting() {
        isAcceptBids = !isAcceptBids;
    }

    public void setCurrentPrice(int currentPrice) {
        this.currentPrice = currentPrice;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLotStatus(User user) {
        StringBuilder sb = new StringBuilder();
        TreeSet<Bid> set = (TreeSet<Bid>) this.getBids();
        Bid lastBid = null;
        if (!set.isEmpty()) {
            lastBid = set.last();
        }
        if (this.isOnSale()) {
            sb.append("Аукцион продолжается. ");
            if (this.isAcceptBids()) {
                sb.append("Идёт приём заявок на лот. ");
                if (lastBid != null) sb.append(getLastBidInfo(lastBid, user));
                else sb.append("Заявок на лот нет. ");
            } else sb.append("Приём заявок приостановлен. ");
        } else {
            sb.append("Аукцион завершён. ");
            if (lastBid != null)
            sb.append(getLotInfAboutSoldLot(lastBid, user));
        }
        return sb.toString();
    }
    static StringBuilder getLastBidInfo(Bid bid, User user) {
        StringBuilder str = new StringBuilder();
        if (bid == null) return str.append("Заявок на лот нет. ");
        else {
            if (bid.getBuyerId() == user.getId())
                str.append("Ваша заявка является текущей с ценой ").append(bid.getOffer()).append(" рублей");
            else str.append("Другим покупателем сделана заявка с ценой ").append(bid.getOffer()).append(" рублей");
        }
        return str;
    }

    static StringBuilder getLotInfAboutSoldLot(Bid lastBid, User user) {
        StringBuilder stringBuilder = new StringBuilder();
        if (lastBid.getBuyerId() == user.getId()) {
            stringBuilder.append("Вы получили лот по цене ").append(lastBid.getOffer()).append(" рублей");
        } else {
            stringBuilder.append("к сожалению лот достался другому покупателю по цене ").append(lastBid.getOffer()).append(" рублей");
        }
        return stringBuilder;
    }
}
