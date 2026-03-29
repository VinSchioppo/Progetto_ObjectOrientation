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
    private DefaultListModel<String> modelInvitati = new DefaultListModel<>();

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

        // ===== GIUDICI =====
        List<String> giudici = controller.listaGiudiciEvento();
        DefaultListModel<String> modelGiudici = new DefaultListModel<>();

        if (giudici != null) {
            for (String g : giudici) {
                modelGiudici.addElement(g);
            }
        }

        listGiudici.setModel(modelGiudici);

        // ===== PARTECIPANTI FILTRATI =====
        filtraPartecipantiEvento(giudici);

        // ===== INVITATI =====
        listGiudiciPossibili.setModel(modelInvitati);
    }

    private void filtraPartecipantiEvento(List<String> giudici) {

        List<String> partecipanti = controller.listaPartecipantiEvento();
        DefaultListModel<String> modelPartecipanti = new DefaultListModel<>();

        if (partecipanti != null) {
            for (String p : partecipanti) {

                boolean isGiudice = giudici != null && giudici.contains(p);
                boolean isInvitato = modelInvitati.contains(p);

                if (!isGiudice && !isInvitato) {
                    modelPartecipanti.addElement(p);
                }
            }
        }
        listPartecipantiEvento.setModel(modelPartecipanti);
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
        abilitazionePartecipantiEvento();

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

            salvaInvitoGiudice(ok, selezionato);
        });
    }

    private void abilitazionePartecipantiEvento() {

        listPartecipantiEvento.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                saveButton.setEnabled(listPartecipantiEvento.getSelectedIndex() != -1);
            }
        });

    }

    private void salvaInvitoGiudice(boolean ok, String selezionato) {

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Invito inviato",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            // ===== AGGIUNGI A INVITATI =====
            modelInvitati.addElement(selezionato);

            // ===== RIMUOVI DAI PARTECIPANTI =====
            DefaultListModel<String> modelPartecipanti =
                    (DefaultListModel<String>) listPartecipantiEvento.getModel();

            modelPartecipanti.removeElement(selezionato);

        } else {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante l'invito",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}