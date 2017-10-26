package com.tabela.domain.layout;

import com.tabela.domain.Match;
import com.tabela.domain.SimulationInfo;
import com.tabela.views.ViewName;
import com.vaadin.navigator.Navigator;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MiłoszSiegmund on 2017-10-03.
 */
public class RoundLayout extends VerticalLayout {
    private List<MatchLayout> matchLayouts;
    private Button btnSimulateRound = new Button("SIMULATE ROUND");
    private Button btnTable = new Button("TABLE");
    private static int buttonNumber = 0;

    public RoundLayout(List<Match> matches) {
        matchLayouts = new ArrayList<>();
        for(Match match : matches)
        {
            matchLayouts.add(new MatchLayout(this, match));
        }

        for(MatchLayout matchLayout : matchLayouts)
        {
            addComponent(matchLayout);
        }
        addComponent(new HorizontalLayout(btnSimulateRound, btnTable));
        btnSimulateRound.addStyleName(ValoTheme.BUTTON_PRIMARY);
        btnSimulateRound.addClickListener(e -> {
            for(MatchLayout matchLayout : matchLayouts)
            {
                matchLayout.startSimulation();
            }
            btnTable.setEnabled(true);
            btnSimulateRound.setEnabled(false);
        });

        btnTable.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnTable.setEnabled(false);
        btnTable.addClickListener(e -> {
            Navigator navigator = UI.getCurrent().getNavigator();
            navigator.navigateTo(ViewName.TABLE_VIEW);
            btnTable.setEnabled(false);

        });

        //NA POCZATEK WSZYSTKIE PRZYCISKI OPROCZ PIERWSZEGO MAM NIEKATYWNE
        for (int i = 1; i < matchLayouts.size(); ++i)
        {
            matchLayouts.get(i).setSimulationButtonActive(false);
        }
    }

    public boolean changeActivatedSimulationButton()
    {
        ++buttonNumber;
        if (buttonNumber >= matchLayouts.size())
        {
            buttonNumber = 0;
        }

        if (buttonNumber > 0) {
            matchLayouts.get(buttonNumber).setSimulationButtonActive(true);
            matchLayouts.get(buttonNumber - 1).setSimulationButtonActive(false);
        }
        else
        {
            matchLayouts.get(matchLayouts.size() - 1).setSimulationButtonActive(false);
            btnTable.setEnabled(true);
            SimulationInfo.whichRound++;
        }

        return buttonNumber == 0;
    }

    public Button getBtnSimulateRound() {
        return btnSimulateRound;
    }

    public void setAllActive()
    {
        matchLayouts.forEach(m -> m.setSimulationButtonActive(true));
    }
}
