package net.thumbtack.school.thread.ttschool;
import javax.management.Query;
import java.util.LinkedList;
import java.util.Queue;

public class TraineeQueue {
    private Queue<Trainee> queue;

    public TraineeQueue(){
        super();
        queue = new LinkedList<>();
    }
    public void addTrainee(Trainee trainee){
        queue.add(trainee);
    }
    public Trainee removeTrainee() throws TrainingException {
        if (queue.size() == 0) throw new TrainingException(TrainingErrorCode.EMPTY_TRAINEE_QUEUE);
        return queue.remove();
    }
    public boolean isEmpty(){
        return queue.isEmpty();
    }
}
