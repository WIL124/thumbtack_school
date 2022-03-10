package net.thumbtack.school.database.mybatis.daoimpl;

import net.thumbtack.school.database.model.Group;
import net.thumbtack.school.database.model.School;
import net.thumbtack.school.database.model.Subject;
import net.thumbtack.school.database.model.Trainee;
import net.thumbtack.school.database.mybatis.dao.SchoolDao;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class SchoolDaoImpl extends DaoImplBase implements SchoolDao {

    private static final Logger LOGGER = LoggerFactory.getLogger(SchoolDaoImpl.class);

    @Override
    public School insert(School school) {
        LOGGER.debug("DAO insert school {}", school);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSchoolMapper(sqlSession).insert(school);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert school {} {}", school, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
            return school;
        }
    }

    @Override
    public School getById(int id) {
        LOGGER.debug("DAO get school by id {}", id);
        try (SqlSession sqlSession = getSession()) {
            School school = getSchoolMapper(sqlSession).getById(id);

            if (school.getGroups() == null) {
                school.setGroups(new ArrayList<>());
            }
            return school;
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get school bu id {} {}", id, ex);
            return null;
        }
    }

    @Override
    public List<School> getAllLazy() {
        LOGGER.debug("DAO get list of schools using LAZY");
        try (SqlSession sqlSession = getSession()) {
            return getSchoolMapper(sqlSession).getAllLazy();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get list of schools using LAZY", ex);
            throw ex;
        }
    }

    @Override
    public List<School> getAllUsingJoin() {
        LOGGER.debug("DAO get list of schools using JOIN");
        try (SqlSession sqlSession = getSession()) {
            return getSchoolMapper(sqlSession).getAllJoin();
        } catch (RuntimeException ex) {
            LOGGER.info("Can't get list of schools using JOIN", ex);
            throw ex;
        }
    }

    @Override
    public void update(School school) {
        LOGGER.debug("DAO update school {}", school);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSchoolMapper(sqlSession).update(school);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't update school {} {}", school, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void delete(School school) {
        LOGGER.debug("DAO delete school {}", school);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSchoolMapper(sqlSession).delete(school);
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete school {} {}", school, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public void deleteAll() {
        try (SqlSession sqlSession = getSession()) {
            try {
                getSchoolMapper(sqlSession).deleteAll();
            } catch (RuntimeException ex) {
                LOGGER.info("Can't delete all schools", ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
    }

    @Override
    public School insertSchoolTransactional(School school) {
        LOGGER.debug("DAO insert school transactional {}", school);
        try (SqlSession sqlSession = getSession()) {
            try {
                getSchoolMapper(sqlSession).insert(school);
                for (Group group : school.getGroups()) {
                    getGroupMapper(sqlSession).insert(school, group);
                    for (Trainee trainee : group.getTrainees()) {
                        getTraineeMapper(sqlSession).insert(group, trainee);
                    }
                    for (Subject subject : group.getSubjects()) {
                        getGroupMapper(sqlSession).addSubjectToGroup(group, subject);
                    }
                }
            } catch (RuntimeException ex) {
                LOGGER.info("Can't insert school transactional {} {}", school, ex);
                sqlSession.rollback();
                throw ex;
            }
            sqlSession.commit();
        }
        return school;
    }
}
