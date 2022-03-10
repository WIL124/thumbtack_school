package net.thumbtack.school.store.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.thumbtack.school.store.models.Education;
import net.thumbtack.school.store.models.Sex;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    @Pattern(regexp = "[1][8-9][0-9][0-9]|[2][0][0-1][0-9]|[2][0][2][0-2]", //dig between 1800-2022
            message = "Incorrect year. Year must be between 1800 and 2022")
    @NotNull (message = "year is a mandatory parameter")
    private  String year;
    @NotNull (message = "sex is a mandatory parameter")
    private  Sex sex;
    @Pattern(regexp = "^\\d{16}$", message = "Incorrect card number. Must be 16 digits")
    @NotNull (message = "cardNumber is a mandatory parameter")
    private  String cardNumber;
    @NotNull(message = "The field hasChildren is required")
    private  String hasChildren;
    @NotNull(message = "The field isMarried is required")
    private  String isMarried;
    @NotNull(message = "Education must match the value UNIVERSITY, COLLEGE or SCHOOL")
    private  Education education;
    @NotBlank(message = "Enter the city")
    private  String city;
}
