package net.thumbtack.school.interfaces;

import net.thumbtack.school.dto.request.RegisterUserDtoRequest;
import net.thumbtack.school.model.Buyer;
import net.thumbtack.school.model.Seller;
import net.thumbtack.school.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    default User userFromUserDto(RegisterUserDtoRequest registerUserDtoRequest){
        if(registerUserDtoRequest.getStatus().equalsIgnoreCase("seller")){
            return new Seller(registerUserDtoRequest.getName(), registerUserDtoRequest.getSurname(), registerUserDtoRequest.getPatronymic(), registerUserDtoRequest.getLogin(), registerUserDtoRequest.getPassword());
        }
        else {
            return new Buyer(registerUserDtoRequest.getName(), registerUserDtoRequest.getSurname(), registerUserDtoRequest.getPatronymic(), registerUserDtoRequest.getLogin(), registerUserDtoRequest.getPassword());
        }
    }
}
