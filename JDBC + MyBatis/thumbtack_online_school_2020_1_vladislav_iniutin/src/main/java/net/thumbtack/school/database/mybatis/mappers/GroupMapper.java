package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.mapping.FetchType;

import java.util.List;

public interface GroupMapper {
    @Select("SELECT id, name, room FROM `group` WHERE school_id = #{school.id}")
    @Results({
            @Result(property = "id", column = "id"),
            @Result(property = "trainees", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.database.mybatis.mappers.TraineeMapper.getByGroup", fetchType = FetchType.LAZY)),
            @Result(property = "subjects", column = "id", javaType = List.class,
                    many = @Many(select = "net.thumbtack.school.database.mybatis.mappers.SubjectGroupMapper.getByGroup", fetchType = FetchType.LAZY))
    })

    @Delete("DELETE FROM group WHERE id = #{group.id}")
    void delete(@Param("group") Group group);

    @Insert("INSERT INTO `group` (id, name, room, school_id) VALUES (#{group.id}, #{group.name}, #{group.room}, #{school.id})")
    @Options(useGeneratedKeys = true, keyProperty = "group.id")
    Integer insert(@Param("school") School school, @Param("group") Group group);

    @Select("SELECT id, name, room, school_id FROM group")
    List<Group> getAll();

    @Update("UPDATE `group` SET id = #{group.id}, name = #{group.name}, room = #{group.room} WHERE id = #{group.id}")
    void update(@Param("group") Group group);

    @Insert("INSERT INTO group_subject VALUES (#{group.id}, #{subject.id})")
    void addSubjectToGroup(@Param("group") Group group, @Param("subject") Subject subject);

    @Update("UPDATE trainee set group_id = null where id = #{trainee.id}")
    void deleteTraineeFromGroup(@Param("trainee") Trainee trainee);

    @Update("UPDATE trainee SET group_id = #{group.id} WHERE trainee.id = #{trainee.id}")
    void moveTraineeToGroup(@Param("group") Group group, @Param("trainee") Trainee trainee);
}
