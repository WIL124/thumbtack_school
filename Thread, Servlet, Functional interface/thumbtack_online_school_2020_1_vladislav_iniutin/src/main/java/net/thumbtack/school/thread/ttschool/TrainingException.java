package net.thumbtack.school.thread.ttschool;
public class TrainingException extends Exception{
    private TrainingErrorCode trainingErrorCode;

    public TrainingException(TrainingErrorCode errorCode){
        super(errorCode.getErrorString());
        this.trainingErrorCode = errorCode;
    }

    public TrainingErrorCode getErrorCode() {
        return trainingErrorCode;
    }
}
