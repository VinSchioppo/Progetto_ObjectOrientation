package ClassModel;

public class Giudice extends Utente {

    Evento evento;

    public Giudice(String NomeUtente, String PasswordUtente) {
        
        super(NomeUtente, PasswordUtente);
    
    }

    public void PubblicaProblema(String Problema) {
        
        evento.DescrizioneProblema = Problema;
    }

    private void CommentaProgresso(Progressi progresso, String commento) {
        
        Commento CommentoProgresso = new Commento(commento);
        CommentoProgresso.Giudice = this.NomeUtente;
        progresso.AddCommento(CommentoProgresso);
    
    }

    private void DaiVoto(Team team, int valore) {
        
        Voto voto = new Voto(valore);
        voto.Giudice = this.NomeUtente;
        team.AddVoto(voto);
    
    }
}
