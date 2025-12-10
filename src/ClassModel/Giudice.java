package ClassModel;

public class Giudice extends Utente {

    Evento evento;

    public Giudice(String NomeUtente, String PasswordUtente) {
        
        super(NomeUtente, PasswordUtente);
    
    }


    public void pubblicaProblema(String Problema) {
        
        evento.setDescrizioneProblema(Problema);
    }

    private void commentaProgresso(Progressi progresso, String commento) {
        
        Commento CommentoProgresso = new Commento(commento);
        CommentoProgresso.Giudice = this.NomeUtente;
        progresso.addCommento(CommentoProgresso);
    
    }

    private void daiVoto(Team team, int valore) {
        
        Voto voto = new Voto(valore);
        voto.setGiudice(this.NomeUtente);
        team.addVoto(voto);
    
    }
}
