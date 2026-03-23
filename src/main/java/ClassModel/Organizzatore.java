package ClassModel;

public class Organizzatore extends Utente {

    public Organizzatore(String nomeUtente, String passwordUtente) {

        super(nomeUtente, passwordUtente);

    }

    @Override
    public void addEvento(Evento evento) {

        super.addEvento(evento);
        evento.setOrganizzatore(this);
    }

}