package GUI;

import Controller.Controller;

import javax.swing.*;
import java.awt.*;

public class UserAreaFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel container;
    private Controller controller;

    public UserAreaFrame(Controller controller) {

        this.controller = controller;
        setTitle("User Area");

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // pannelli
        UserArea userAreaPanel = new UserArea(this, controller);
        SetDati setDatiPanel = new SetDati(this, controller);
        iscriviEvento iscriviEventoPanel = new iscriviEvento(this, controller);
        CreaEvento creaEventoPanel = new CreaEvento(this, controller);

        container.add(userAreaPanel.getMainPanel(), "HOME");
        container.add(setDatiPanel.getMainPanel(), "SET_DATI");
        container.add(iscriviEventoPanel.getMainPanel(), "ISCRIVI_EVENTO");
        container.add(creaEventoPanel.getMainPanel(), "CREA_EVENTO");



        setContentPane(container);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
    }

    public void openSelectEvento() {
        SelectEventoFrame selectEventoFrame = new SelectEventoFrame(controller);
        selectEventoFrame.setVisible(true);
        dispose();
    }

    public void logout() {
        new LoginFrame().setVisible(true);
        dispose();
    }

    public void showHome() {
        cardLayout.show(container, "HOME");
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


}