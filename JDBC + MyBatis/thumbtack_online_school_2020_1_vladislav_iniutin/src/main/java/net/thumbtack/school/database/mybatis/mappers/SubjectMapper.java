package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Subject;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.type.JdbcType;

import java.util.List;

public interface SubjectMapper {
    @Insert("INSERT into subject (name) VALUES (#{subject.name})")
    @Options(useGeneratedKeys = true, keyProperty = "subject.id")
    Integer insert(@Param("subject") Subject subject);

    @Select("SELECT id, name FROM subject WHERE id = #{id}")
    @Results({
            @Result(property = "id", column = "id")
    })
    Subject getById(int id);

    @Select("SELECT id, name FROM subject")
    List<Subject> getAll();

    @Update("UPDATE subject SET name = #{subject.name}  WHERE id = #{subject.id}")
    void update(@Param("subject") Subject subject);

    @Delete("DELETE FROM subject WHERE id = #{subject.id}")
    void delete(@Param("subject") Subject subject);

    @Delete("DELETE FROM subject")
    void deleteAll();
}
