package GUI;

import ClassModel.Evento;
import ClassModel.Team;
import Controller.Controller;

import javax.swing.*;
import java.util.ArrayList;

public class TeamGUI {

    private JList<String> listTeam;
    private JButton backButton;
    private JButton invitaTeamButton;
    private JPanel mainPanel;
    private JLabel NomeEvento;
    private JLabel DescrizioneEvento;
    private JList<String> listPartecipantiTeam;
    private JButton lasciaTeamButton;
    private JButton progressiButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    private ArrayList<Team> teamUtente;

    public TeamGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        caricaTeam();

        progressiButton.addActionListener(e -> apriProgressi());

        backButton.addActionListener(e ->
                parentFrame.showPartecipanteGUI()
        );

        listTeam.addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int index = listTeam.getSelectedIndex();

                if (index != -1) {
                    mostraDettagliTeam(teamUtente.get(index));
                }
            }
        });

        invitaTeamButton.addActionListener(e -> invitaPartecipante());
        lasciaTeamButton.addActionListener(e -> lasciaTeam());

    }

    /* ======================= TEAM ======================= */

    private void caricaTeam() {

        DefaultListModel<String> model = new DefaultListModel<>();

        teamUtente = controller.listaTeamUtente();

        if (teamUtente != null) {

            for (Team t : teamUtente) {
                model.addElement(t.getNome());
            }
        }

        listTeam.setModel(model);
    }

    /* ======================= DETTAGLI TEAM ======================= */

    private void mostraDettagliTeam(Team team) {

        /* EVENTO */

        Evento evento = controller.getEventoTeam(team.getIdTeam());

        if (evento != null) {

            NomeEvento.setText(evento.getTitolo());
            DescrizioneEvento.setText(evento.getDescrizioneProblema());
        }

        /* MEMBRI TEAM */

        DefaultListModel<String> model = new DefaultListModel<>();

        ArrayList<String> membri = controller.listaMembriTeam(team.getIdTeam());

        if (membri != null) {

            for (String m : membri) {
                model.addElement(m);
            }
        }

        listPartecipantiTeam.setModel(model);
    }

    private void invitaPartecipante() {

        int index = listTeam.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona prima un team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Team team = teamUtente.get(index);

        // recupera partecipanti disponibili
        ArrayList<String> possibili = controller.listaPartecipantiDisponibili(team.getIdTeam());

        if (possibili == null || possibili.isEmpty()) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Nessun partecipante disponibile per questo evento",
                    "Informazione",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        // crea lista selezionabile
        JList<String> lista = new JList<>(possibili.toArray(new String[0]));
        JScrollPane scroll = new JScrollPane(lista);

        int scelta = JOptionPane.showConfirmDialog(
                mainPanel,
                scroll,
                "Seleziona partecipante",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (scelta != JOptionPane.OK_OPTION)
            return;

        String nome = lista.getSelectedValue();

        if (nome == null)
            return;

        boolean ok = controller.inviaRichiestaTeam(nome, team.getIdTeam());

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Partecipante aggiunto al team",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            mostraDettagliTeam(team);

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore nell'inserimento",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void lasciaTeam() {

        int index = listTeam.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona un team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        Team team = teamUtente.get(index);

        int scelta = JOptionPane.showConfirmDialog(
                mainPanel,
                "Vuoi davvero lasciare il team?",
                "Conferma",
                JOptionPane.YES_NO_OPTION
        );

        if (scelta != JOptionPane.YES_OPTION)
            return;

        boolean ok = controller.lasciaTeam(team.getIdTeam());

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Hai lasciato il team",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            caricaTeam(); // aggiorna la lista

        } else {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Errore durante l'operazione",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void apriProgressi() {

        int index = listTeam.getSelectedIndex();

        if (index == -1) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Seleziona prima un team",
                    "Errore",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        Team team = teamUtente.get(index);

        parentFrame.showProgressiGUI(team.getIdTeam());
    }


    public JPanel getMainPanel() {
        return mainPanel;
    }
}