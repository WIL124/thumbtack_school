package net.thumbtack.school.database.mybatis.mappers;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Subject;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
public interface SubjectGroupMapper {
    @Select("SELECT id, name FROM subject WHERE id IN (SELECT subject_id FROM group_subject WHERE group_id = #{group.id})")
    List<Subject> getByGroup(@Param("group") Group group);
}
