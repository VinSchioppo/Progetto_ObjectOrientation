package gui;

import controller.Controller;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class ProgressiGUI {

    private JPanel mainPanel;
    private JButton backButton;
    private JList<String> listprogressi;
    private JLabel votomedioValore;
    private JTextPane textPaneProgressi;
    private JButton salvaButton;
    private JTable tableVoti;
    private JList<String> listCommenti;

    private static final String ERRORE = "Errore";

    private UserAreaFrame parentFrame;
    private Controller controller;

    public ProgressiGUI(UserAreaFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaDati();
        inizializzaEventi();
        inizializzaBottoni();
    }

    /* ============================================================
       =================== DATI INIZIALI ==========================
       ============================================================ */

    private void inizializzaDati() {

        aggiornaProgressi();
        aggiornaVoti();
        aggiornaMedia();
    }

    /* ============================================================
       =================== EVENTI ================================
       ============================================================ */

    private void inizializzaEventi() {

        listprogressi.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int index = listprogressi.getSelectedIndex();

                if (index == -1) return;

                String selezionato = listprogressi.getSelectedValue();

                int spazio = selezionato.indexOf(" ");
                if (spazio == -1) return;

                int idProgresso = Integer.parseInt(selezionato.substring(0, spazio));

                aggiornaCommenti(idProgresso);
            }
        });
    }

    /* ============================================================
       =================== BOTTONI ================================
       ============================================================ */

    private void inizializzaBottoni() {

        backButton.addActionListener(e ->
                parentFrame.showTeamGUI()
        );

        salvaButton.addActionListener(e -> pubblicaProgresso());
    }

    /* ============================================================
       =================== PROGRESSI ==============================
       ============================================================ */

    private void aggiornaProgressi() {

        List<String> progressi = controller.listaProgressiTeam();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (progressi != null) {
            for (String p : progressi) {
                model.addElement(p);
            }
        }

        listprogressi.setModel(model);
    }

    private void pubblicaProgresso() {

        String testo = textPaneProgressi.getText().trim();

        if (testo.isEmpty()) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Inserisci testo progresso",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        boolean ok = controller.pubblicaProgresso(testo);

        risultatoProgresso(ok);

    }

    private void risultatoProgresso(boolean ok){

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Progresso pubblicato",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            textPaneProgressi.setText("");

            aggiornaProgressi();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore pubblicazione (devi essere team leader)",
                    ERRORE,
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    /* ============================================================
       =================== COMMENTI ===============================
       ============================================================ */

    private void aggiornaCommenti(int idProgresso) {

        List<String> commenti = controller.listaCommentiProgresso(idProgresso);

        DefaultListModel<String> model = new DefaultListModel<>();

        if (commenti != null) {
            for (String c : commenti) {
                model.addElement(c);
            }
        }

        listCommenti.setModel(model);
    }

    /* ============================================================
       =================== VOTI ===============================
       ============================================================ */

    private void aggiornaVoti() {

        List<String> voti = controller.listaVotiTeam();

        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Giudice", "Voto"}, 0
        );

        // 🔥 CASO: nessun voto
        if (voti == null || voti.isEmpty()) {

            tableVoti.setModel(model);

            return;
        }

        // ===== POPOLAMENTO =====
        popolamentoVoti(model, voti);

        tableVoti.setModel(model);
    }


    private void popolamentoVoti(DefaultTableModel model, List<String> voti) {

        for (String v : voti) {

            int spazio = v.indexOf(" ");
            if (spazio == -1) continue;

            String giudice = v.substring(0, spazio);
            String valore = v.substring(spazio + 1);

            model.addRow(new Object[]{giudice, valore});
        }

    }

    private void aggiornaMedia() {

        List<String> voti = controller.listaVotiTeam();

        if (voti == null || voti.isEmpty()) {
            votomedioValore.setText("N/A");
            return;
        }

        try {
            float media = controller.mediaVotiTeam();
            votomedioValore.setText(String.valueOf(media));
        } catch (Exception _) {
            votomedioValore.setText(ERRORE);
        }
    }

    /* ============================================================
       =================== GET PANEL ==============================
       ============================================================ */

    public JPanel getMainPanel() {
        return mainPanel;
    }
}