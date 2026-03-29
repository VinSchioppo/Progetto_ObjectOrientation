package gui;

import controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    private JTextArea descrizioneProblema;

    private static final String DATEFORMAT = "yyyy-MM-dd";

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private static final Logger logger = Logger.getLogger(SetDatiEvento.class.getName());

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

            // ===== INPUT UTENTE =====
            String indirizzo = indirizzoSede.getText().trim();

            int nCivico = (int) numeroCivicospinner.getValue();
            int maxPartecipanti = (int) maxPartecipantispinner.getValue();
            int maxTeam = (int) maxTeamSpinner.getValue();

            // ===== DATE =====
            LocalDate dataInizioEvento = convertiData(datainizio);
            LocalDate dataFineEvento   = convertiData(datafine);

            LocalDate dataInizioReg = convertiData(datainizioreg);
            LocalDate dataFineReg   = convertiData(datafinereg);

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
                    maxPartecipanti,
                    maxTeam,
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
            numeroCivicospinner.setValue(civico);

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
            datainizio.setValue(convertToDate(LocalDate.parse(d[firstDateIndex])));
            datafine.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 1])));

            // ===== NUMERI =====
            maxPartecipantispinner.setValue(Integer.parseInt(d[firstDateIndex + 2]));
            maxTeamSpinner.setValue(Integer.parseInt(d[firstDateIndex + 3]));

            // ===== DATE REG =====
            datainizioreg.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 4])));
            datafinereg.setValue(convertToDate(LocalDate.parse(d[firstDateIndex + 5])));

            // ===== DESCRIZIONE =====
            if (d.length > firstDateIndex + 6) {
                StringBuilder desc = new StringBuilder();
                for (int i = firstDateIndex + 6; i < d.length; i++) {
                    desc.append(d[i]).append(" ");
                }
                descrizioneProblema.setText(desc.toString().trim());
            }

        } catch (Exception e) {
            logger.info("Errore parsing dati utente");
            logger.info(e.getMessage());
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
    }

    private Date convertToDate(LocalDate localDate) {
        return Date.from(
                localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()
        );
    }

    private void controlloRegistrazione(String indirizzo, int nCivico, int maxPartecipanti, int maxTeam,
                                        LocalDate dataInizioEvento, LocalDate dataFineEvento, LocalDate dataInizioReg, LocalDate dataFineReg) {

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