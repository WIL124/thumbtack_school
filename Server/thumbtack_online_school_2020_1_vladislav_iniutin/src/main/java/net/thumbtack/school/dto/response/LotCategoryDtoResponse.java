package net.thumbtack.school.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LotCategoryDtoResponse extends DtoResponseBase {
    private LotInformationDtoResponse[] lotInformationDtoResponses;
}
