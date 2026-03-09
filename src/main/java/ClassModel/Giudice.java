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

}