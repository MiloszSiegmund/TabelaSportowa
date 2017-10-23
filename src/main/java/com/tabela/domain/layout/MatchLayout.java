package com.tabela.domain.layout;


import com.tabela.dao.TeamDao;
import com.tabela.dao.TeamDaoImpl;
import com.tabela.domain.Match;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.ValoTheme;

import java.util.List;

/**
 * Created by MiłoszSiegmund on 2017-10-03.
 */

public class MatchLayout extends GridLayout {
    private Label lHome = new Label();
    private Label lHomeResult = new Label();
    private Label lAwayResult = new Label();
    private Label lAway = new Label();
    private Label lReferee = new Label();
    private Label lStadium = new Label();
    private Label lAudience = new Label();
    private Label lAduienceLabel = new Label("Audience: ");
    private Label lStadiumName = new Label("Stadium Name: ");
    private Label lSeparator = new Label(" : ");
    private Button btnSimulate = new Button("SIMULATE");

    private Match match;
    private TeamDao teamDao = new TeamDaoImpl();


    private RoundLayout roundLayout = null;

    public MatchLayout(RoundLayout roundLayout, Match match)
    {
        super(6, 4);

        this.match = match;

        //USTAWIENIE ZAWARTOSCI LABELOW
        lHome.setValue(match.getHome().getName());
        lHome.addStyleName("label");
        lAway.setValue(match.getAway().getName());
        lAway.addStyleName("label");
        lStadium.setValue(match.getHome().getStadiumName());
        lStadium.addStyleName("font");

        //ROZMIESZCZENIE KOMPONENTOW
        addComponent(lHome, 0, 0);
        addComponent(lHomeResult, 1, 0);
        lHomeResult.addStyleName("style5");
        //addComponent(new Label(" : "), 2, 0);
        addComponent(lSeparator,2,0);
        lSeparator.addStyleName("style3");
        addComponent(lAwayResult, 3, 0);
        lAwayResult.addStyleName("style4");
        addComponent(lAway, 4, 0);
        addComponent(btnSimulate, 5, 0);
        btnSimulate.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSimulate.addStyleName("button");
        addComponent(lStadiumName,0,2);
        lStadiumName.addStyleName("font2");
        addComponent(lStadium, 1, 2);
        lStadium.addStyleName("font");
        addComponent(lAduienceLabel,0,3);
        lAduienceLabel.addStyleName("font2");
        addComponent(lAudience,1,3);
        lAudience.addStyleName("font");

        setDefaultComponentAlignment(Alignment.BOTTOM_RIGHT);
        setComponentAlignment(btnSimulate,Alignment.TOP_RIGHT);

        //KONFIGURACJA PRZYCISKU
        btnSimulate.addClickListener(e -> {
            boolean finish = startSimulation();
            if(finish)
            {
                roundLayout.getBtnSimulateRound().setEnabled(false);
            }
        });

        //WSTRZYKUJEMY ROUND LAYOUT
        this.roundLayout = roundLayout;
    }

    public void setSimulationButtonActive(boolean active)
    {
        btnSimulate.setEnabled(active);
    }

    public boolean startSimulation()
    {
        System.out.println("--------------------------");
        List<Integer> results = match.playTheMatch();
        lHomeResult.setValue(String.valueOf(results.get(0)));
        lAwayResult.setValue(String.valueOf(results.get(1)));
        //lReferee.setValue(referee.getName());
        lAudience.setValue(String.valueOf(match.getAudienceMatch()));

        match.updateTeamsAfterMatch(results.get(0), results.get(1), match.getAudience());

        teamDao.update(match.getHome());
        teamDao.update(match.getAway());

        return roundLayout.changeActivatedSimulationButton();
    }

}
