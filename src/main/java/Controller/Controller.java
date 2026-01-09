package Controller;
import ClassModel.*;
import DAO.*;

import java.sql.SQLException;
import java.util.ArrayList;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

//Questo enum serve a determinare la specializzazione che un utente assume.
enum Role{
    PARTECIPANTE, ORGANIZZATORE, GIUDICE
}

public class Controller {

    private Utente UtenteCorrente = null;
    private Partecipante PartecipanteCorrente = null;
    private Organizzatore OrganizzatoreCorrente = null;
    private Giudice GiudiceCorrente = null;
    private Role RuoloCorrente = null;
    private static ImplementazioneDAO DAO = new ImplementazioneDAO();

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            success = DAO.checkLoginDB(NomeUtente, Password);
            if(success) UtenteCorrente = DAO.getUtenteDB(NomeUtente);
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String NomeUtente, String Password) {
        boolean success = false;
        try {
            if(!DAO.checkRegisteredDB(NomeUtente)) {
                UtenteCorrente = new Utente(NomeUtente, Password);
                success = true;
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return success;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi in cui l'utente ha un ruolo.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public ArrayList<String> listaEventiUtente() {
        ArrayList<String> listaEventi = null;
        try {
            if(PartecipanteCorrente == null)
                PartecipanteCorrente = DAO.getPartecipanteDB(UtenteCorrente.getNomeUtente());
            if(OrganizzatoreCorrente == null)
                OrganizzatoreCorrente = DAO.getOrganizzatoreDB(UtenteCorrente.getNomeUtente());
            if(GiudiceCorrente == null)
                GiudiceCorrente = DAO.getGiudiceDB(UtenteCorrente.getNomeUtente());
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        if(PartecipanteCorrente != null){
            listaEventi = new ArrayList<String>();
            PartecipanteCorrente.firstEvento();
            while(PartecipanteCorrente.getEvento() != null){
                String Evento = PartecipanteCorrente.getEvento().getIdEvento() + " " + PartecipanteCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                PartecipanteCorrente.nextEvento();
            }
        }
        if(OrganizzatoreCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            OrganizzatoreCorrente.firstEvento();
            while(OrganizzatoreCorrente.getEvento() != null){
                String Evento = OrganizzatoreCorrente.getEvento().getIdEvento() + " " + OrganizzatoreCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                OrganizzatoreCorrente.nextEvento();
            }
        }
        if(GiudiceCorrente != null){
            if(listaEventi == null){
                listaEventi = new ArrayList<String>();
            }
            GiudiceCorrente.firstEvento();
            while(GiudiceCorrente.getEvento() != null){
                String Evento = GiudiceCorrente.getEvento().getIdEvento() + " " + GiudiceCorrente.getEvento().getTitolo();
                listaEventi.add(Evento);
                GiudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo riceve un input l'id di un evento e determina il ruolo dell'utente.
    //Restituisce il ruolo alla funzione chiamante.

    public Role selectEvento(int idEvento) {

        if(PartecipanteCorrente != null){
            if(PartecipanteCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.PARTECIPANTE;
            }
        }
        if(OrganizzatoreCorrente != null){
            if(OrganizzatoreCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.ORGANIZZATORE;
            }
        }
        if(GiudiceCorrente != null){
            if(GiudiceCorrente.seekEvento(idEvento) != null){
                RuoloCorrente = Role.GIUDICE;
            }
        }
        return RuoloCorrente;
    }

    //Questo metodo restituisce una stringa contenente i dati personali dell'utente.
    //Nel caso in cui non sia salvato nessun dato restituisce null.
    //La stringa segue il formato: FNome MNome LNome DataNascita

    public String datiUtente(){
        if(UtenteCorrente != null){
            if(UtenteCorrente.getFNome() == null && UtenteCorrente.getMNome() == null &&
                    UtenteCorrente.getLNome() == null && UtenteCorrente.getDataNascita() == null)
                return null;
            else return UtenteCorrente.getFNome() + " " + UtenteCorrente.getMNome() + " "
                    + UtenteCorrente.getLNome() + " " + UtenteCorrente.getDataNascita();
        }
        return null;
    }

    //Questo metodo permette di aggiornare i dati personali di un utente.
    //Restituisce un boolean e accetta solo una data col formato yyyy-mm-dd, altrimenti lancia una eccezione.

    public boolean inserisciDatiUtente(String FNome, String MNome, String LNome, LocalDate DataNascita){
        try {
            if ((FNome.length() < 3) || (LNome.length() < 3)) return false;
            else {
                if(UtenteCorrente != null)
                    UtenteCorrente.setDati(FNome, MNome, LNome, DataNascita);
                if(PartecipanteCorrente != null)
                    PartecipanteCorrente.setDati(FNome, MNome, LNome, DataNascita);
                if(OrganizzatoreCorrente != null)
                    OrganizzatoreCorrente.setDati(FNome, MNome, LNome, DataNascita);
                if(GiudiceCorrente != null)
                    GiudiceCorrente.setDati(FNome, MNome, LNome, DataNascita);
            }
        } catch (DateTimeParseException e) {
            System.out.println("Errore nel tentativo di copiare la data di nascita.");
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi le cui prenotazioni sono attualmente aperte.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg DescrizioneProblema

    public ArrayList<String> listaEventiAperti(){
        ArrayList<String> listaEventi = null;
        try{
            ArrayList<Evento> eventi = DAO.getEventiApertiDB();
            if(eventi != null) {
                listaEventi = new ArrayList<String>();
                for (Evento evento : eventi) {
                    String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                            + " " + evento.getIndirizzoSede() + " " + evento.getNCivicoSede()
                            + " " + evento.getDataInizio() + " " + evento.getDataFine()
                            + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                            + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                            + " " + evento.getDescrizioneProblema();
                    listaEventi.add(datiEvento);
                }
            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return listaEventi;
    }

    //Questo metodo aggiunge un evento alla lista di eventi a cui è iscritto un partecipante.
    //Restituisce false se fallisce.

    public boolean iscriviEvento(int idEvento){
        Evento evento = null;
        try{
            evento = DAO.getEventoDB(idEvento);
            if(evento != null){
                if(PartecipanteCorrente == null)
                    PartecipanteCorrente = UtenteCorrente.becomePartecipante();
                PartecipanteCorrente.addEvento(evento);
            }
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Questo metodo restituisce una stringa con l'id e il nome del team con cui il partecipante è iscritto all'evento.
    //La stringa è fromattata come: IdTeam Nome

    public String teamPartecipante() {
        String infoTeam = null;
        Team team = PartecipanteCorrente.seekTeamEvento(PartecipanteCorrente.getEvento().getIdEvento());
        if(team != null){
            infoTeam = team.getIdTeam() + " " + team.getNome();
        }
        return infoTeam;
    }

    //Questo metodo restiuisce una lista contenente l'id e il testo di ogni progresso pubblicato da un team.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public ArrayList<String> listaProgressiTeam(){
        ArrayList<String> listaProgressi = null;
        Team team = PartecipanteCorrente.getTeam();
        Progresso progresso = team.firstProgresso();
        if(progresso != null){
            listaProgressi = new ArrayList<String>();
            while(progresso != null){
                listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                progresso = team.nextProgresso();
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette di pubblicare un progresso. Restituisce un boolean e fallisce se avviene un SQLException
    //o se qualcuno di diverso dal team leader cerca di pubblicare il progresso.

    public boolean pubblicaProgresso(String Testo){
        Progresso progresso = null;
        Team team = PartecipanteCorrente.getTeam();
        if(team.getTeamLeader().equals(PartecipanteCorrente.getNomeUtente()))
        {
            try {
                progresso = new Progresso(Testo, team.getIdTeam());
                progresso.setIdProgresso(DAO.addProgressoDB(team.getIdTeam(), Testo));
                team.addProgresso(progresso);
            }
            catch(SQLException e)
            {
                e.printStackTrace();
                return false;
            }
        }
        else return false;
        return true;
    }

    //Questo metodo permette al partecipante di creare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaTeamPartecipante(String nome){
        Team team = new Team(-1, nome, PartecipanteCorrente.getNomeUtente());
        team.setEventoIscritto(PartecipanteCorrente.getEvento());
        team.addMembroTeam(PartecipanteCorrente);
        try{
            team.setIdTeam(DAO.addTeamDB(team));
        }
        catch(SQLException e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente l'id e il nome di tutti i team iscritti all'evento.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public ArrayList<String> listaTeamEvento(){
        ArrayList<String> listaTeam = null;
        Team team = PartecipanteCorrente.getEvento().firstTeam();
        if(team != null){
            listaTeam = new ArrayList<String>();
            while(team != null){
                listaTeam.add(team.getIdTeam() + " " + team.getNome());
                team = PartecipanteCorrente.getEvento().nextTeam();
            }
        }
        return listaTeam;
    }

    //Questo metodo fa aggiungere un partecipante alla lista di attesa per un team.

    public void joinTeam(int idTeam){
        Team team = PartecipanteCorrente.getEvento().seekTeam(idTeam);
        team.addRichiesta(PartecipanteCorrente);
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti coloro che
    //hanno fatto richiesta di entrare nel team.

    public ArrayList<String> requestTeamNotifications(){
        ArrayList<String> notifications = null;
        Team team = PartecipanteCorrente.getTeam();
        if(PartecipanteCorrente != null){
            if(PartecipanteCorrente.getNomeUtente().equals(team.getTeamLeader()))
            {
                Partecipante partecipante = team.firstRichiesta();
                while(partecipante != null)
                {
                    if(notifications == null)
                        notifications = new ArrayList<String>();
                    notifications.add(partecipante.getNomeUtente());
                    partecipante = team.nextRichiesta();
                }
            }
        }
        return notifications;
    }

    //Questo metodo riceve in input gli utenti che sono stati accettati e rifiutati dal team.
    //Aggiunge ai membri team, laddove necessario, e li rimuove tutti dalla lista di attesa.

    public void teamAcceptance(ArrayList<String> accepted, ArrayList<String> refused){
        Team team = PartecipanteCorrente.getTeam();
        if(accepted != null){
            for(String s : accepted){
                Partecipante partecipante = team.seekRichiesta(s);
                if(partecipante != null) {
                    team.addMembroTeam(partecipante);
                    team.setRichiestaAnswer(true);
                }
            }
        }
        if(refused != null){
            for(String s : refused){
                if(team.seekRichiesta(s) != null) {
                    team.setRichiestaAnswer(false);
                }
            }
        }
    }

    //Questo metodo restituisce una stringa contenente tutti i dati dell'evento.
    //La stringa segue il formato: Titolo IndirizzoSede NCivicoSede DataInizio DataFine MaxIscritti MaxTeam
    // DataInizioReg DataFineReg DescrizioneProblema

    public String datiEvento(){
        Evento evento = OrganizzatoreCorrente.getEvento();
        if(evento != null){
            return evento.getTitolo() + " "
                   + evento.getIndirizzoSede() + " " + evento.getNCivicoSede()
                   + " " + evento.getDataInizio() + " " + evento.getDataFine()
                   + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                   + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                   + " " + evento.getDescrizioneProblema();
        }
        return null;
    }

    //Questo metodo permette di impostare i dati dell'evento.

    public void inserisciDatiEvento(String indirizzoSede, int nCivico, int maxIscritti, int maxTeam){
        Evento evento = OrganizzatoreCorrente.getEvento();
        evento.setIndirizzoSede(indirizzoSede);
        evento.setNCivicoSede(nCivico);
        evento.setMaxIscritti(maxIscritti);
        evento.setMaxTeam(maxTeam);
    }

    //Questo metodo permette di impostare le date dell'evento.

    public boolean setDateEvento(LocalDate dataInizio, LocalDate dataFine){
        try {
            OrganizzatoreCorrente.getEvento().setDate(dataInizio, dataFine);
        }
        catch(DateTimeParseException e)
        {
            System.out.println("Errore nel tentativo di copiare le date dell'evento.");
            return false;
        }
        return true;
    }

    //Questo metodo permette di impostare le date di registrazione dell'evento.

    public boolean setRegistrazioniEvento(LocalDate dataInizio, LocalDate dataFine){
        try {
            OrganizzatoreCorrente.getEvento().setDateReg(dataInizio, dataFine);
        }
        catch(DateTimeParseException e){
            System.out.println("Errore nel tentativo di copiare le date registrazione evento.");
            return false;
        }
        return true;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti i partecipanti all'evento.
    //Ogni elemento della lista segue il formato: NomeUtente

    public ArrayList<String> listaPartecipantiEvento(){
        ArrayList<String> listaPartecipanti = null;
        Evento evento = OrganizzatoreCorrente.getEvento();
        if(evento != null){
            evento.firstPartecipante();
            while(evento.getPartecipante() != null){
                if(listaPartecipanti == null)
                    listaPartecipanti = new ArrayList<String>();
                listaPartecipanti.add(evento.getPartecipante().getNomeUtente());
                evento.nextPartecipante();
            }
        }
        return listaPartecipanti;
    }

    //Questo metodo aggiunge un partecipante alla lista di coloro che sono stati invitati a essere giudici.

    public void invitaGiudice(String NomeUtente){
        Evento evento = OrganizzatoreCorrente.getEvento();
        if(evento != null){
            Partecipante partecipante = evento.seekPartecipante(NomeUtente);
            evento.addInvitoGiudice(partecipante);
        }
    }

}