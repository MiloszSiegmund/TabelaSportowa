package com.tabela.views;

import com.tabela.dao.TeamDao;
import com.tabela.dao.TeamDaoImpl;
import com.tabela.domain.Team;
import com.vaadin.data.Binder;
import com.vaadin.data.ValidationException;
import com.vaadin.data.converter.StringToIntegerConverter;
import com.vaadin.data.converter.StringToLongConverter;
import com.vaadin.data.validator.IntegerRangeValidator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.VaadinRequest;
import com.vaadin.shared.ui.grid.HeightMode;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;

import java.util.Optional;
import java.util.Set;

/**
 * Created by MiłoszSiegmund on 2017-09-13.
 */
public class TeamsView extends VerticalLayout implements View {

    private final Button btnAdd = new Button("ADD TEAM");
    private final Button btnDelete = new Button("DELETE TEAM");
    private final Button btnUpdate = new Button("UPDATE TEAM");

    private final TextField tfId = new TextField();
    private final TextField tfName = new TextField("TEAM NAME:");
    private final TextField tfStadiumName = new TextField("STADIUM NAME");
    private final TextField tfStadiumCapacity = new TextField("STADIUM CAPACITY");

    private final ComboBox<Integer> cbQuality = new ComboBox<>("TEAM QUALITY:");

    private final Grid<Team> grid = new Grid<>(Team.class);
    private final Binder<Team> formBinder = new Binder<>();

    private TeamDao teamDao = new TeamDaoImpl();


    private HorizontalLayout createButtons()
    {
        btnAdd.addStyleName(ValoTheme.BUTTON_FRIENDLY);
        btnDelete.addStyleName(ValoTheme.BUTTON_DANGER);
        btnUpdate.addStyleName(ValoTheme.BUTTON_FRIENDLY);

        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponents(btnAdd, btnUpdate, btnDelete);
        return layout;
    }

    private HorizontalLayout createTextFields()
    {
        cbQuality.setItems(1,2,3,4,5);
        cbQuality.setSelectedItem(1);

        tfId.setVisible(false);
        HorizontalLayout layout = new HorizontalLayout();
        layout.addComponent(tfId);
        layout.addComponent(tfName);
        layout.addComponent(tfStadiumCapacity);
        layout.addComponent(tfStadiumName);
        layout.addComponent(cbQuality);
        return layout;
    }

    private VerticalLayout createGrid()
    {
        grid.setCaption("TEAMS");
        grid.getColumn("id").setHidden(true);
        grid.getColumn("wins").setHidden(true);
        grid.getColumn("losts").setHidden(true);
        grid.getColumn("draws").setHidden(true);
        grid.getColumn("goalsFor").setHidden(true);
        grid.getColumn("goalsAgainst").setHidden(true);
        grid.getColumn("points").setHidden(true);
        grid.getColumn("played").setHidden(true);
        grid.setColumnOrder("name", "quality");
        grid.setSizeFull();
        grid.setItems(teamDao.getAll());
        grid.addStyleName(ValoTheme.TABLE_COMPACT);


        return new VerticalLayout(grid);
    }

    private VerticalLayout createGui()
    {
        VerticalLayout layout = new VerticalLayout();
        layout.addComponent(createGrid());
        layout.addComponent(createButtons());
        layout.addComponent(createTextFields());
        return layout;
    }
    private void createFormBinder()
    {
        formBinder
                .forField(tfId)
                .withConverter(new StringToLongConverter(""))
                .bind(Team::getId, Team::setId);

        formBinder
                .forField(tfName)
                .withValidator(n -> n.matches("[A-Z ]+"), "Same litery")
                .bind(Team::getName, Team::setName);

        formBinder
                .forField(cbQuality)
                .bind(Team::getQuality, Team::setQuality);
        formBinder
                .forField(tfStadiumCapacity)
                .withConverter(new StringToIntegerConverter("Pojemnosc stadionu musi być liczbą całkowitą"))
                .withValidator(new IntegerRangeValidator("Max pojemnosc stadionu to 1000000", 0, 1000000)) //max milion
                .bind(Team::getStadiumCapacity, Team::setStadiumCapacity);

        formBinder
                .forField(tfStadiumName)
                .withValidator(n-> n.matches("[A-Z ]+"), "SAME DUZE LITERY")
                .bind(Team::getStadiumName, Team::setStadiumName);
    }



    private void createActions()
    {
        btnAdd.addClickListener(e -> {
            try {
                Team team = new Team();
                formBinder.writeBean(team);
                team.setPlayed(0);
                team.setWins(0);
                team.setDraws(0);
                team.setLosts(0);
                team.setGoalsFor(0);
                team.setGoalsAgainst(0);
                team.setPoints(0);

                teamDao.add(team);
                grid.setItems(teamDao.getAll());
                Team team1 = Team
                        .builder()
                        .id(0L)
                        .name("")
                        .stadiumCapacity(new Integer(0))
                        .stadiumName("")
                        .quality(1)
                        .build();
                formBinder.readBean(team1);
            } catch (ValidationException e1)
            {
                Notification.show("Niepoprawne dane", Notification.Type.ERROR_MESSAGE);
            }

        });

        btnUpdate.addClickListener(e -> {
            try {
                Team team = new Team();
                formBinder.writeBean(team);
                teamDao.update(team);
                Team team1 = Team.builder().id(0L).name("").stadiumCapacity(0).stadiumName("").quality(1).build();
                formBinder.readBean(team1);
                grid.setItems(teamDao.getAll());
            } catch (ValidationException e1)
            {
                Notification.show("Niepoprawne dane", Notification.Type.ERROR_MESSAGE);
            }


        });

        btnDelete.addClickListener(e -> {
            Set<Team> selectedTeam = grid.getSelectedItems();
            if (selectedTeam != null)
            {
                for (Team team : selectedTeam)
                {
                    teamDao.delete(team.getId());
                }
            }
            grid.setItems(teamDao.getAll());


        });

        grid.addSelectionListener(e -> {
            Optional<Team> team = e.getFirstSelectedItem();
            if (team.isPresent())
            {
                formBinder.readBean(team.get());
            }
        });
    }

    public TeamsView()
    {
        createActions();
        addComponent(createGui());
        createFormBinder();
        grid.setItems(teamDao.getAll());

    }

    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {

    }

}
