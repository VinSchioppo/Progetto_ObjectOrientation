package gui;

import controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SetDati {

    private JPanel mainPanel;
    private JTextField nomeNuovoField;
    private JTextField secondoNomeNuovoField;
    private JTextField cognomeNuovoField;
    private JSpinner dataNascitaNuovaSpinner;
    private JButton saveButton;
    private JButton backButton;

    private gui.UserAreaFrame parentFrame;
    private Controller controller;

    private static final Logger logger = Logger.getLogger(SetDati.class.getName());

    public SetDati(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        // ===== JSpinner per data di nascita =====
        Calendar cal = Calendar.getInstance();
        cal.set(2000, Calendar.JANUARY, 1);

        SpinnerDateModel dateModel = new SpinnerDateModel(
                cal.getTime(),
                null,
                null,
                Calendar.DAY_OF_MONTH
        );

        dataNascitaNuovaSpinner.setModel(dateModel);
        dataNascitaNuovaSpinner.setEditor(
                new JSpinner.DateEditor(dataNascitaNuovaSpinner, "dd/MM/yyyy")
        );
        caricaDatiUtente();

        // ===== BACK =====
        backButton.addActionListener(e -> parentFrame.showHome());

        // ===== SAVE =====
        saveButton.addActionListener(e -> {

            String nome = nomeNuovoField.getText().trim();
            String secondoNome = secondoNomeNuovoField.getText().trim();
            String cognome = cognomeNuovoField.getText().trim();

            // Validazione obbligatori
            String dati = controller.datiUtente();

            if (dati == null && (nome.isEmpty() || cognome.isEmpty())) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Nome e cognome sono obbligatori"
                );
                return;
            }

            // Secondo nome opzionale
            if (secondoNome.isEmpty()) {
                secondoNome = null;
            }

            Date date = (Date) dataNascitaNuovaSpinner.getValue();
            LocalDate dataNascita = date.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate();


            messaggioRisposta(nome, secondoNome, cognome, dataNascita, controller);
        });
    }

    private void caricaDatiUtente() {

        String dati = controller.datiUtente();

        if (dati == null) return;

        try {

            String[] parti = dati.split(" ", 4);

            // ===== NOME =====
            if (parti.length > 0 && !parti[0].equals("null"))
                nomeNuovoField.setText(parti[0]);

            // ===== SECONDO NOME =====
            if (parti.length > 1 && !parti[1].equals("null"))
                secondoNomeNuovoField.setText(parti[1]);

            // ===== COGNOME =====
            if (parti.length > 2 && !parti[2].equals("null"))
                cognomeNuovoField.setText(parti[2]);

            // ===== DATA =====
            if (parti.length > 3 && !parti[3].equals("null")) {

                LocalDate data = LocalDate.parse(parti[3]);

                Date date = Date.from(
                        data.atStartOfDay(ZoneId.systemDefault()).toInstant()
                );

                dataNascitaNuovaSpinner.setValue(date);
            }

        } catch (Exception e) {
            logger.info("Errore parsing dati utente");
            logger.info(e.getMessage());
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
    }

    private void messaggioRisposta(String nome, String secondoNome, String cognome, LocalDate dataNascita, Controller controller) {

        boolean ok = controller.inserisciDatiUtente(
                nome,
                secondoNome,
                cognome,
                dataNascita
        );

        if (ok) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Dati aggiornati correttamente"
            );
            parentFrame.getUserArea().updateDatiUtente(
                    nome,
                    secondoNome,
                    cognome,
                    dataNascita.toString()
            );
            parentFrame.showHome();
        } else {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante l'aggiornamento dei dati"
            );
        }

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}