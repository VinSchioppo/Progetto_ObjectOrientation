package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PartecipanteGUI {

    private JPanel mainPanel;
    private JButton joinTeamButton;
    private JButton creaTeamButton;
    private JButton teamButton;
    private JButton backButton;
    private JList<String> listEventi;

    private UserAreaFrame parentFrame;
    private Controller controller;
    private List<String> eventi;
    private List<Integer> idEventi;

    public PartecipanteGUI(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaLista();
        inizializzaBottoni();

    }

    private void inizializzaLista() {

        eventi = controller.listaEventiPartecipante();
        idEventi = new ArrayList<>();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eventi != null) {
            for (String e : eventi) {

                int spazio = e.indexOf(" ");
                if (spazio == -1) continue;

                int id = Integer.parseInt(e.substring(0, spazio));
                String titolo = e.substring(spazio + 1);

                idEventi.add(id);
                model.addElement(titolo);
            }
        }

        listEventi.setModel(model);

        // ===== BLOCCA SELEZIONE MULTIPLA =====
        listEventi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ===== LISTENER =====
        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                boolean selezionato = listEventi.getSelectedIndex() != -1;

                creaTeamButton.setEnabled(selezionato);
                joinTeamButton.setEnabled(selezionato);
                teamButton.setEnabled(selezionato);
            }
        });
    }

    public void refreshListaEventi() {
        eventi = controller.listaEventiPartecipante();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (eventi != null) {
            for (String e : eventi) {
                model.addElement(e);
            }
        }

        listEventi.setModel(model);
    }

    private void inizializzaBottoni() {

        // DISABILITATI ALL’INIZIO
        creaTeamButton.setEnabled(false);
        joinTeamButton.setEnabled(false);
        teamButton.setEnabled(false);

        backButton.addActionListener(e -> parentFrame.showHome());

        creaTeamButton.addActionListener(e -> {
            int index = listEventi.getSelectedIndex();
            if (index != -1) {
                int idEvento = idEventi.get(index);
                controller.selectEvento(idEvento, Role.PARTECIPANTE);
                parentFrame.openCreaTeamDialog();
            }
        });

        joinTeamButton.addActionListener(e -> {
            int index = listEventi.getSelectedIndex();
            if (index != -1) {
                int idEvento = idEventi.get(index);
                controller.selectEvento(idEvento, Role.PARTECIPANTE);

                JoinTeamDialog dialog = new JoinTeamDialog(parentFrame, controller);
                dialog.setVisible(true);
            }
        });

        teamButton.addActionListener(e -> {
            int index = listEventi.getSelectedIndex();
            if (index != -1) {
                int idEvento = idEventi.get(index);
                controller.selectEvento(idEvento, Role.PARTECIPANTE);
                parentFrame.showTeamGUI();
            }
        });
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

}
