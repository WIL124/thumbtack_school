package net.thumbtack.school.interfaces;

import net.thumbtack.school.dto.request.LotDtoRequest;
import net.thumbtack.school.dto.response.LotFullInfoDtoResponse;
import net.thumbtack.school.dto.response.LotInformationDtoResponse;
import net.thumbtack.school.model.Lot;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.TreeSet;

@Mapper(imports = {TreeSet.class, Comparator.class})
public interface LotMapper {

        LotMapper INSTANCE = Mappers.getMapper(LotMapper.class);
        @Mapping(target = "bids", expression = "java(new TreeSet<>(new Comparator<>() {@Override public int compare(Bid o1, Bid o2) {return o1.getOffer().compareTo(o2.getOffer());}}))")
        @Mapping(target = "currentPrice", source = "startPrice")
        @Mapping(target = "isAcceptBids", constant = "true")
        @Mapping(target = "onSale", constant = "true")
        Lot lotFromLotDto(LotDtoRequest lotDtoRequest);
        LotInformationDtoResponse lotInfDtoFromLot(Lot lot);
        @Mapping(target = "ownerId", source = "seller.id")
        LotFullInfoDtoResponse lotFullInfDtoFromLot(Lot lot);
}
