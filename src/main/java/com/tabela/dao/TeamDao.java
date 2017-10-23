package com.tabela.dao;

import com.tabela.domain.Team;

import java.util.List;
import java.util.Optional;

/**
 * Created by MiłoszSiegmund on 2017-09-08.
 */
public interface TeamDao {
    void add(Team team);
    void update(Team team);
    void delete(Long id);
    List<Team> getAll();
    Optional<Team> getById(Long id);
    void resetTable();
}
