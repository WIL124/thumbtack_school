package net.thumbtack.school.model;

import lombok.Data;

@Data
public class Auctioneer extends User{
    private Auctioneer() {
        setLogin("auctioneer");
        setPassword("passWord123");
        setId(0);
        setName("Vasiliy");
        setSurname("Fedorov");
        setPatronymic("Petrovich");
    }
    public static final Auctioneer AUCTIONEER = new Auctioneer(){
    };
}
