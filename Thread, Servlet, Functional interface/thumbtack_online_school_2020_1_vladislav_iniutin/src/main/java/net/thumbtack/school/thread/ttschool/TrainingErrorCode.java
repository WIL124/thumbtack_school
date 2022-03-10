package net.thumbtack.school.thread.ttschool;
public enum TrainingErrorCode {
    TRAINEE_WRONG_FIRSTNAME("Неверное имя"),
    TRAINEE_WRONG_LASTNAME("Неверная фамилия"),
    TRAINEE_WRONG_RATING("Оценка должна быть от 1 до 5"),
    TRAINEE_NOT_FOUND("Ученик не найден"),
    GROUP_WRONG_NAME("Имя группы не может быть пустым"),
    GROUP_WRONG_ROOM("Имя аудитории не может быть пустым"),
    GROUP_NOT_FOUND("Группа не найдена"),
    DUPLICATE_GROUP_NAME("Группа с таким именем уже существует"),
    DUPLICATE_TRAINEE("Такой ученик уже существует"),
    EMPTY_TRAINEE_QUEUE("В очереди никого нет"),
    SCHOOL_WRONG_NAME("Имя школы не может быть пустым");
    private String errorString;

    TrainingErrorCode(String errorString){
        this.errorString = errorString;
    }
    public String getErrorString(){
        return errorString;
    }
}
