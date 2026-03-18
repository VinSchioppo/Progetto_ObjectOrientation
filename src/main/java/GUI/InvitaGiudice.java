package GUI;

import ClassModel.Evento;
import Controller.*;

import javax.swing.*;
import java.util.ArrayList;

public class InvitaGiudice {

    private JPanel mainPanel;
    private JList<String> listGiudiciPossibili;
    private JList<String> listGiudic;
    private JButton saveButton;
    private JButton backButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private Evento evento;

    public InvitaGiudice(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        saveButton.addActionListener(e -> invitaGiudice());

        backButton.addActionListener(e ->
                parentFrame.showOrganizzatoreGUI()
        );
    }


    public void caricaListe() {


        evento = controller.getEventoSelezionato();

        if (evento == null) {
            System.out.println("Nessun evento selezionato");
            return;
        }

        System.out.println("Evento selezionato: " + evento.getTitolo());


        if (evento == null)
            return;

        /* ===== GIUDICI EVENTO ===== */

        DefaultListModel<String> modelGiudici = new DefaultListModel<>();

        ArrayList<String> giudici = controller.listaGiudiciEvento(evento.getIdEvento());

        if (giudici != null) {
            for (String g : giudici) {
                modelGiudici.addElement(g);
            }
        }

        listGiudic.setModel(modelGiudici);

        /* ===== GIUDICI POSSIBILI ===== */

        DefaultListModel<String> modelPossibili = new DefaultListModel<>();

        ArrayList<String> possibili = controller.listaUtentiPossibiliGiudici(evento.getIdEvento());

        if (possibili != null) {
            for (String u : possibili) {
                modelPossibili.addElement(u);
            }
        }

        listGiudiciPossibili.setModel(modelPossibili);
    }

    private void invitaGiudice() {

        int index = listGiudiciPossibili.getSelectedIndex();

        if (index == -1) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona un utente",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        String nomeGiudice = listGiudiciPossibili.getSelectedValue();

        boolean ok = controller.invitaGiudice(
                nomeGiudice,
                evento.getIdEvento()
        );

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Giudice invitato correttamente",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            caricaListe();

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore nell'invito del giudice",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}