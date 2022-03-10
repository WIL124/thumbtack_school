package net.thumbtack.school.thread.ttschool;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class School {
    private Set<Group> groups;
    private String name;
    private int year;

    public School(String name, int year) throws TrainingException {
        super();
        groups = Collections.synchronizedSet(new HashSet<>());
        setName(name);
        setYear(year);
    }

    public String getName() {
        return name;
    }

    public synchronized void setName(String name) throws TrainingException {
        if (name == null || name.length() == 0) throw new TrainingException(TrainingErrorCode.SCHOOL_WRONG_NAME);
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public synchronized void setYear(int year) {
        this.year = year;
    }

    public Set<Group> getGroups() {
        return groups;
    }

    public synchronized void addGroup(Group group) throws TrainingException {
        for (Group group1 : groups) {
            if (group1.getName().equals(group.getName()))
                throw new TrainingException(TrainingErrorCode.DUPLICATE_GROUP_NAME);
        }
        groups.add(group);
    }

    public synchronized void removeGroup(Group group) throws TrainingException {
        if (!this.containsGroup(group)) throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
        groups.remove(group);
    }

    public synchronized void removeGroup(String name) throws TrainingException {
        if (!groups.removeIf(group -> group.getName().equals(name)))
            throw new TrainingException(TrainingErrorCode.GROUP_NOT_FOUND);
    }

    public boolean containsGroup(Group group) {
        return groups.contains(group);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        School school = (School) o;
        return year == school.year && Objects.equals(groups, school.groups) && Objects.equals(name, school.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(groups, name, year);
    }
}
