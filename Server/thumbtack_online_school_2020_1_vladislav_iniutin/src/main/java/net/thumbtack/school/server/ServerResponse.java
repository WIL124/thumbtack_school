package net.thumbtack.school.server;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.thumbtack.school.dto.response.DtoResponseBase;

@Getter
@AllArgsConstructor
public class ServerResponse {

    private static final int CODE_ERROR = 400;
    private static final int CODE_SUCCESS = 200;

    private int responseCode;
    private String responseData;
    public ServerResponse(DtoResponseBase dto){
        responseCode = CODE_SUCCESS;
        responseData = new Gson().toJson(dto);
    }
    public ServerResponse(ServerException ex){
        responseCode = CODE_ERROR;
        responseData = new Gson().toJson(ex.getErrorCode());
    }
    public ServerResponse(String jsonString){
        responseCode = CODE_SUCCESS;
        responseData = jsonString;
    }
}
