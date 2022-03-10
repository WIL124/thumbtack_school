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
public class Group {
    private int id;
    private String name;
    private String room;
    private List<Trainee> trainees;
    private List<Subject> subjects;

    public Group(int id, String name, String room) {
        this(id, name, room, new ArrayList<>(), new ArrayList<>());
    }

    public Group(String name, String room) {
        this(0, name, room);
    }

    public void addTrainee(Trainee trainee) {
        trainees.add(trainee);
    }

    public void removeTrainee(Trainee trainee) {
        trainees.remove(trainee);
    }

    public void addSubject(Subject subject) {
        subjects.add(subject);
    }

    public void removeSubject(Subject subject) {
        subjects.remove(subject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Group)) return false;
        Group group = (Group) o;
        return getId() == group.getId() && Objects.equals(getName(), group.getName()) && Objects.equals(getRoom(), group.getRoom()) && Objects.equals(getTrainees(), group.getTrainees()) && Objects.equals(getSubjects(), group.getSubjects());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getRoom(), getTrainees(), getSubjects());
    }
}
