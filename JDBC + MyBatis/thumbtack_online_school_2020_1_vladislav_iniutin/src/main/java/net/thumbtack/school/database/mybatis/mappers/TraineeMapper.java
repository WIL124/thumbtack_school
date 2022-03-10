package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Trainee;
import org.apache.ibatis.annotations.*;

import java.util.List;

public interface TraineeMapper {
    @Insert("INSERT INTO trainee (firstName, lastName, rating, group_id) VALUES ( #{trainee.firstName}, #{trainee.lastName}, #{trainee.rating}, #{group.id} )")
    @Options(useGeneratedKeys = true, keyProperty = "trainee.id")
    Integer insert(@Param("group") Group group, @Param("trainee") Trainee trainee);

    @Select({"<script>",
            "SELECT id, firstName, lastName, rating, group_id FROM trainee",
            "<where>",
            "<if test='fName != null'> firstName like #{fName}",
            "</if>",
            "<if test='lName != null'> AND lastName like #{lName}",
            "</if>",
            "<if test='rating != null'> AND rating = #{rating}",
            "</if>",
            "</where>",
            "</script>"})
    @Results({
            @Result(property = "id", column = "id")
    })
    List<Trainee> getAllWithParams(@Param("fName") String fName, @Param("lName") String lName, @Param("rating") Integer rating);

    @Select("SELECT id, firstName, lastName, rating FROM trainee where id = #{id}")
    Trainee getById(@Param("id") int id);

    @Select("SELECT id, firstName, lastName, rating FROM trainee")
    List<Trainee> getAll();

    @Select("SELECT id, firstName, lastName, rating FROM trainee WHERE group_id = #{group.id}")
    List<Trainee> getByGroup(@Param("group") Group group);

    @Update("UPDATE trainee SET id = #{trainee.id}, firstName = #{trainee.firstName}, lastName = #{trainee.lastName}, rating = #{trainee.rating} where id = #{trainee.id}")
    void update(@Param("trainee") Trainee trainee);

    @Delete("DELETE FROM trainee where id = #{trainee.id}")
    void delete(@Param("trainee") Trainee trainee);

    @Delete("DELETE FROM trainee")
    void deleteAll();
}
