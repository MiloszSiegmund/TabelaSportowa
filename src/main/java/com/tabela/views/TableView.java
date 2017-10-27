package com.tabela.views;

import com.tabela.dao.TeamDao;
import com.tabela.dao.TeamDaoImpl;
import com.tabela.domain.SimulationInfo;
import com.tabela.domain.Team;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;

import java.util.Comparator;
import java.util.List;

/**
 * Created by MiłoszSiegmund on 2017-10-06.
 */
public class TableView extends VerticalLayout implements View{
    private Button btnTeamsView = new Button("TEAMS");
    private Button btnSimulationView = new Button("SIMULATION");
    private Button btnContinueSimulation = new Button("CONTINUE");
    

    private Grid<Team> teams = new Grid<>(Team.class);
    private TeamDao teamDao = new TeamDaoImpl();

    public TableView()
    {
        teams.setCaption("RESULTS");
        teams.setSizeFull();
        teams.setColumnOrder(
                "rank",
                "name",
                "played",
                "wins",
                "draws",
                "losts",
                "goalsFor",
                "goalsAgainst",
                "points"
                );

        teams.getColumn("id").setHidden(true);
        teams.getColumn("quality").setHidden(true);
        teams.getColumn("stadiumCapacity").setHidden(true);
        teams.getColumn("stadiumName").setHidden(true);


        addComponent(teams);


        teams.setStyleGenerator(e -> {
            if (e.getRank() == 1)
            {
                return "first";
            }
            else if (e.getRank() == 2 || e.getRank() == 3){
                return "second-third";
            }
            else if (e.getRank() == teamDao.getAll().size() || e.getRank() == teamDao.getAll().size() - 1)
            {
                return "last";
            }
            else
            {
                return "other";
            }
        });

        addComponent(new HorizontalLayout(btnTeamsView, btnSimulationView, btnContinueSimulation/*, btnNewSimulation*/));

        btnTeamsView.addClickListener(e -> {

                Navigator navigator = UI.getCurrent().getNavigator();
                navigator.navigateTo(ViewName.TEAMS_VIEW);

        });
        btnSimulationView.addClickListener(e -> {
            if (teamDao.getAll().size() % 2 == 1)
            {
                Notification.show("Przygotuj parzyst ilość drużyn!", Notification.Type.ERROR_MESSAGE);
            }
            else {
                Navigator navigator = UI.getCurrent().getNavigator();
                navigator.navigateTo(ViewName.SIMULATION_VIEW);
            }
        });

        btnContinueSimulation.addClickListener(e -> {
            if (teamDao.getAll().size() % 2 == 1)
            {
                Notification.show("Przygotuj parzyst ilość drużyn!", Notification.Type.ERROR_MESSAGE);
            }
            else {
                Navigator navigator = UI.getCurrent().getNavigator();
                navigator.navigateTo(ViewName.SIMULATION_VIEW);
            }
        });


    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

        List<Team> t = teamDao.getAll();
        t.sort(
                (t1, t2) -> {
                    if (t1.getPoints().equals(t2.getPoints()))
                    {
                        return Integer.compare(t2.getGoalsFor() - t2.getGoalsAgainst(), t1.getGoalsFor() - t1.getGoalsAgainst());
                    }
                    return t2.getPoints().compareTo(t1.getPoints());
                }
        );
        for(int i = 1; i <= t.size(); ++i)
        {
            t.get(i - 1).setRank(i);
        }
        teams.setItems(t);


        if (SimulationInfo.whichRound == (teamDao.getAll().size() - 1 ) * 2)
        {
            SimulationInfo.simulation = false;
            SimulationInfo.simulationFinish = true;
        }

        if (!SimulationInfo.simulation)
        {
            btnSimulationView.setEnabled(true);
            btnContinueSimulation.setEnabled(false);
            btnTeamsView.setEnabled(true);

        }
        else
        {
            btnSimulationView.setEnabled(false);
            btnContinueSimulation.setEnabled(true);
            btnTeamsView.setEnabled(false);

        }
    }
}
