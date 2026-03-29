package gui;

import controller.Controller;
import controller.Role;

import javax.swing.*;
import java.util.List;

public class GiudiceGUI {

    private JPanel mainPanel;
    private JButton backButton;
    private JButton saveButton;
    private JList<String> listEventi;
    private JList<String> listTeam;

    private JSpinner voto;
    private JTextPane giudizio;
    private JCheckBox abilitaVoto;
    private JCheckBox abilitaGiudizio;
    private JList<String> listaProgressi;
    private JList<String> listVotiTeam;
    private JButton pubblicaProblemaButton;
    private JButton commentiPassatiButton;

    private SelectEventoFrame parentFrame;
    private Controller controller;

    public GiudiceGUI(SelectEventoFrame parentFrame, Controller controller) {

        this.parentFrame = parentFrame;
        this.controller = controller;

        inizializzaComponenti();
        inizializzaListe();
        inizializzaBottoni();
    }

    /* ================= COMPONENTI ================= */

    private void inizializzaComponenti() {

        voto.setModel(new SpinnerNumberModel(1, 1, 10, 1));

        voto.setEnabled(false);
        giudizio.setEnabled(false);

        abilitaVoto.addActionListener(e -> {
            voto.setEnabled(abilitaVoto.isSelected());
            aggiornaStatoSave();
        });

        abilitaGiudizio.addActionListener(e -> {
            giudizio.setEnabled(abilitaGiudizio.isSelected());
            aggiornaStatoSave();
        });

        giudizio.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                aggiornaStatoSave();
            }
        });

        saveButton.setEnabled(false);
    }

    /* ================= LISTE ================= */

    private void inizializzaListe() {

        // ===== EVENTI =====
        listaEventi();

        // ===== TEAM =====
        listaTeam();

    }

    private void listaEventi(){

        List<String> eventi = controller.listaEventiGiudice();
        DefaultListModel<String> modelEventi = new DefaultListModel<>();

        if (eventi != null) {
            for (String e : eventi) modelEventi.addElement(e);
        }

        listEventi.setModel(modelEventi);

        listEventi.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = listEventi.getSelectedIndex();
                if (row != -1) {
                    selezionaEvento(row);
                    aggiornaTeam();
                }
            }
        });

    }

    private void listaTeam(){

        listTeam.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {

                String team = listTeam.getSelectedValue();
                if (team == null) return;

                Integer idTeam = estraiId(team);
                if (idTeam == null) return;

                aggiornaProgressi(idTeam);
                aggiornaVoti(idTeam);
            }
        });

    }

    /* ================= SELEZIONE EVENTO ================= */

    private void selezionaEvento(int row) {

        String evento = listEventi.getModel().getElementAt(row);
        Integer id = estraiId(evento);

        if (id != null) {
            controller.selectEvento(id, Role.GIUDICE);
        }
    }

    /* ================= AGGIORNAMENTI ================= */

    private void aggiornaTeam() {

        List<String> team = controller.listaTeamGiudicati();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (team != null) {
            for (String t : team) model.addElement(t);
        }

        listTeam.setModel(model);

        listaProgressi.setModel(new DefaultListModel<>());
        listVotiTeam.setModel(new DefaultListModel<>());
    }

    private void aggiornaProgressi(int idTeam) {

        List<String> progressi = controller.listaProgressiTeamGiudicato(idTeam);

        DefaultListModel<String> model = new DefaultListModel<>();

        if (progressi != null) {
            for (String p : progressi) model.addElement(p);
        }

        listaProgressi.setModel(model);
    }

    private void aggiornaVoti(int idTeam) {

        List<String> voti = controller.listaVotiTeamGiudicati();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (voti != null) {
            for (String v : voti) {

                Integer id = estraiId(v);

                if (id != null && id == idTeam) {
                    model.addElement(v);
                }
            }
        }

        listVotiTeam.setModel(model);
    }

    /* ================= LOGICA SAVE ================= */

    private void aggiornaStatoSave() {

        boolean votoAttivo = abilitaVoto.isSelected();
        boolean giudizioAttivo = abilitaGiudizio.isSelected();

        boolean votoValido = votoAttivo;
        boolean giudizioValido = giudizioAttivo && !giudizio.getText().trim().isEmpty();

        saveButton.setEnabled(votoValido || giudizioValido);
    }

    /* ================= BOTTONI ================= */

    private void inizializzaBottoni() {

        backButton.addActionListener(e -> parentFrame.showHome());

        saveButton.addActionListener(e -> salvaValutazione());

        pubblicaProblemaButton.addActionListener(e -> {

            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel);

            PubblicaProblemaDialog dialog =
                    new PubblicaProblemaDialog(frame, controller);

            dialog.setVisible(true);
        });

        commentiPassatiButton.addActionListener(e -> mostraCommentiPassati());

    }

    private void mostraCommentiPassati() {

        List<String> commenti = controller.listaCommenti();

        DefaultListModel<String> model = new DefaultListModel<>();

        if (commenti != null) {
            for (String c : commenti) {
                model.addElement(c);
            }
        }

        // 🔥 Lista visualizzazione
        JList<String> list = new JList<>(model);

        JScrollPane scrollPane = new JScrollPane(list);
        scrollPane.setPreferredSize(new java.awt.Dimension(400, 300));

        JOptionPane.showMessageDialog(
                mainPanel,
                scrollPane,
                "Commenti già inseriti",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /* ================= SAVE ================= */

    private void salvaValutazione() {

        String teamStr = listTeam.getSelectedValue();
        String progStr = listaProgressi.getSelectedValue();

        if (teamStr == null) {
            mostraErrore("Seleziona un team");
            return;
        }

        Integer idTeam = estraiId(teamStr);
        Integer idProg = progStr != null ? estraiId(progStr) : null;

        if (idTeam == null) {
            mostraErrore("Errore ID team");
            return;
        }

        boolean ok = true;

        // ===== VOTO =====
        salvaVoto(idTeam);

        // ===== COMMENTO =====
        salvaCommento(idProg);

        messaggioRisultato(ok, idTeam);
    }

    private void salvaVoto(int idTeam){

        if (abilitaVoto.isSelected()) {
            int votoVal = (Integer) voto.getValue();
            controller.giveVotoTeam(idTeam, votoVal);
        }
    }

    private void salvaCommento(Integer idProg) {

        if (abilitaGiudizio.isSelected()) {

            if (idProg == null) {
                mostraErrore("Seleziona un progresso");
                return;
            }

            String testo = giudizio.getText().trim();

            if (testo.isEmpty()) {
                mostraErrore("Scrivi un commento");
                return;
            }

            controller.commentaProgresso(idProg, testo);
        }
    }

    private void messaggioRisultato(boolean ok, int idTeam) {

        if (ok) {

            JOptionPane.showMessageDialog(
                    mainPanel,
                    "Salvato",
                    "Successo",
                    JOptionPane.INFORMATION_MESSAGE
            );

            aggiornaVoti(idTeam);

        } else {
            mostraErrore("Errore durante il salvataggio");
        }

    }

    /* ================= UTILITY ================= */

    private Integer estraiId(String valore) {

        int spazio = valore.indexOf(" ");
        if (spazio == -1) return null;

        try {
            return Integer.parseInt(valore.substring(0, spazio));
        } catch (Exception _) {
            return null;
        }
    }

    private void mostraErrore(String msg) {
        JOptionPane.showMessageDialog(mainPanel, msg, "Errore", JOptionPane.ERROR_MESSAGE);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}