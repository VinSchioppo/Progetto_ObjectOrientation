package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;
import java.util.List;

public class GiudiceGUI {

    private JPanel mainPanel;
    private JButton backButton;
    private JButton saveButton;
    private JList<String> listEventi;
    private JList<String> listTeam;
    private JList<String> listProgressi;

    private JSpinner voto;
    private JTextPane giudizio;
    private JCheckBox abilitaVoto;
    private JCheckBox abilitaGiudizio;
    private JList<String> listaProgressi;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public GiudiceGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaComponenti();
        inizializzaListe();
        inizializzaBottoni();
    }

    /* ================= COMPONENTI ================= */

    private void inizializzaComponenti() {

        voto.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        voto.setEnabled(false);
        giudizio.setEnabled(false);

        abilitaVoto.addActionListener(e ->
                voto.setEnabled(abilitaVoto.isSelected())
        );

        abilitaGiudizio.addActionListener(e ->
                giudizio.setEnabled(abilitaGiudizio.isSelected())
        );

        saveButton.setEnabled(false);
    }

    /* ================= LISTE ================= */

    private void inizializzaListe() {

        // EVENTI
        List<String> eventi = controller.listaEventiGiudice();
        DefaultListModel<String> modelEventi = new DefaultListModel<>();

        if (eventi != null) {
            for (String e : eventi) modelEventi.addElement(e);
        }

        listEventi.setModel(modelEventi);

        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = listEventi.getSelectedIndex();
                if (row != -1) {
                    selezionaEvento(row);
                    aggiornaTeam();
                }
            }
        });

        // TEAM
        listTeam.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                String team = listTeam.getSelectedValue();
                if (team != null) {
                    Integer idTeam = estraiId(team);
                    if (idTeam != null) {
                        aggiornaProgressi(idTeam);
                    }
                }
            }
        });

        // PROGRESSI
        listProgressi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                saveButton.setEnabled(listProgressi.getSelectedIndex() != -1);
            }
        });
    }

    /* ================= SELEZIONE EVENTO ================= */

    private void selezionaEvento(int row) {

        String evento = listEventi.getModel().getElementAt(row);
        Integer id = estraiId(evento);

        if (id != null) {
            controller.selectEvento(id, Role.GIUDICE);
        }
    }

    /* ================= AGGIORNAMENTI ================= */

    private void aggiornaTeam() {

        List<String> team = controller.listaTeamEvento();
        DefaultListModel<String> model = new DefaultListModel<>();

        if (team != null) {
            for (String t : team) model.addElement(t);
        }

        listTeam.setModel(model);
        listProgressi.setModel(new DefaultListModel<>());
    }

    private void aggiornaProgressi(int idTeam) {

        List<String> progressi = controller.listaProgressiTeamGiudicato(idTeam);
        DefaultListModel<String> model = new DefaultListModel<>();

        if (progressi != null) {
            for (String p : progressi) model.addElement(p);
        }

        listProgressi.setModel(model);
    }

    /* ================= BOTTONI ================= */

    private void inizializzaBottoni() {

        backButton.addActionListener(e -> parentFrame.showHome());

        saveButton.addActionListener(e -> salvaValutazione());
    }

    /* ================= SAVE ================= */

    private void salvaValutazione() {

        String teamStr = listTeam.getSelectedValue();
        String progStr = listProgressi.getSelectedValue();

        if (teamStr == null || progStr == null) {
            mostraErrore("Seleziona team e progresso");
            return;
        }

        Integer idTeam = estraiId(teamStr);
        Integer idProg = estraiId(progStr);

        if (idTeam == null || idProg == null) {
            mostraErrore("Errore formato dati");
            return;
        }

        boolean ok = true;

        // VOTO
        if (abilitaVoto.isSelected()) {
            int votoVal = (Integer) voto.getValue();
            ok &= controller.giveVotoTeam(idTeam, votoVal);
        }

        // COMMENTO
        if (abilitaGiudizio.isSelected()) {
            String testo = giudizio.getText();
            ok &= controller.commentaProgresso(idProg, testo);
        }

        if (ok) {
            JOptionPane.showMessageDialog(mainPanel, "Salvato", "Successo", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostraErrore("Errore durante il salvataggio");
        }
    }

    /* ================= UTILITY ================= */

    private Integer estraiId(String valore) {
        int spazio = valore.indexOf(" ");
        if (spazio == -1) return null;

        try {
            return Integer.parseInt(valore.substring(0, spazio));
        } catch (Exception e) {
            return null;
        }
    }

    private void mostraErrore(String msg) {
        JOptionPane.showMessageDialog(mainPanel, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}