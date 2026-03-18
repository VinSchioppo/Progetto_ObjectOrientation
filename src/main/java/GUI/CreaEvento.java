package GUI;

import Controller.*;

import javax.swing.*;

public class CreaEvento {

    private JPanel mainPanel;
    private JTextField textField1;     // Nome evento
    private JSpinner spinner1;         // Max membri team
    private JSpinner spinner2;         // Max squadre
    private JTextArea textArea1;       // Descrizione
    private JButton salvaButton;
    private JButton indietroButton;

    private UserAreaFrame parentFrame;
    private Controller controller;

    public CreaEvento(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaSpinner();
        inizializzaBottoni();
    }

    private void inizializzaSpinner() {

        spinner1.setModel(new SpinnerNumberModel(1, 1, 10, 1));   // membri team
        spinner2.setModel(new SpinnerNumberModel(1, 1, 100, 1));  // max squadre
    }

    private void inizializzaBottoni() {

        indietroButton.addActionListener(e ->
                parentFrame.showHome()
        );

        salvaButton.addActionListener(e -> {

            String nomeEvento = textField1.getText().trim();
            int maxTeam = (int) spinner1.getValue();
            int maxSquadre = (int) spinner2.getValue();
            String descrizione = textArea1.getText().trim();

            if (nomeEvento.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Il nome dell'evento è obbligatorio",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean ok = controller.creaEventoBase(
                    nomeEvento,
                    maxTeam,
                    maxSquadre,
                    descrizione
            );

            if (!ok) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Errore durante la creazione dell'evento",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Evento creato correttamente.\nCompleta ora i dati dell'evento.",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            parentFrame.showHome();
        });
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}