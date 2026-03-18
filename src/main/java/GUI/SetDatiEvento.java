package GUI;

import ClassModel.Evento;
import Controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetDatiEvento {

    private JPanel mainPanel;

    private JTextField textField1;   // Indirizzo
    private JSpinner spinner1;        // Numero civico

    private JSpinner spinner3;        // Data inizio evento
    private JSpinner spinner2;        // Data fine evento

    private JSpinner spinner4;        // Data inizio registrazioni
    private JSpinner spinner5;        // Data fine registrazioni

    private JTextArea textArea1;      // (NON usata qui, ma la lasciamo)
    private JButton saveButton;
    private JButton backButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public SetDatiEvento(SelectEventoFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaSpinnerDate();

        backButton.addActionListener(e ->
                parentFrame.showOrganizzatoreGUI()
        );

        saveButton.addActionListener(e ->
                salvaDatiEvento()
        );
    }

    /* ============================================================
       =================== INIZIALIZZAZIONE ======================
       ============================================================ */
    private void inizializzaSpinnerDate() {

        spinner2.setModel(new SpinnerDateModel());
        spinner3.setModel(new SpinnerDateModel());
        spinner4.setModel(new SpinnerDateModel());
        spinner5.setModel(new SpinnerDateModel());

        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(spinner2, "yyyy-MM-dd");
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(spinner3, "yyyy-MM-dd");
        JSpinner.DateEditor editor3 = new JSpinner.DateEditor(spinner4, "yyyy-MM-dd");
        JSpinner.DateEditor editor4 = new JSpinner.DateEditor(spinner5, "yyyy-MM-dd");

        spinner2.setEditor(editor1);
        spinner3.setEditor(editor2);
        spinner4.setEditor(editor3);
        spinner5.setEditor(editor4);
    }

    /* ============================================================
       ====================== SALVATAGGIO =========================
       ============================================================ */
    private void salvaDatiEvento() {

        Evento evento = controller.getEventoSelezionato();
        if (evento == null) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nessun evento selezionato",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        try {
            String indirizzo = textField1.getText().trim();
            int nCivico = (int) spinner1.getValue();

            // ===== DATE EVENTO =====
            LocalDate dataInizioEvento = convertiData(spinner3);
            LocalDate dataFineEvento   = convertiData(spinner2);

            // ===== DATE REGISTRAZIONI =====
            LocalDate dataInizioReg = convertiData(spinner4);
            LocalDate dataFineReg   = convertiData(spinner5);

            boolean ok1 = controller.inserisciDatiEvento(
                    indirizzo,
                    nCivico,
                    textArea1.getText()
            );

            boolean ok2 = controller.setDateEvento(
                    dataInizioEvento,
                    dataFineEvento
            );

            boolean ok3 = controller.setRegistrazioniEvento(
                    dataInizioReg,
                    dataFineReg
            );

            if (ok1 && ok2 && ok3) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Dati evento aggiornati correttamente",
                        "Successo",
                        JOptionPane.INFORMATION_MESSAGE
                );
                parentFrame.showOrganizzatoreGUI();
            } else {
                mostraErrore();
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            mostraErrore();
        }
    }

    private void mostraErrore() {
        JOptionPane.showMessageDialog(
                mainPanel,
                "Errore durante il salvataggio dei dati",
                "Errore",
                JOptionPane.ERROR_MESSAGE
        );
    }

    /* ============================================================
       ===================== UTILITIES ============================
       ============================================================ */
    private LocalDate convertiData(JSpinner spinner) {
        Date date = (Date) spinner.getValue();
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}