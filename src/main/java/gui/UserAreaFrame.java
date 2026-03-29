package gui;

import controller.Controller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserAreaFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private Controller controller;

    private UserArea userAreaPanel;
    private IscriviEvento iscriviEventoPanel;

    // NUOVI PANEL SELECT EVENTO
    private SelectEvento selectEventoPanel;
    private InvitaGiudice invitaGiudicePanel;
    private PartecipanteGUI partecipanteGUIPanel;

    public UserAreaFrame(Controller controller) {

        this.controller = controller;
        setTitle("Applicazione");

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        pannelli();

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

    private void pannelli(){

        // ===== USER AREA =====
        userAreaPanel = new UserArea(this, controller);
        SetDati setDatiPanel = new SetDati(this, controller);
        iscriviEventoPanel = new IscriviEvento(this, controller);
        CreaEvento creaEventoPanel = new CreaEvento(this, controller);

        // ===== SELECT EVENTO =====
        selectEventoPanel = new SelectEvento(this);
        OrganizzatoreGUI organizzatorePanel = new OrganizzatoreGUI(this, controller);
        invitaGiudicePanel = new InvitaGiudice(this, controller);
        partecipanteGUIPanel = new PartecipanteGUI(this, controller);
        SetDatiEvento setDatiEventoPanel = new SetDatiEvento(this, controller);
        TeamGUI teamGUIPanel = new TeamGUI(this, controller);
        GiudiceGUI giudicePanel = new GiudiceGUI(this, controller);

        // ===== AGGIUNTA =====
        container.add(userAreaPanel.getMainPanel(), "HOME");
        container.add(setDatiPanel.getMainPanel(), "SET_DATI");
        container.add(iscriviEventoPanel.getMainPanel(), "ISCRIVI_EVENTO");
        container.add(creaEventoPanel.getMainPanel(), "CREA_EVENTO");

        container.add(selectEventoPanel.getMainPanel(), "SELECT_EVENTO");
        container.add(organizzatorePanel.getMainPanel(), "ORGANIZZATORE");
        container.add(invitaGiudicePanel.getMainPanel(), "INVITA_GIUDICE");
        container.add(setDatiEventoPanel.getMainPanel(), "SET_DATI_EVENTO");
        container.add(partecipanteGUIPanel.getMainPanel(), "PARTECIPANTE");
        container.add(teamGUIPanel.getMainPanel(), "TEAM");
        container.add(giudicePanel.getMainPanel(), "GIUDICE");
    }

    // ================= NAVIGAZIONE =================

    public void showHome() {

        userAreaPanel.refreshDatiUtente();
        userAreaPanel.refreshEventiIscritti();

        cardLayout.show(container, "HOME");
    }

    public void showSelectEvento() {
        cardLayout.show(container, "SELECT_EVENTO");
    }

    public void showSetDati() {
        cardLayout.show(container, "SET_DATI");
    }

    public void showIscriviEvento() {
        cardLayout.show(container, "ISCRIVI_EVENTO");
    }

    public void showCreaEvento() {
        cardLayout.show(container, "CREA_EVENTO");
    }

    public void showOrganizzatoreGUI() {
        cardLayout.show(container, "ORGANIZZATORE");
    }

    public void showPartecipanteGUI() {
        partecipanteGUIPanel.refreshListaEventi();
        cardLayout.show(container, "PARTECIPANTE");
    }

    public void showGiudiceGUI() {
        cardLayout.show(container, "GIUDICE");
    }

    public void showInvitaGiudice() {
        cardLayout.show(container, "INVITA_GIUDICE");
    }

    public void showSetDatiEvento() {
        cardLayout.show(container, "SET_DATI_EVENTO");
    }

    public void showTeamGUI() {
        cardLayout.show(container, "TEAM");
    }

    public void showProgressiGUI(int idTeam) {

        ProgressiGUI progressiGUI = new ProgressiGUI(this, controller);

        container.add(progressiGUI.getMainPanel(), "PROGRESSI");

        cardLayout.show(container, "PROGRESSI");
    }

    public void openCreaTeamDialog() {
        CreaTeam dialog = new CreaTeam(this, controller);
        dialog.setVisible(true);
    }

    public void logout() {
        controller.exitApplication();
        new LoginFrame().setVisible(true);
        dispose();
    }

    public Controller getController() {
        return controller;
    }
}