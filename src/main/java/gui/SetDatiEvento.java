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

    private JSpinner Datainizio;        // Data inizio evento
    private JSpinner Datafine;        // Data fine evento

    private JSpinner Datainizioreg;        // Data inizio registrazioni
    private JSpinner Datafinereg;        // Data fine registrazioni

    private JButton saveButton;
    private JButton backButton;
    private JSpinner MaxPartecipantispinner;
    private JSpinner NumeroCivicospinner;

    private JTextArea DescrizioneProblema;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public SetDatiEvento(SelectEventoFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaSpinnerDate();
        inizializzaSpinnerDate();
        caricaDatiEvento();

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

            // ===== INPUT UTENTE =====
            String indirizzo = indirizzoSede.getText().trim();

            int nCivico = (int) NumeroCivicospinner.getValue();
            int MaxPartecipanti = (int) MaxPartecipantispinner.getValue();
            int MaxTeam = (int) maxTeamSpinner.getValue();

            // ===== DATE =====
            LocalDate dataInizioEvento = convertiData(Datainizio);
            LocalDate dataFineEvento   = convertiData(Datafine);

            LocalDate dataInizioReg = convertiData(Datainizioreg);
            LocalDate dataFineReg   = convertiData(Datafinereg);

            // ===== VALIDAZIONE =====
            if (indirizzo.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Inserisci un indirizzo valido",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            // ===== SALVATAGGIO =====
            controlloRegistrazione(
                    indirizzo,
                    nCivico,
                    MaxPartecipanti,
                    MaxTeam,
                    dataInizioEvento,
                    dataFineEvento,
                    dataInizioReg,
                    dataFineReg
            );

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

    private void caricaDatiEvento() {

        String dati = controller.datiEvento();

        if (dati == null) return;

        try {

            String[] d = dati.split(" ");

            // ===== TROVA PRIMA DATA =====
            int firstDateIndex = -1;

            for (int i = 0; i < d.length; i++) {
                if (d[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
                    firstDateIndex = i;
                    break;
                }
            }

            if (firstDateIndex == -1) {
                throw new RuntimeException("Date non trovate");
            }

            // ===== CIVICO =====
            int civicoIndex = firstDateIndex - 1;
            int civico = Integer.parseInt(d[civicoIndex]);
            NumeroCivicospinner.setValue(civico);

            // ===== TROVA INIZIO INDIRIZZO =====
            int startIndirizzo = 0;

            for (int i = 0; i < civicoIndex; i++) {
                String parola = d[i].toLowerCase();

                if (parola.equals("via") ||
                        parola.equals("viale") ||
                        parola.equals("piazza") ||
                        parola.equals("corso") ||
                        parola.equals("largo")) {

                    startIndirizzo = i;
                    break;
                }
            }

            // ===== COSTRUZIONE INDIRIZZO =====
            StringBuilder indirizzoBuilder = new StringBuilder();

            for (int i = startIndirizzo; i < civicoIndex; i++) {
                indirizzoBuilder.append(d[i]).append(" ");
            }

            indirizzoSede.setText(indirizzoBuilder.toString().trim());

            // ===== DATE EVENTO =====
            Datainizio.setValue(convertToDate(LocalDate.parse(d[firstDateIndex])));
            Datafine.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 1])));

            // ===== NUMERI =====
            MaxPartecipantispinner.setValue(Integer.parseInt(d[firstDateIndex + 2]));
            maxTeamSpinner.setValue(Integer.parseInt(d[firstDateIndex + 3]));

            // ===== DATE REG =====
            Datainizioreg.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 4])));
            Datafinereg.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 5])));

            // ===== DESCRIZIONE =====
            if (d.length > firstDateIndex + 6) {
                StringBuilder desc = new StringBuilder();
                for (int i = firstDateIndex + 6; i < d.length; i++) {
                    desc.append(d[i]).append(" ");
                }
                DescrizioneProblema.setText(desc.toString().trim());
            }

        } catch (Exception e) {
            System.out.println("Errore parsing dati evento");
            e.printStackTrace();
        }
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    private void controlloRegistrazione(String indirizzo, int nCivico, int MaxPartecipanti, int MaxTeam,
                                        LocalDate dataInizioEvento, LocalDate dataFineEvento, LocalDate dataInizioReg, LocalDate dataFineReg) {

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