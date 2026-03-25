package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectEventoFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private Controller controller;
    private InvitaGiudice invitaGiudicePanel;

    public SelectEventoFrame(Controller controller) {
        this.controller = controller;

        setTitle("Select Evento");

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // pannelli
        SelectEvento selectEventoPanel = new SelectEvento(this);
        OrganizzatoreGUI organizzatorePanel = new OrganizzatoreGUI(this, controller);
        invitaGiudicePanel = new InvitaGiudice(this, controller);
        SetDatiEvento setDatiEventoPanel = new SetDatiEvento(this, controller);
        PartecipanteGUI partecipanteGUIPanel = new PartecipanteGUI(this);
        TeamGUI teamGUIPanel = new TeamGUI(this, controller);
        GiudiceGUI giudicePanel = new GiudiceGUI(this, controller);

        container.add(selectEventoPanel.getMainPanel(), "HOME");
        container.add(organizzatorePanel.getMainPanel(), "ORGANIZZATORE");
        container.add(invitaGiudicePanel.getMainPanel(), "INVITA_GIUDICE");
        container.add(setDatiEventoPanel.getMainPanel(), "SET_DATI_EVENTO");
        container.add(partecipanteGUIPanel.getMainPanel(), "PARTECIPANTE");
        container.add(teamGUIPanel.getMainPanel(), "TEAM");
        container.add(giudicePanel.getMainPanel(), "GIUDICE");

        setContentPane(container);
        pack();
        setLocationRelativeTo(null);

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {

                controller.exitApplication();

                System.exit(0);
            }
        });
    }

    public void logout() {
        UserAreaFrame userAreaFrame = new UserAreaFrame(controller);
        userAreaFrame.setVisible(true);
        dispose();
    }

    public void openCreaTeamDialog() {
        CreaTeam dialog = new CreaTeam(this, controller);
        dialog.setVisible(true);
    }

    public void showHome() {
        cardLayout.show(container, "HOME");
    }

    public void showOrganizzatoreGUI() {
        cardLayout.show(container, "ORGANIZZATORE");
    }

    public void showPartecipanteGUI() {
        cardLayout.show(container, "PARTECIPANTE");
    }

    public void showGiudiceGUI() {
        cardLayout.show(container, "GIUDICE");
    }

    public void showInvitaGiudice() { cardLayout.show(container, "INVITA_GIUDICE"); }

    public void showSetDatiEvento() {
        cardLayout.show(container, "SET_DATI_EVENTO");
    }

    public void showTeamGUI() {
        cardLayout.show(container, "TEAM");
    }

    public void showProgressiGUI(int idTeam) {

        ProgressiGUI progressiGUI = new ProgressiGUI(this, controller, idTeam);

        container.add(progressiGUI.getMainPanel(), "PROGRESSI");

        cardLayout.show(container, "PROGRESSI");
    }

    public Controller getController() {
        return controller;
    }
}