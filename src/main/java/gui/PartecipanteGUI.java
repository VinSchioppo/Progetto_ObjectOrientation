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

        // ===== SELEZIONE SINGOLA =====
        listEventi.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // ===== RESET BOTTONI =====
        creaTeamButton.setEnabled(false);
        joinTeamButton.setEnabled(false);
        teamButton.setEnabled(false);

        // ===== LISTENER SELEZIONE =====
        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int index = listEventi.getSelectedIndex();
                boolean selezionato = index != -1;

                creaTeamButton.setEnabled(selezionato);
                joinTeamButton.setEnabled(selezionato);
                teamButton.setEnabled(selezionato);
            }
        });
    }

    public void refreshListaEventi() {

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

        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                int index = listEventi.getSelectedIndex();

                boolean selezionato = index != -1;

                creaTeamButton.setEnabled(selezionato);
                joinTeamButton.setEnabled(selezionato);
                teamButton.setEnabled(selezionato);
            }
        });
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
                parentFrame.openCreaTeamDialog(idEvento);
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
            if (index == -1) return;

            int idEvento = idEventi.get(index);

            controller.selectEvento(idEvento, Role.PARTECIPANTE);

            String teamInfo = controller.teamPartecipante();

            if (teamInfo == null) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Non sei in un team per questo evento",
                        "Informazione",
                        JOptionPane.WARNING_MESSAGE
                );

                return;
            }

            parentFrame.showTeamGUI();
        });
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }

}
