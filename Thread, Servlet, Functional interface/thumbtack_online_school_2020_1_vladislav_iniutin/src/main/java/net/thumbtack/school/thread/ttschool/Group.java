package net.thumbtack.school.thread.ttschool;

import java.util.*;
import java.util.stream.Collectors;

public class Group {
    private String name;
    private String room;
    private List<Trainee> groupList;

    public Group(String name, String room) throws TrainingException {
        super();
        groupList = Collections.synchronizedList(new ArrayList<>());
        setName(name);
        setRoom(room);
    }

    public String getName() {
        return name;
    }

    public synchronized void setName(String name) throws TrainingException {
        if (name == null || name.length() == 0) throw new TrainingException(TrainingErrorCode.GROUP_WRONG_NAME);
        this.name = name;
    }

    public String getRoom() {
        return room;
    }

    public synchronized void setRoom(String room) throws TrainingException {
        if (room == null || room.length() == 0) throw new TrainingException(TrainingErrorCode.GROUP_WRONG_ROOM);
        this.room = room;
    }

    public List<Trainee> getTrainees() {
        return groupList;
    }

    public synchronized void addTrainee(Trainee trainee) {
        groupList.add(trainee);
    }

    public synchronized void removeTrainee(Trainee trainee) throws TrainingException {
        if (!groupList.remove(trainee)) throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public synchronized void removeTrainee(int index) throws TrainingException {
        if (index >= 0 && index < groupList.size()) groupList.remove(index);
        else throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public Trainee getTraineeByFirstName(String firstName) throws TrainingException {
        for (Trainee trainee : groupList) {
            if (trainee.getFirstName().equals(firstName)) return trainee;
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public Trainee getTraineeByFullName(String fullName) throws TrainingException {
        for (Trainee trainee : groupList) {
            if (trainee.getFullName().equals(fullName)) return trainee;
        }
        throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public synchronized void sortTraineeListByFirstNameAscendant() {
        groupList.sort(Comparator.comparing(Trainee::getFirstName));
    }

    public synchronized void sortTraineeListByRatingDescendant() {
        groupList.sort((t1, t2) -> -Integer.compare(t1.getRating(), t2.getRating()));
    }

    public synchronized void reverseTraineeList() {
        Collections.reverse(groupList);
    }

    public synchronized void rotateTraineeList(int positions) {
        Collections.rotate(groupList, positions);
    }

    public List<Trainee> getTraineesWithMaxRating() throws TrainingException {
        if (groupList.size() == 0) {
            throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        }
        int maxRating = Collections.max(groupList, Comparator.comparing(Trainee::getRating)).getRating();
        return groupList.stream().filter(trainee -> trainee.getRating() == maxRating).collect(Collectors.toList());
    }

    public boolean hasDuplicates() {
        Set<Trainee> trainees = new HashSet<>(groupList);
        return groupList.size() != trainees.size();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return name.equals(group.name) && room.equals(group.room) && Objects.equals(groupList, group.groupList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, room, groupList);
    }
}
