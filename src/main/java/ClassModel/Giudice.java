package ClassModel;

public class Giudice extends Utente {

    private Evento EventoGiudicato = null;

    public Giudice() {}

    public Giudice(String NomeUtente, String PasswordUtente) {

        super(NomeUtente, PasswordUtente);

    }

    public void setEvento(Evento evento) {
        this.EventoGiudicato = evento;
        evento.addGiudice(this);
    }

    public int getIdEventoGiudicato() {return EventoGiudicato.getIdEvento();}

    public void pubblicaProblema(String Problema) {

        EventoGiudicato.setDescrizioneProblema(Problema);
    }

    private void commentaProgresso(Progresso progresso, String commento) {

        Commento CommentoProgresso = new Commento(progresso.getIdProgresso(),commento, this.getNomeUtente());
        CommentoProgresso.setGiudice(this.NomeUtente);
        progresso.addCommento(CommentoProgresso);

    }

    private void daiVoto(Team team, int valore) {

        Voto voto = new Voto(team.getIdTeam(), valore, this.getNomeUtente());
        voto.setGiudice(this.NomeUtente);
        team.addVoto(voto);

    }
}