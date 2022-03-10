package net.thumbtack.school.thread.ttschool;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TraineeMap {
    private Map<Trainee, String> map;

    public TraineeMap() {
        super();
        map = new HashMap<>();
    }

    public void addTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if (map.putIfAbsent(trainee,institute) != null) throw new TrainingException(TrainingErrorCode.DUPLICATE_TRAINEE);
    }

    public void replaceTraineeInfo(Trainee trainee, String institute) throws TrainingException {
        if (map.replace(trainee, institute) == null) throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public void removeTraineeInfo(Trainee trainee) throws TrainingException {
        if (map.remove(trainee) == null) throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
    }

    public int getTraineesCount() {
        return map.size();
    }

    public String getInstituteByTrainee(Trainee trainee) throws TrainingException {
        if (map.get(trainee) == null) throw new TrainingException(TrainingErrorCode.TRAINEE_NOT_FOUND);
        return map.get(trainee);
    }

    public Set<Trainee> getAllTrainees() {
        return map.keySet();
    }

    public Set<String> getAllInstitutes(){
        Set<String> set = new HashSet<>(map.values());
        return set;
    }
    public boolean isAnyFromInstitute(String institute){
        return this.map.containsValue(institute);
    }

}
