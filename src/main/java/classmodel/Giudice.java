package classmodel;

public class Giudice extends Utente {

    public Giudice(String nomeUtente, String passwordUtente) {

        super(nomeUtente, passwordUtente);

    }

    @Override
    public void addEvento(Evento evento) {
        super.addEvento(evento);
        evento.addGiudice(this);
    }

}