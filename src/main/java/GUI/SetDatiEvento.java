package GUI;

import Controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class SetDatiEvento {

    private JPanel mainPanel;

    private JTextField indirizzoSede;   // Indirizzo
    private JSpinner maxTeamSpinner;        // Numero civico

    private JSpinner Datainizio;        // Data inizio evento
    private JSpinner Datafine;        // Data fine evento

    private JSpinner Datainizioreg;        // Data inizio registrazioni
    private JSpinner Datafinereg;        // Data fine registrazioni

    private JButton saveButton;
    private JButton backButton;
    private JSpinner MaxPartecipantispinner;
    private JSpinner NumeroCivicospinner;

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

        Datafine.setModel(new SpinnerDateModel());
        Datainizio.setModel(new SpinnerDateModel());
        Datainizioreg.setModel(new SpinnerDateModel());
        Datafinereg.setModel(new SpinnerDateModel());

        JSpinner.DateEditor editor1 = new JSpinner.DateEditor(Datafine, "yyyy-MM-dd");
        JSpinner.DateEditor editor2 = new JSpinner.DateEditor(Datainizio, "yyyy-MM-dd");
        JSpinner.DateEditor editor3 = new JSpinner.DateEditor(Datainizioreg, "yyyy-MM-dd");
        JSpinner.DateEditor editor4 = new JSpinner.DateEditor(Datafinereg, "yyyy-MM-dd");

        Datafine.setEditor(editor1);
        Datainizio.setEditor(editor2);
        Datainizioreg.setEditor(editor3);
        Datafinereg.setEditor(editor4);

        NumeroCivicospinner.setModel(new SpinnerNumberModel(1, 1, 300, 1));
        maxTeamSpinner.setModel(new SpinnerNumberModel(1,1,50,1));
        MaxPartecipantispinner.setModel(new SpinnerNumberModel(1,1,200,1));
    }

    /* ============================================================
       ====================== SALVATAGGIO =========================
       ============================================================ */
    private void salvaDatiEvento() {

        try {
            String indirizzo = indirizzoSede.getText().trim();
            int nCivico = (int) NumeroCivicospinner.getValue();
            int MaxPartecipanti = (int) MaxPartecipantispinner.getValue();
            int MaxTeam = (int) maxTeamSpinner.getValue();

            // ===== DATE EVENTO =====
            LocalDate dataInizioEvento = convertiData(Datainizio);
            LocalDate dataFineEvento   = convertiData(Datafine);

            // ===== DATE REGISTRAZIONI =====
            LocalDate dataInizioReg = convertiData(Datainizioreg);
            LocalDate dataFineReg   = convertiData(Datafinereg);

            boolean ok1 = controller.inserisciDatiEvento(
                    indirizzo,
                    nCivico,
                    MaxPartecipanti,
                    MaxTeam
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