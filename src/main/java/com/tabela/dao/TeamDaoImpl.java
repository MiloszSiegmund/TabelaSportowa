package com.tabela.dao;

import com.tabela.domain.Team;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * Created by MiłoszSiegmund on 2017-09-11.
 */
public class TeamDaoImpl implements TeamDao, Serializable {
    @Override
    public void add(Team team) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE");
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            em.persist(team);


            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public void update(Team team) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE");
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            System.out.println("TEAM: " + team);
            Team team1 = em.find(Team.class, team.getId());
            if (team1 != null) {

                team1.setName(team.getName());
                team1.setQuality(team.getQuality());
                team1.setStadiumCapacity(team.getStadiumCapacity());
                team1.setStadiumName(team.getStadiumName());
                team1.setDraws(team.getDraws());
                team1.setLosts(team.getLosts());
                team1.setWins(team.getWins());
                team1.setPlayed(team.getPlayed());
                team1.setGoalsFor(team.getGoalsFor());
                team1.setGoalsAgainst(team.getGoalsAgainst());
                team1.setPoints(team.getPoints());
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public void delete(Long id) {

        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE");
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            Team team = em.find(Team.class, id);
            if (team != null) {

                em.remove(team);
            }

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }


    @Override
    public List<Team> getAll() {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE");
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        List<Team> teams = null;

        try {
            tx.begin();
            Query query = em.createQuery("SELECT t from Team t");
            teams = query.getResultList();


            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return teams;

    }

    @Override
    public Optional<Team> getById(Long id) {
        EntityManagerFactory factory = Persistence.createEntityManagerFactory("PERSISTENCE");
        EntityManager em = factory.createEntityManager();
        EntityTransaction tx = em.getTransaction();


        Team team = null;

        try {
            tx.begin();

            team = em.find(Team.class, team.getId());

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            if (em != null) {
                em.close();
            }
        }
        return Optional.ofNullable(team);


    }
}
