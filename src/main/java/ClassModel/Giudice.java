package ClassModel;

public class Giudice extends Utente {

    public Giudice() {}

    public Giudice(String NomeUtente, String PasswordUtente) {

        super(NomeUtente, PasswordUtente);

    }

    public void addEvento(Evento evento) {
        super.addEvento(evento);
        evento.addGiudice(this);
    }

    public void pubblicaProblema(String Problema) {

        getEvento().setDescrizioneProblema(Problema);
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