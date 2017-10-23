package com.tabela.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by MiłoszSiegmund on 2017-09-15.
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Match implements Serializable {
    private Team home;
    private Team away;
    private Integer audience;
    private LocalDate time;
    //private String referee;

    @Override
    public String toString() {
        return
                home.getName() + " vs " + away.getName()
                + "\n" +
                "Stadium: " + home.getStadiumName()
                + "\n" +
                "Audience: " + audience
                + "\n" +
                "Time: " + time
                + "\n" ;
                //"Referee: " + referee
                //+ "\n";
    }




    public static Map<Integer, List<Match>> generateGames(List<Team> teams)
    {
        if (teams.size() % 2 == 0) {

            Map<Integer, List<Match>> m1 = new LinkedHashMap<>();
            Map<Integer, List<Match>> m2 = new LinkedHashMap<>();
            List<Team> teams1 = teams.subList(0, teams.size() / 2);
            List<Team> teams2 = teams.subList(teams.size() / 2, teams.size());

            //Map<Integer, List<Match>> games = new LinkedHashMap<>();

            for (int i = 0; i < teams.size() - 1; i++) {
                //WPAKOWANIE KOLEJEK DO MAPY
                m1.put(i + 1, new ArrayList<>());
                m2.put(teams.size() + i, new ArrayList<>());
                for (int j = 0; j < teams1.size(); j++) {
                    m1.get(i + 1).add(Match.builder().home(teams1.get(j)).away(teams2.get(j)).build());
                    m2.get(teams.size() + i).add(Match.builder().home(teams2.get(j)).away(teams1.get(j)).build());
                }
                //PRZESUNIECIE
                Collections.rotate(teams1, 1);
                Collections.rotate(teams2, -1);
                Collections.swap(teams1, 0, 1);

                Team t = teams1.get(0);
                teams1.set(0, teams2.get(teams2.size() - 1));
                teams2.set(teams2.size() - 1, t);
            }
            Map<Integer, List<Match>> m = new LinkedHashMap<>();
            m.putAll(m1);
            m.putAll(m2);

            return m;
        }

        return null;
    }
    public  Integer getAudienceMatch()
    {

        if (away.getQuality() == 1)
        {
            audience = home.getStadiumCapacity();
        }
        if (away.getQuality() == 2)
        {
            audience = 90 * home.getStadiumCapacity() / 100;
        }
        if (away.getQuality() == 3)
        {
            audience = 80 * home.getStadiumCapacity() / 100;
        }
        if (away.getQuality() == 4)
        {
            audience = 70 * home.getStadiumCapacity() / 100;
        }
        if (away.getQuality() == 5)
        {
            audience = home.getStadiumCapacity() / 2;
        }


        return audience;
    }

    public List<Integer> playTheMatch()
    {
        int homeGoals = 0;
        int awayGoals = 0;

        Random rnd = new Random();
        homeGoals = rnd.nextInt(home.getQuality());
        awayGoals = rnd.nextInt(away.getQuality());

        if (rnd.nextInt(10000) % 5 == 0)
        {
            homeGoals += rnd.nextInt(5);
        }

        return new ArrayList<>(Arrays.asList(homeGoals, awayGoals));

    }

    public void updateTeamsAfterMatch(int homeGoals, int awayGoals, int audiece)
    {
        if (homeGoals > awayGoals)
        {
            updateAfterHomeWon(homeGoals, awayGoals);
        }
        else if (homeGoals < awayGoals)
        {
            updateAfterAwayWon(homeGoals, awayGoals);
        }
        else
        {
            updateAfterDraw(homeGoals, awayGoals);
        }
    }

    public void updateAfterHomeWon(int homeGoals, int awayGoals)
    {
        home.setPoints(home.getPoints() + 3);
        home.setGoalsAgainst(home.getGoalsAgainst() + awayGoals);
        home.setGoalsFor(home.getGoalsFor() + homeGoals);
        home.setWins(home.getWins() + 1);
        home.setPlayed(home.getPlayed() + 1);

        away.setGoalsAgainst(away.getGoalsAgainst() + homeGoals);
        away.setGoalsFor(away.getGoalsFor() + awayGoals);
        away.setLosts(away.getLosts() + 1);
        away.setPlayed(away.getPlayed() + 1);
    }
    public void updateAfterAwayWon(int homeGoals, int awayGoals)
    {
        away.setPoints(away.getPoints() + 3);
        away.setGoalsAgainst(away.getGoalsAgainst() + homeGoals);
        away.setGoalsFor(away.getGoalsFor() + awayGoals);
        away.setWins(away.getWins() + 1);
        away.setPlayed(away.getPlayed() + 1);

        home.setGoalsAgainst(home.getGoalsAgainst() + homeGoals);
        home.setGoalsFor(home.getGoalsFor() + awayGoals);
        home.setLosts(home.getLosts() + 1);
        home.setPlayed(home.getPlayed() + 1);
    }
    public void updateAfterDraw(int homeGoals, int awayGoals)
    {
        away.setPoints(away.getPoints() + 1);
        away.setGoalsAgainst(away.getGoalsAgainst() + homeGoals);
        away.setGoalsFor(away.getGoalsFor() + awayGoals);
        away.setDraws(away.getDraws() + 1);
        away.setPlayed(away.getPlayed() + 1);

        home.setPoints(home.getPoints() + 1);
        home.setGoalsAgainst(home.getGoalsAgainst() + awayGoals);
        home.setGoalsFor(home.getGoalsFor() +homeGoals);
        home.setDraws(home.getDraws() + 1);
        home.setPlayed(home.getPlayed() + 1);
    }




}
