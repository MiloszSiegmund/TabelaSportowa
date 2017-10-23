package com.tabela.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * Created by MiłoszSiegmund on 2017-09-08.
 */
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Team implements Serializable{
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private Integer played;
    private Integer wins;
    private Integer draws;
    private Integer losts;
    private Integer goalsFor;
    private Integer goalsAgainst;
    private Integer points;
    private Integer quality;
    private Integer stadiumCapacity;
    private String stadiumName;

    public void zeruj()
    {
        played = 0;
        wins = 0;
        draws = 0;
        losts = 0;
        goalsFor = 0;
        goalsAgainst = 0;
        points = 0;
    }

}
