package gui;

import controller.Controller;

import javax.swing.*;

public class CreaEvento {

    private JPanel mainPanel;
    private JTextField nomeEventoJTextField;     // Nome evento
    private JSpinner numeroCivicoSpinner;         // Max squadre
    private JTextArea indirizzoJtextArea;       // Descrizione
    private JButton salvaButton;
    private JButton indietroButton;

    private gui.UserAreaFrame parentFrame;
    private Controller controller;

    public CreaEvento(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaSpinner();
        inizializzaBottoni();
    }

    private void inizializzaSpinner() {

        numeroCivicoSpinner.setModel(new SpinnerNumberModel(1, 1, 300, 1));
    }

    private void inizializzaBottoni() {

        indietroButton.addActionListener(e ->
                parentFrame.showHome()
        );

        salvaButton.addActionListener(e -> {

            String nomeEvento = nomeEventoJTextField.getText().trim();
            int numeroCivico = (int) numeroCivicoSpinner.getValue();
            String indirizzo = indirizzoJtextArea.getText().trim();

            if (nomeEvento.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Il nome dell'evento è obbligatorio",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            controlloEvento(nomeEvento, numeroCivico, indirizzo);

            parentFrame.showHome();
        });
    }

    private void controlloEvento(String nomeEvento, int numeroCivico, String indirizzo) {

        boolean ok = controller.creaEvento(
                nomeEvento,
                indirizzo,
                numeroCivico
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

    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}