package net.thumbtack.school.database.mybatis.daoimpl;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.Trainee;
import net.thumbtack.school.database.mybatis.dao.TraineeDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class TraineeDaoImpl extends DaoImplBase implements TraineeDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(TraineeDaoImpl.class);

    @Override
    public Trainee insert(Group group, Trainee trainee) {
        LOGGER.debug("DAO insert trainee {}", trainee);
        try (SqlSession sqlSession = getSession()) {
            try {
                getTraineeMapper(sqlSession).insert(group, trainee);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert Trainee {} {}", trainee, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
            return trainee;
        }
    }

    @Override
    public Trainee getById(int id) {
        LOGGER.debug("DAO get trainee by id {}", id);
        try (SqlSession sqlSession = getSession()) {
            return getTraineeMapper(sqlSession).getById(id);
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get trainee by id {} {}", id, ex);
            throw ex;
        }
    }

    @Override
    public List<Trainee> getAll() {
        LOGGER.debug("DAO getAll trainees");
        try (SqlSession sqlSession = getSession()) {
            return getTraineeMapper(sqlSession).getAll();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get all trainees {}", ex);
            throw ex;
        }
    }

    @Override
    public Trainee update(Trainee trainee) {
        LOGGER.debug("DAO update trainee {}", trainee);
        try (SqlSession sqlSession = getSession()) {
            try {
                getTraineeMapper(sqlSession).update(trainee);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update trainee {} {}", trainee, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
            return trainee;
        }
    }

    @Override
    public List<Trainee> getAllWithParams(String firstName, String lastName, Integer rating) {
        LOGGER.debug("DAO get all trainees with params: firstname = {}, lastname = {}, rating = {}", firstName, lastName ,rating);
        try(SqlSession sqlSession = getSession()) {
           List<Trainee> list = getTraineeMapper(sqlSession).getAllWithParams(firstName,lastName,rating);
           return list;
        }catch (RuntimeException ex){
            LOGGER.info("Can't get trainee list with params firstname = {}, lastname = {}, rating = {} {}", firstName, lastName ,rating, ex);
            throw ex;
        }
    }

    @Override
    public void batchInsert(List<Trainee> trainees) {
        LOGGER.debug("DAO batch insert trainees {}", trainees);
        try(SqlSession sqlSession = getSession()) {
            try {
                for (Trainee trainee: trainees){
                    getTraineeMapper(sqlSession).insert(null, trainee);
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert all trainees {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(Trainee trainee) {
        LOGGER.debug("DAO delete trainee {}", trainee);
        try (SqlSession sqlSession = getSession()){
            try {
                 getTraineeMapper(sqlSession).delete(trainee);
            }catch (RuntimeException ex){
                LOGGER.info("Can't delete trainee {} {}", trainee ,ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() {
        LOGGER.debug("DAO delete all trainees");
        try (SqlSession sqlSession = getSession()){
            try {
                getTraineeMapper(sqlSession).deleteAll();
            }catch (RuntimeException ex){
                LOGGER.info("Can't delete all trainees {}", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }
}
