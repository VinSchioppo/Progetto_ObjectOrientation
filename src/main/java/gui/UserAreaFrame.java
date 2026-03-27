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
    private iscriviEvento iscriviEventoPanel;

    public UserAreaFrame(Controller controller) {

        this.controller = controller;
        setTitle("User Area");

        cardLayout = new CardLayout();
        container = new JPanel(cardLayout);

        // pannelli
        userAreaPanel = new UserArea(this, controller);
        SetDati setDatiPanel = new SetDati(this, controller);
        iscriviEventoPanel = new iscriviEvento(this, controller);
        CreaEvento creaEventoPanel = new CreaEvento(this, controller);

        container.add(userAreaPanel.getMainPanel(), "HOME");
        container.add(setDatiPanel.getMainPanel(), "SET_DATI");
        container.add(iscriviEventoPanel.getMainPanel(), "ISCRIVI_EVENTO");
        container.add(creaEventoPanel.getMainPanel(), "CREA_EVENTO");

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

    public UserArea getUserArea() {
        return userAreaPanel;
    }

    public void openSelectEvento() {
        SelectEventoFrame selectEventoFrame = new SelectEventoFrame(controller);
        selectEventoFrame.setVisible(true);
        dispose();
    }

    public void logout() {
        controller.exitApplication();
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
        iscriviEventoPanel.refreshTabella();
        cardLayout.show(container, "ISCRIVI_EVENTO");
    }

    public void showCreaEvento() {
        cardLayout.show(container, "CREA_EVENTO");
    }


}