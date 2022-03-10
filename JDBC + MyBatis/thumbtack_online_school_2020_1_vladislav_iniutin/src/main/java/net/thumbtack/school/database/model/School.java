package net.thumbtack.school.database.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class School {
    private int id;
    private String name;
    private int year;
    private List<Group> groups;

    public School(int id, String name, int year) {
        this(id, name, year, new ArrayList<>());
    }

    public School(String name, int year) {
        this(0, name, year);
    }

    public void addGroup(Group group) {
        groups.add(group);
    }

    public void removeGroup(Group group) {
        groups.remove(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof School)) return false;
        School school = (School) o;
        return getId() == school.getId() && getYear() == school.getYear() && Objects.equals(getName(), school.getName()) && Objects.equals(getGroups(), school.getGroups());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getYear(), getGroups());
    }
}
