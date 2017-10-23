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
    private Button btnTeamsView = new Button("TEMS");
    private Button btnSimulationView = new Button("SIMULATION");
    private Button btnContinueSimulation = new Button("CONTINUE");

    private Grid<Team> teams = new Grid<>(Team.class);
    private TeamDao teamDao = new TeamDaoImpl();

    public TableView()
    {
        teams.setCaption("RESULTS");
        teams.setSizeFull();
        teams.setColumnOrder(
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

        addComponent(new HorizontalLayout(btnTeamsView, btnSimulationView, btnContinueSimulation));

        btnTeamsView.addClickListener(e -> {
            Navigator navigator = UI.getCurrent().getNavigator();
            navigator.navigateTo(ViewName.TEAMS_VIEW);
        });
        btnSimulationView.addClickListener(e -> {
            Navigator navigator = UI.getCurrent().getNavigator();
            navigator.navigateTo(ViewName.SIMULATION_VIEW);
        });

        btnContinueSimulation.addClickListener(e -> {
            Navigator navigator = UI.getCurrent().getNavigator();
            navigator.navigateTo(ViewName.SIMULATION_VIEW);
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
        teams.setItems(t);

        if (SimulationInfo.whichRound == (teamDao.getAll().size() - 1 ) * 2)
        {
            SimulationInfo.simulation = false;
        }

        if (!SimulationInfo.simulation)
        {
            btnSimulationView.setEnabled(true);
            btnContinueSimulation.setEnabled(false);
        }
        else
        {
            btnSimulationView.setEnabled(false);
            btnContinueSimulation.setEnabled(true);
        }
    }
}
