package gui;

import controller.Controller;

import javax.swing.*;
import java.util.List;

public class InvitaGiudice {

    private JPanel mainPanel;
    private JList<String> listGiudiciPossibili;   // invitati
    private JList<String> listGiudici;            // già giudici
    private JButton saveButton;
    private JButton backButton;
    private JList<String> listPartecipantiEvento;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public InvitaGiudice(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaListe();
        inizializzaBottoni();
    }

    /* ============================================================
       =================== INIZIALIZZAZIONE =======================
       ============================================================ */
    private void inizializzaListe() {

        // ===== GIUDICI ATTUALI =====
        List<String> giudici = controller.listaGiudiciEvento();

        DefaultListModel<String> modelGiudici = new DefaultListModel<>();
        if (giudici != null) {
            for (String g : giudici) {
                modelGiudici.addElement(g);
            }
        }
        listGiudici.setModel(modelGiudici);

        // ===== PARTECIPANTI EVENTO =====
        List<String> partecipanti = controller.listaPartecipantiEvento();

        DefaultListModel<String> modelPartecipanti = new DefaultListModel<>();
        if (partecipanti != null) {
            for (String p : partecipanti) {
                modelPartecipanti.addElement(p);
            }
        }
        listPartecipantiEvento.setModel(modelPartecipanti);

        // ===== INVITATI (placeholder) =====
        DefaultListModel<String> modelInvitati = new DefaultListModel<>();
        listGiudiciPossibili.setModel(modelInvitati);
    }

    /* ============================================================
       ======================= BOTTONI =============================
       ============================================================ */
    private void inizializzaBottoni() {

        saveButton.setEnabled(false);

        backButton.addActionListener(e ->
                parentFrame.showOrganizzatoreGUI()
        );

        // Abilita save solo se selezioni un partecipante
        listPartecipantiEvento.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                saveButton.setEnabled(listPartecipantiEvento.getSelectedIndex() != -1);
            }
        });

        // ===== AZIONE SAVE =====
        saveButton.addActionListener(e -> {

            String selezionato = listPartecipantiEvento.getSelectedValue();

            if (selezionato == null) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Seleziona un partecipante",
                        "Errore",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            boolean ok = controller.invitaGiudice(selezionato);

            if (ok) {

                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Invito inviato",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );

                // Aggiorna lista invitati (UI locale)
                DefaultListModel<String> modelInvitati =
                        (DefaultListModel<String>) listGiudiciPossibili.getModel();

                modelInvitati.addElement(selezionato);

            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Errore durante l'invito",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}