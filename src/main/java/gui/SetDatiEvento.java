package gui;

import controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetDatiEvento {

    private JPanel mainPanel;

    private JTextField indirizzoSede;   // Indirizzo
    private JSpinner maxTeamSpinner;        // Numero civico

    private JSpinner datainizio;        // Data inizio evento
    private JSpinner datafine;        // Data fine evento

    private JSpinner datainizioreg;        // Data inizio registrazioni
    private JSpinner datafinereg;        // Data fine registrazioni

    private JButton saveButton;
    private JButton backButton;
    private JSpinner maxPartecipantispinner;
    private JSpinner numeroCivicospinner;

    private static final String DATEFORMAT = "yyyy-MM-dd";

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

        datafine.setModel(new SpinnerDateModel());
        datainizio.setModel(new SpinnerDateModel());
        datainizioreg.setModel(new SpinnerDateModel());
        datafinereg.setModel(new SpinnerDateModel());

        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(datafine, DATEFORMAT);
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(datainizio, DATEFORMAT);
        JSpinner.DateEditor editor3 = new JSpinner.DateEditor(datainizioreg, DATEFORMAT);
        JSpinner.DateEditor editor4 = new JSpinner.DateEditor(datafinereg, DATEFORMAT);

        datafine.setEditor(editor1);
        datainizio.setEditor(editor2);
        datainizioreg.setEditor(editor3);
        datafinereg.setEditor(editor4);

        numeroCivicospinner.setModel(new SpinnerNumberModel(1, 1, 300, 1));
        maxTeamSpinner.setModel(new SpinnerNumberModel(1,1,50,1));
        maxPartecipantispinner.setModel(new SpinnerNumberModel(1,1,200,1));
    }

    /* ============================================================
       ====================== SALVATAGGIO =========================
       ============================================================ */
    private void salvaDatiEvento() {

        try {
            String indirizzo = indirizzoSede.getText().trim();
            int nCivico = (int) numeroCivicospinner.getValue();
            int maxPartecipanti = (int) maxPartecipantispinner.getValue();
            int maxTeam = (int) maxTeamSpinner.getValue();

            // ===== DATE EVENTO =====
            LocalDate dataInizioEvento = convertiData(datainizio);
            LocalDate dataFineEvento   = convertiData(datafine);

            // ===== DATE REGISTRAZIONI =====
            LocalDate dataInizioReg = convertiData(datainizioreg);
            LocalDate dataFineReg   = convertiData(datafinereg);

            boolean ok1 = controller.inserisciDatiEvento(
                    indirizzo,
                    nCivico,
                    maxPartecipanti,
                    maxTeam
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