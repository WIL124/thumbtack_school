package net.thumbtack.school.database.jdbc;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static net.thumbtack.school.database.jdbc.JdbcUtils.getConnection;

public class JdbcService {

    public static void insertTrainee(Trainee trainee) throws SQLException {
        String query = "insert into trainee values(?,?,?,?, null)";
        try (PreparedStatement stmt = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setNull(1, java.sql.Types.INTEGER);
            stmt.setString(2, trainee.getFirstName());
            stmt.setString(3, trainee.getLastName());
            stmt.setInt(4, trainee.getRating());
            stmt.executeUpdate();
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    trainee.setId(generatedKeys.getInt(1));
                } else {
                    throw new SQLException("Creating trainee failed, no id obtained.");
                }
            }
        }
    }

    public static void updateTrainee(Trainee trainee) throws SQLException {
        String query = "update trainee set firstName = ?, lastName = ?, rating = ? where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(query)) {
            stmt.setString(1, trainee.getFirstName());
            stmt.setString(2, trainee.getLastName());
            stmt.setInt(3, trainee.getRating());
            stmt.setInt(4, trainee.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteTrainee(Trainee trainee) throws SQLException {
        String deleteQuery = "delete from trainee where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(deleteQuery)) {
            stmt.setInt(1, trainee.getId());
            stmt.executeUpdate();
        }
    }

    public static void deleteTrainees() throws SQLException {
        String deleteQuery = "delete from trainee";
        try (PreparedStatement stmt = getConnection().prepareStatement(deleteQuery)) {
            stmt.executeUpdate();
        }
    }

    public static Trainee getTraineeByIdUsingColNames(int traineeId) throws SQLException {
        String selectQuery = "select * from trainee where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, traineeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int rating = rs.getInt("rating");
                return new Trainee(traineeId, firstName, lastName, rating);
            } else return null;
        }
    }

    public static Trainee getTraineeByIdUsingColNumbers(int traineeId) throws SQLException {
        String selectQuery = "select * from trainee where id = ?";
        try (PreparedStatement stmt = getConnection().prepareStatement(selectQuery)) {
            stmt.setInt(1, traineeId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                int rating = rs.getInt(4);
                return new Trainee(traineeId, firstName, lastName, rating);
            } else return null;
        }
    }

    public static List<Trainee> getTraineesUsingColNames() throws SQLException {
        String selectQuery = "select * from trainee";
        List<Trainee> list = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(selectQuery)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                int rating = rs.getInt("rating");
                list.add(new Trainee(id, firstName, lastName, rating));
            }
            return list;
        }
    }

    public static List<Trainee> getTraineesUsingColNumbers() throws SQLException {
        String selectQuery = "select * from trainee";
        List<Trainee> list = new ArrayList<>();
        try (PreparedStatement stmt = getConnection().prepareStatement(selectQuery)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt(1);
                String firstName = rs.getString(2);
                String lastName = rs.getString(3);
                int rating = rs.getInt(4);
                list.add(new Trainee(id, firstName, lastName, rating));
            }
            return list;
        }
    }

    public static void insertSubject(Subject subject) throws SQLException {
        String query = "insert into subject values(?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, subject.getId());
            statement.setString(2, subject.getName());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    subject.setId(rs.getInt(1));
                } else {
                    throw new SQLException("Creating subject failed, no id obtained.");
                }
            }
        }
    }

    public static Subject getSubjectByIdUsingColNames(int subjectId) throws SQLException {
        String query = "Select * from subject where id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, subjectId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString("name");
            return new Subject(subjectId, name);
        }
    }

    public static Subject getSubjectByIdUsingColNumbers(int subjectId) throws SQLException {
        String query = "Select * from subject where id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, subjectId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString(2);
            return new Subject(subjectId, name);
        }
    }

    public static void deleteSubjects() throws SQLException {
        String query = "Delete from subject";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    public static void insertSchool(School school) throws SQLException {
        String query = "Insert into school values (?,?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, 0);
            statement.setString(2, school.getName());
            statement.setInt(3, school.getYear());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    school.setId(rs.getInt(1));
                } else throw new SQLException("Creating school failed, no id obtained.");
            }
        }
    }

    public static School getSchoolByIdUsingColNames(int schoolId) throws SQLException {
        String query = "SELECT * from school where id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, schoolId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString("name");
            int year = rs.getInt("year");
            return new School(schoolId, name, year);
        }
    }

    public static School getSchoolByIdUsingColNumbers(int schoolId) throws SQLException {
        String query = "SELECT * from school where id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, schoolId);
            ResultSet rs = statement.executeQuery();
            rs.next();
            String name = rs.getString(2);
            int year = rs.getInt(3);
            return new School(schoolId, name, year);
        }
    }

    public static void deleteSchools() throws SQLException {
        String query = "delete from school";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.executeUpdate();
        }
    }

    public static void insertGroup(School school, Group group) throws SQLException {
        String query = "Insert into `group` values(?,?,?,?)";
        try (PreparedStatement statement = getConnection().prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, group.getId());
            statement.setString(2, group.getName());
            statement.setString(3, group.getRoom());
            statement.setInt(4, school.getId());
            statement.executeUpdate();
            try (ResultSet rs = statement.getGeneratedKeys()) {
                if (rs.next()) {
                    group.setId(rs.getInt(1));
                } else throw new SQLException("Creating school failed, no id obtained.");
            }
        }
    }

    public static School getSchoolByIdWithGroups(int id) throws SQLException {
        String query = "SELECT group.id, group.name , room, school.name, school.year FROM `group` INNER JOIN school ON school.id = ?";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            List<Group> groupList = new ArrayList<>();
            String schoolName = null;
            int year = 0;
            while (rs.next()) {
                if (schoolName == null) {
                    schoolName = rs.getString("school.name");
                    year = rs.getInt("school.year");
                }
                int groupId = rs.getInt("group.id");
                String groupName = rs.getString("group.name");
                String room = rs.getString("room");
                groupList.add(new Group(groupId, groupName, room));
            }
            return new School(id, schoolName, year, groupList);
        }
    }

    public static List<School> getSchoolsWithGroups() throws SQLException {
        String query = "SELECT school.id, school.name, year, group.id, group.name, group.room FROM school JOIN `group` ON school.id = school_id";
        try (PreparedStatement statement = getConnection().prepareStatement(query)) {
            ResultSet rs = statement.executeQuery();
            List<School> schoolList = new ArrayList<>();
            School school = null;
            int id = -1;
            while (rs.next()) {
                if (rs.getInt("school.id") != id) {
                    id = rs.getInt("school.id");
                    String schoolName = rs.getString("school.name");
                    int year = rs.getInt("school.year");
                    school = new School(id, schoolName, year);
                    schoolList.add(school);
                    school.setGroups(new ArrayList<>());
                }
                int groupId = rs.getInt("group.id");
                String groupName = rs.getString("group.name");
                String room = rs.getString("room");
                school.getGroups().add(new Group(groupId, groupName, room));

            }
            return schoolList;
        }
    }
}
