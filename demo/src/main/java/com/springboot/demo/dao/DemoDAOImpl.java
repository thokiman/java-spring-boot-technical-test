package com.springboot.demo.dao;

import com.springboot.demo.entity.History;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Repository
public class DemoDAOImpl implements DemoDAO {

    private EntityManager entityManager;


    @Autowired
    public DemoDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public boolean save(History history) {
        boolean hasSuccess;

        try {
            entityManager.persist(history);
            hasSuccess=true;
        } catch (Exception e) {
            e.printStackTrace();
            hasSuccess=false;
        }

        return hasSuccess;
    }

    @Override
    public List<History> findAll() {

        TypedQuery<History> q =  entityManager. createQuery("FROM History", History.class);
        List<History> lsHistory = q.getResultList();


        return lsHistory;
    }
}
