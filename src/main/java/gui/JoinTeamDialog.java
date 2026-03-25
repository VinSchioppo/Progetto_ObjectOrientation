package gui;

import controller.Controller;

import javax.swing.*;
import java.util.List;

public class JoinTeamDialog extends JDialog {

    private JPanel mainPanel;
    private JList<String> listTeam;
    private JButton saveButton;
    private JButton backButton;

    private Controller controller;

    public JoinTeamDialog(JFrame owner, Controller controller) {

        super(owner, "Join Team", true);

        this.controller = controller;

        setContentPane(mainPanel);
        pack();
        setLocationRelativeTo(owner);

        inizializzaLista();
        inizializzaBottoni();
    }

    // ================== INIZIALIZZA LISTA =======================

    private void inizializzaLista() {

        List<String> team = controller.listaTeamEvento();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (team != null) {
            for (String t : team) {
                model.addElement(t);
            }
        }

        listTeam.setModel(model);

        saveButton.setEnabled(false);

        listTeam.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                saveButton.setEnabled(listTeam.getSelectedIndex() != -1);
            }
        });
    }

    // ===================== BOTTONI ==============================

    private void inizializzaBottoni() {

        saveButton.addActionListener(e -> joinTeam());

        backButton.addActionListener(e -> dispose());
    }

    // ===================== JOIN TEAM ============================

    private void joinTeam() {

        String selezionato = listTeam.getSelectedValue();

        if (selezionato == null) {
            mostraErrore("Seleziona un team");
            return;
        }

        Integer idTeam = estraiId(selezionato);

        if (idTeam == null) {
            mostraErrore("Errore nel formato del team");
            return;
        }

        boolean ok = controller.joinTeam(idTeam);

        if (ok) {
            JOptionPane.showMessageDialog(
                    this,
                    "Richiesta inviata al team",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );
            dispose();
        } else {
            mostraErrore("Impossibile unirsi al team");
        }
    }

    // ===================== UTILITY ==============================

    private Integer estraiId(String valore) {

        int spazio = valore.indexOf(" ");

        if (spazio == -1) return null;

        try {
            return Integer.parseInt(valore.substring(0, spazio));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private void mostraErrore(String messaggio) {
        JOptionPane.showMessageDialog(
                this,
                messaggio,
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }
}