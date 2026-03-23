package GUI;

import Controller.Controller;

import javax.swing.*;

public class CreaEvento {

    private JPanel mainPanel;
    private JTextField NomeEventoJTextField;     // Nome evento
    private JSpinner NumeroCivicoSpinner;         // Max squadre
    private JTextArea IndirizzoJtextArea;       // Descrizione
    private JButton salvaButton;
    private JButton indietroButton;

    private GUI.UserAreaFrame parentFrame;
    private Controller controller;

    public CreaEvento(UserAreaFrame parentFrame, Controller controller) {
        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaSpinner();
        inizializzaBottoni();
    }

    private void inizializzaSpinner() {

        NumeroCivicoSpinner.setModel(new SpinnerNumberModel(1, 1, 300, 1));
    }

    private void inizializzaBottoni() {

        indietroButton.addActionListener(e ->
                parentFrame.showHome()
        );

        salvaButton.addActionListener(e -> {

            String nomeEvento = NomeEventoJTextField.getText().trim();
            int NumeroCivico = (int) NumeroCivicoSpinner.getValue();
            String indirizzo = IndirizzoJtextArea.getText().trim();

            if (nomeEvento.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainPanel,
                        "Il nome dell'evento è obbligatorio",
                        "Errore",
                        JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            boolean ok = controller.creaEvento(
                    nomeEvento,
                    indirizzo,
                    NumeroCivico
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