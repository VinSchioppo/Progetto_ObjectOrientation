import java.util.Date;
import java.util.LinkedList;

public class Organizzatore extends Utente {

    Evento EventoOrganizzato;

    public Organizzatore(String NomeUtente, String PasswordUtente) {

        super(NomeUtente, PasswordUtente);
    }

    public void SelezionaEvento(Evento evento)
    {
        EventoOrganizzato = evento;
        evento.organizzatore = this;
    }

    public void setDateReg(Date Inizio, Date Fine)
    {
        EventoOrganizzato.DataInizioReg = Inizio;
        EventoOrganizzato.DataFineReg = Fine;
    }

    public void ApriPrenotazioni() {EventoOrganizzato.prenotazioni = true;}

    public void ChiudiPrenotazioni() {EventoOrganizzato.prenotazioni = false;}

    public Giudice SelezionaGiudice(Utente utente)
    {
        if(EventoOrganizzato.Giudici == null) EventoOrganizzato.Giudici = new LinkedList<Giudice>();
        Giudice giudice = new Giudice(utente.NomeUtente, utente.PasswordUtente);
        giudice = PromuoviUtente(giudice, utente);
        EventoOrganizzato.Giudici.add(giudice);
        giudice.evento = EventoOrganizzato;
        return giudice;
    }

    public Giudice PromuoviUtente(Giudice giudice, Utente utente)
    {
        giudice.FNome = utente.FNome;
        giudice.MNome = utente.MNome;
        giudice.LNome = utente.LNome;
        giudice.DataNascita = utente.DataNascita;
        return giudice;
    }
}