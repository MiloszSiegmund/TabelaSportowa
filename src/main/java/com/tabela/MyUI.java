package com.tabela;

import javax.servlet.annotation.WebServlet;

import com.tabela.dao.TeamDao;
import com.tabela.dao.TeamDaoImpl;
import com.tabela.views.SimulationView;
import com.tabela.views.TableView;
import com.tabela.views.TeamsView;
import com.tabela.views.ViewName;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.navigator.Navigator;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.*;

@Theme("mytheme")
public class MyUI extends UI {

    @Override
    protected void init(VaadinRequest vaadinRequest) {

        TeamDao teamDao = new TeamDaoImpl();
        SimulationView simulationView = new SimulationView();

        Navigator navigator = new Navigator(this, this);
        navigator.addView(ViewName.TEAMS_VIEW, TeamsView.class);
        navigator.addView(ViewName.SIMULATION_VIEW, simulationView);
        navigator.addView(ViewName.TABLE_VIEW, TableView.class);
        navigator.navigateTo(ViewName.TABLE_VIEW);

    }

    @WebServlet(urlPatterns = "/*", name = "MyUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = MyUI.class, productionMode = false)
    public static class MyUIServlet extends VaadinServlet {
    }
}
