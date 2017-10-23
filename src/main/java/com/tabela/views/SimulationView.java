package com.tabela.views;

import com.tabela.dao.TeamDao;
import com.tabela.dao.TeamDaoImpl;
import com.tabela.domain.Match;
import com.tabela.domain.SimulationInfo;
import com.tabela.domain.Team;
import com.tabela.domain.layout.RoundLayout;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.*;

/**
 * Created by MiłoszSiegmund on 2017-09-26.
 */
public class SimulationView extends VerticalLayout implements View {
    private TeamDao teamDao = new TeamDaoImpl();
    private List<RoundLayout> roundLayouts = new ArrayList<>();


    private GridLayout createMatchLayout(Match match) {
        GridLayout layout = new GridLayout(6, 4);
        layout.setWidth("400px");
        layout.setHeight("150px");

        Label lRound = new Label("ROUND");
        lRound.addStyleName("stylenam");

        Label lHome = new Label(match.getHome().getName());
        lHome.addStyleName("fontColour");
        TextField tfHome = new TextField();
        Label lSeparator = new Label(" : ");
        TextField tfAway = new TextField();
        Label lAway = new Label(match.getAway().getName());
        Button btnSimulate = new Button("SIMULATE");
        btnSimulate.addStyleName(ValoTheme.BUTTON_PRIMARY);


        layout.addComponent(lHome, 0, 0);
        layout.addComponent(tfHome, 1, 0);
        layout.addComponent(lSeparator, 2, 0);
        layout.addComponent(tfAway, 3, 0);
        layout.addComponent(lAway, 4, 0);
        layout.addComponent(btnSimulate, 5, 0);


        Label lStadium = new Label("Stadium Name");
        Label tfStadium = new Label();
        layout.addComponent(lStadium, 0, 2);
        layout.addComponent(tfStadium, 1, 2);

        Label lAudience = new Label("Audience");
        Label tfAudience = new Label();
        layout.addComponent(lAudience,0,3);
        layout.addComponent(tfAudience,1,3);

        layout.setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
        layout.setComponentAlignment(btnSimulate,Alignment.TOP_RIGHT);

        tfStadium.setValue(match.getHome().getStadiumName());


        btnSimulate.addClickListener(e -> {

            tfAudience.setValue(String.valueOf(match.getAudienceMatch()));
            List<Integer> results = match.playTheMatch();
            tfHome.setValue(String.valueOf(results.get(0)));
            tfAway.setValue(String.valueOf(results.get(1)));

        });

        return layout;
    }

    public SimulationView() {
        List<Team> teams = teamDao.getAll();
        Map<Integer, List<Match>> m = Match.generateGames(teams);

        m.forEach((k, v) -> {
            roundLayouts.add(new RoundLayout(v));
        });

        roundLayouts.forEach(r -> addComponent(r));
    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        if (SimulationInfo.whichRound == 0)
        {
            SimulationInfo.simulation = true;
        }
    }
}
