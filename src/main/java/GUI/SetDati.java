package GUI;

import Controller.Controller;

import javax.swing.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class SetDati {

    private JPanel mainPanel;
    private JTextField nomeNuovoField;
    private JTextField secondoNomeNuovoField;
    private JTextField cognomeNuovoField;
    private JSpinner dataNascitaNuovaSpinner;
    private JButton saveButton;
    private JButton backButton;

    private GUI.UserAreaFrame parentFrame;

    public SetDati(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;

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

        // ===== BACK =====
        backButton.addActionListener(e -> parentFrame.showHome());

        // ===== SAVE =====
        saveButton.addActionListener(e -> {

            String nome = nomeNuovoField.getText().trim();
            String secondoNome = secondoNomeNuovoField.getText().trim();
            String cognome = cognomeNuovoField.getText().trim();

            // Validazione obbligatori
            if (nome.isEmpty() || cognome.isEmpty()) {
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
                parentFrame.showHome();
            } else {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Errore durante l'aggiornamento dei dati"
                );
            }
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}