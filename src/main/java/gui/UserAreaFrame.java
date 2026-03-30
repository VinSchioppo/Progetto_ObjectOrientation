package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class UserAreaFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private Controller controller;

    private static final String INVITA_GIUDICE = "INVITA_GIUDICE";

    private UserArea userAreaPanel;
    private IscriviEvento iscriviEventoPanel;

    // NUOVI PANEL SELECT EVENTO
    private InvitaGiudice invitaGiudicePanel;
    private PartecipanteGUI partecipanteGUIPanel;
    private OrganizzatoreGUI organizzatoreGUIPanel;

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
        SelectEvento selectEventoPanel = new SelectEvento(this);
        organizzatoreGUIPanel = new OrganizzatoreGUI(this, controller);
        invitaGiudicePanel = new InvitaGiudice(this, controller);
        partecipanteGUIPanel = new PartecipanteGUI(this, controller);
        GiudiceGUI giudicePanel = new GiudiceGUI(this, controller);

        // ===== AGGIUNTA =====
        container.add(userAreaPanel.getMainPanel(), "HOME");
        container.add(setDatiPanel.getMainPanel(), "SET_DATI");
        container.add(iscriviEventoPanel.getMainPanel(), "ISCRIVI_EVENTO");
        container.add(creaEventoPanel.getMainPanel(), "CREA_EVENTO");

        container.add(selectEventoPanel.getMainPanel(), "SELECT_EVENTO");
        container.add(organizzatoreGUIPanel.getMainPanel(), "ORGANIZZATORE");
        container.add(invitaGiudicePanel.getMainPanel(), INVITA_GIUDICE);
        container.add(partecipanteGUIPanel.getMainPanel(), "PARTECIPANTE");
        container.add(giudicePanel.getMainPanel(), "GIUDICE");
    }



    // ================= NAVIGAZIONE =================

    public void showHome() {

        userAreaPanel.refreshDatiUtente();
        userAreaPanel.refreshEventiIscritti();
        userAreaPanel.refreshInvitiGiudice();

        cardLayout.show(container, "HOME");
    }

    public void showSelectEvento() {
        cardLayout.show(container, "SELECT_EVENTO");
    }

    public void showSetDati() {
        cardLayout.show(container, "SET_DATI");
    }

    public void showIscriviEvento() {

        iscriviEventoPanel.refreshEventi();

        cardLayout.show(container, "ISCRIVI_EVENTO");
    }

    public void showCreaEvento() {
        cardLayout.show(container, "CREA_EVENTO");
    }

    public void showOrganizzatoreGUI() {

        organizzatoreGUIPanel.refresh();

        cardLayout.show(container, "ORGANIZZATORE");
    }

    public void showPartecipanteGUI() {
        partecipanteGUIPanel.refreshListaEventi();
        cardLayout.show(container, "PARTECIPANTE");
    }

    public void showGiudiceGUI() {
        cardLayout.show(container, "GIUDICE");
    }

    public void showInvitaGiudice(int idEvento) {

        controller.selectEvento(idEvento, Role.ORGANIZZATORE);

        invitaGiudicePanel = new InvitaGiudice(this, controller);

        container.add(invitaGiudicePanel.getMainPanel(), INVITA_GIUDICE);

        cardLayout.show(container, INVITA_GIUDICE);
    }

    public void showSetDatiEvento(int idEvento) {

        controller.selectEvento(idEvento, Role.ORGANIZZATORE);

        SetDatiEvento panel = new SetDatiEvento(this, controller, idEvento);

        container.add(panel.getMainPanel(), "SET_DATI_EVENTO");

        cardLayout.show(container, "SET_DATI_EVENTO");
    }
    public void showTeamGUI() {

        TeamGUI teamGUIPanel = new TeamGUI(this, controller);

        container.add(teamGUIPanel.getMainPanel(), "TEAM");

        cardLayout.show(container, "TEAM");
    }

    public void showProgressiGUI() {

        ProgressiGUI progressiGUI = new ProgressiGUI(this, controller);

        container.add(progressiGUI.getMainPanel(), "PROGRESSI");

        cardLayout.show(container, "PROGRESSI");
    }

    public void openCreaTeamDialog(int idEvento) {
        CreaTeam dialog = new CreaTeam(this, controller, idEvento);
        dialog.setVisible(true);
    }

    public void showClassificaGUI(int idEvento) {

        ClassificaGUI classificaGUI = new ClassificaGUI(this, controller, idEvento);

        container.add(classificaGUI.getMainPanel(), "CLASSIFICA");

        cardLayout.show(container, "CLASSIFICA");
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