package Controller;
import ClassModel.*;
import DAO.*;

import java.sql.SQLException;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

//Questo enum serve a determinare la specializzazione che un utente assume.

public class Controller {

    private Utente utenteCorrente = null;
    private Partecipante partecipanteCorrente = null;
    private Organizzatore organizzatoreCorrente = null;
    private Giudice giudiceCorrente = null;
    private static final ImplementazioneDAO DAO = new ImplementazioneDAO();

    private boolean addOrganizzatore = false;
    private ArrayList<Evento> updateInvitiGiudice = null;
    private ArrayList<Evento> updateOrganizzatoreEvento = null;
    private ArrayList<Evento> updatePartecipanteEvento = null;
    private ArrayList<Team> updateRichiesteTeam = null;
    private ArrayList<Evento> pubblicaProblema = null;
    private ArrayList<Voto> addVoti = null;
    private ArrayList<Commento> addCommenti = null;

    private static final Logger logger = Logger.getLogger(Controller.class.getName());

    //Questo metodo verifica i dati di login inseriti e restituisce true se va a buon fine, altrimenti false.

    public boolean logInUtente(String nomeUtente, String password) {
        try {
            boolean success = DAO.checkLoginDB(nomeUtente, password);
            if (success) {
                utenteCorrente = DAO.getUtenteDB(nomeUtente);

                // ===== RICOSTRUZIONE RUOLI =====
                partecipanteCorrente = DAO.getPartecipanteDB(nomeUtente);
                organizzatoreCorrente = DAO.getOrganizzatoreDB(nomeUtente);
                giudiceCorrente = DAO.getGiudiceDB(nomeUtente);

                return true;
            }

        } catch (SQLException e) {
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }
    
    //Questo metodo verifica che il nome utente scelto durante la registrazione non sia già presente nel Database.
    //Restituisce true se la registrazione viene completata con successo, altrimenti restituisce false.

    public boolean registerUtente(String nomeUtente, String password) {
        try {
            DAO.addUtenteDB(nomeUtente, password);
            utenteCorrente = new Utente(nomeUtente, password);
        }
        catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    //Metodi di verifica del ruolo

    public boolean isOrganizzatore() {
        return organizzatoreCorrente != null;
    }
    public boolean isGiudice() {
        return giudiceCorrente != null;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi a cui l'utente partecipa.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiPartecipante() {
        List<String> listaEventi = null;
        if (partecipanteCorrente != null) {
            listaEventi = new ArrayList<>();
            partecipanteCorrente.firstEvento();
            while(partecipanteCorrente.getEvento() != null) {
                String evento = partecipanteCorrente.getEvento().getIdEvento() + " " + partecipanteCorrente.getEvento().getTitolo();
                listaEventi.add(evento);
                partecipanteCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi che l'utente organizza.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiOrganizzatore() {
        List<String> listaEventi = null;
        if (organizzatoreCorrente != null) {
            listaEventi = new ArrayList<>();
            organizzatoreCorrente.firstEvento();
            while(organizzatoreCorrente.getEvento() != null) {
                String evento = organizzatoreCorrente.getEvento().getIdEvento() + " " + organizzatoreCorrente.getEvento().getTitolo();                listaEventi.add(evento);
                listaEventi.add(evento);
                organizzatoreCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo restiuisce una lista contenente l'id e il titolo di tutti gli eventi che l'utente giudica.
    //Ogni elemento della lista segue il formato: IdEvento Titolo

    public List<String> listaEventiGiudice() {
        List<String> listaEventi = null;
        if(giudiceCorrente != null){
            listaEventi = new ArrayList<>();
            giudiceCorrente.firstEvento();
            while(giudiceCorrente.getEvento() != null){
                String evento = giudiceCorrente.getEvento().getIdEvento() + " " + giudiceCorrente.getEvento().getTitolo();
                listaEventi.add(evento);
                giudiceCorrente.nextEvento();
            }
        }
        return listaEventi;
    }

    //Questo metodo riceve un input l'id di un evento e il ruolo del utente in quell'evento e selezione l'evento in memoria.
    //Restituisce true se l'operazione viene completata con successo, altrimenti restituisce false.

    public boolean selectEvento(int idEvento, Role ruolo) {

        if(partecipanteCorrente != null && ruolo == Role.PARTECIPANTE && partecipanteCorrente.seekEvento(idEvento) != null)
                return true;

        if(organizzatoreCorrente != null && ruolo == Role.ORGANIZZATORE && organizzatoreCorrente.seekEvento(idEvento) != null)
                return true;

        return giudiceCorrente != null && ruolo == Role.GIUDICE && giudiceCorrente.seekEvento(idEvento) != null;
    }

    //Questo metodo restituisce una stringa contenente i dati personali dell'utente.
    //Nel caso in cui non sia salvato nessun dato restituisce null.
    //La stringa segue il formato: FNome MNome LNome DataNascita

    public String datiUtente(){
        if(utenteCorrente != null){
            if(utenteCorrente.getFNome() == null && utenteCorrente.getMNome() == null &&
                    utenteCorrente.getLNome() == null && utenteCorrente.getDataNascita() == null)
                return null;
            else
                return utenteCorrente.getFNome() + " " + utenteCorrente.getMNome() + " "
                    + utenteCorrente.getLNome() + " " + utenteCorrente.getDataNascita();
        }
        return null;
    }

    //Questo metodo permette di aggiornare i dati personali di un utente.
    //Restituisce un boolean e accetta solo una data col formato yyyy-mm-dd, altrimenti lancia una eccezione.

    public boolean inserisciDatiUtente(String fNome, String mNome, String lNome, LocalDate dataNascita){
        try{
                if(utenteCorrente != null) {
                    utenteCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    DAO.updatePartecipanteDB(utenteCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
                if(partecipanteCorrente != null)
                    partecipanteCorrente.setDati(fNome, mNome, lNome, dataNascita);
                if(organizzatoreCorrente != null) {
                    organizzatoreCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    DAO.updateOrganizzatoreDB(organizzatoreCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
                if(giudiceCorrente != null) {
                    giudiceCorrente.setDati(fNome, mNome, lNome, dataNascita);
                    DAO.updateGiudiceDB(giudiceCorrente.getNomeUtente(), fNome, mNome, lNome, dataNascita);
                }
        }
        catch (DateTimeParseException _) {
            logger.log(Level.SEVERE, "Errore nel tentativo di copiare la data di nascita.");
            return false;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            return false;
        }
        return true;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi le cui prenotazioni sono attualmente aperte.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg DescrizioneProblema

    public List<String> listaEventiAperti(){
        List<String> listaEventi = null;
        if(utenteCorrente != null) {
            try {
                ArrayList<Evento> eventi = DAO.getEventiApertiDB(utenteCorrente.getNomeUtente());
                if (eventi != null) {
                    listaEventi = new ArrayList<>();
                    for (Evento evento : eventi) {
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                                + " " + evento.getDescrizioneProblema();
                        listaEventi.add(datiEvento);
                    }
                }
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaEventi;
    }

    //Questo metodo aggiunge un evento alla lista di eventi a cui è iscritto un partecipante.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean iscriviEvento(int idEvento){
        Evento evento = null;
        try{
            evento = DAO.getEventoDB(idEvento);
            if(evento != null && evento.sizePartecipanti() > evento.getMaxIscritti()) {
                if (partecipanteCorrente == null)
                    partecipanteCorrente = utenteCorrente.becomePartecipante();
                partecipanteCorrente.addEvento(evento);
                if(updatePartecipanteEvento == null)
                    updatePartecipanteEvento = new ArrayList<>();
                updatePartecipanteEvento.add(evento);
                return true;
            }
        }
        catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    //Questo metodo permette ad un utente di creare un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaEvento(String titolo, String indirizzo, int nCivico){
        if(utenteCorrente != null) {
            Evento evento = new Evento(titolo, indirizzo, nCivico);
            Organizzatore organizzatore = null;
            if(organizzatoreCorrente == null) {
                organizzatore = utenteCorrente.becomeOrganizzatore();
                addOrganizzatore = true;
            }
            else
                organizzatore = organizzatoreCorrente;
            evento.setOrganizzatore(organizzatore);
            try {
                DAO.addEventoDB(evento);
                organizzatore.addEvento(evento);
                organizzatoreCorrente = organizzatore;
                if(updateOrganizzatoreEvento == null)
                    updateOrganizzatoreEvento = new ArrayList<>();
                updateOrganizzatoreEvento.add(evento);
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente i dati di tutti gli eventi
    //per cui un utente è stato invitato a diventare un giudice.
    //Ogni elemento della lista segue il formato: IdEvento Titolo IndirizzoSede NCivicoSede DataInizio DataFine
    // MaxIscritti MaxTeam DataInzioReg DataFineReg

    public List<String> listaInvitiGiudice() {
        ArrayList<String> invitiGiudice = null;
        if(utenteCorrente != null && partecipanteCorrente != null) {
                try {
                    DAO.getAllInvitiGiudiceDB(utenteCorrente, partecipanteCorrente);
                    Evento evento = utenteCorrente.firstInvitoGiudiceEvento();
                    while (evento != null) {
                        if (invitiGiudice == null)
                            invitiGiudice = new ArrayList<>();
                        String datiEvento = evento.getIdEvento() + " " + evento.getTitolo()
                                + " " + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                                + " " + evento.getDataInizio() + " " + evento.getDataFine()
                                + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                                + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg();
                        invitiGiudice.add(datiEvento);
                        evento = utenteCorrente.nextInvitoGiudiceEvento();
                    }
                }
                catch(SQLException e){
                    logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                }
            }

        return invitiGiudice;
    }

    //Questo metodo permette ad un utente di accettare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean acceptInvitoGiudiceEvento(int idEvento){
        if(utenteCorrente != null) {
            Evento evento = utenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null && utenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    utenteCorrente.setInvitoGiudiceEventoAnswer(true);
                    evento.seekInvitoGiudice(utenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(true);
                    if(giudiceCorrente == null)
                        giudiceCorrente = utenteCorrente.becomeGiudice();
                    evento.addGiudice(giudiceCorrente);
                    giudiceCorrente.addEvento(evento);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<>();
                    updateInvitiGiudice.add(evento);
                    return true;
                }

        }
        return false;
    }

    //Questo metodo permette ad un utente di rifiutare un invito ad essere giudice di un evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean refuseInvitoGiudiceEvento(int idEvento){
        if(utenteCorrente != null) {
            Evento evento = utenteCorrente.seekInvitoGiudiceEvento(idEvento);
            if (evento != null && utenteCorrente.getInvitoGiudiceEventoAnswer() == null) {
                    utenteCorrente.setInvitoGiudiceEventoAnswer(false);
                    evento.seekInvitoGiudice(utenteCorrente.getNomeUtente());
                    evento.setInvitoGiudiceAnswer(false);
                    if(updateInvitiGiudice == null)
                        updateInvitiGiudice = new ArrayList<>();
                    updateInvitiGiudice.add(evento);
                    return true;
                }

        }
        return false;
    }

    /*******************************************************************************************************/

    //Operazioni Partecipante

    //Questo metodo restituisce una stringa con l'id e il nome del team con cui il partecipante è iscritto all'evento.
    //La stringa è fromattata come: IdTeam Nome TeamLeader

    public String teamPartecipante() {
        String infoTeam = null;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.seekTeamEvento(partecipanteCorrente.getEvento().getIdEvento());
            if (team != null) {
                infoTeam = team.getIdTeam() + " " + team.getNome() + " " + team.getTeamLeader();
            }
        }
        return infoTeam;
    }

    //Questo metodo permette al partecipante di creare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean creaTeamPartecipante(String nome){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento().getDataFineReg() != null && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())){
            Team team = new Team(-1, nome, partecipanteCorrente.getNomeUtente());
            team.setEventoIscritto(partecipanteCorrente.getEvento());
            team.addMembroTeam(partecipanteCorrente);
            try {
                team.setIdTeam(DAO.addTeamDB(team));
                return true;
            } catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }

        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il nome di tutti i team iscritti all'evento.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public List<String> listaTeamEvento(){
        List<String> listaTeam = null;
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento() != null) {
            Team team = partecipanteCorrente.getEvento().firstTeam();
            if (team != null) {
                listaTeam = new ArrayList<>();
                while (team != null) {
                    listaTeam.add(team.getIdTeam() + " " + team.getNome());
                    team = partecipanteCorrente.getEvento().nextTeam();
                }
            }
        }
        return listaTeam;
    }

    //Questo metodo fa aggiungere un partecipante alla lista di attesa per un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean joinTeam(int idTeam){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento().getDataFineReg() != null && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())) {
                Team team = partecipanteCorrente.getEvento().seekTeam(idTeam);
                if (team != null) {
                    team.addRichiesta(partecipanteCorrente);
                    if (updateRichiesteTeam == null)
                        updateRichiesteTeam = new ArrayList<>();
                    updateRichiesteTeam.add(team);
                    return true;
                }
            }

        return false;
    }

    //Questo metodo restiuisce una lista contenente tutti i membri di un team.
    //Ogni elemento della lista segue il formato: NomeUtente FNome MNome LNome

    public List<String> listaMembriTeam(){
        List<String> listaMembri = null;
        if(partecipanteCorrente != null && partecipanteCorrente.getTeam() != null) {
            Partecipante membro = partecipanteCorrente.getTeam().firstMembroTeam();
            if (membro != null) {
                listaMembri = new ArrayList<>();
                while (membro != null) {
                    listaMembri.add(membro.getNomeUtente() + " " + membro.getFNome() + " " + membro.getMNome() + " " + membro.getLNome());
                    membro = partecipanteCorrente.getTeam().nextMembroTeam();
                }
            }
        }
        return listaMembri;
    }

    //Questo metodo permette a un partecipante di lasciare un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean leaveTeam(int idTeam){
        if(partecipanteCorrente != null && partecipanteCorrente.getEvento().getDataFineReg() != null && partecipanteCorrente.getEvento().getDataFineReg().isAfter(LocalDate.now())) {
            Team team = partecipanteCorrente.seekTeam(idTeam);
            if (team != null) {
                try {
                    DAO.leaveTeamDB(partecipanteCorrente.getNomeUtente(), idTeam);
                    partecipanteCorrente.removeTeam(idTeam);
                    return true;
                }
                catch(SQLException e)
                {
                    logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti coloro che
    //hanno fatto richiesta di entrare nel team.

    public List<String> requestTeamNotifications(){
        List<String> notifications = null;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null && partecipanteCorrente.getNomeUtente().equals(team.getTeamLeader())) {
                    Partecipante partecipante = team.firstRichiesta();
                    while (partecipante != null) {
                        if (notifications == null)
                            notifications = new ArrayList<>();
                        notifications.add(partecipante.getNomeUtente());
                        partecipante = team.nextRichiesta();
                    }
                }

        }
        return notifications;
    }

    //Questo metodo riceve in input un utente che è stato accettato nel team e lo aggiunge.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean acceptRichiestaTeam(String nomeUtente){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if(team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                Partecipante partecipante = team.seekRichiesta(nomeUtente);
                if (partecipante != null) {
                    team.addMembroTeam(partecipante);
                    team.setRichiestaAnswer(true);
                }
                if(updateRichiesteTeam == null)
                    updateRichiesteTeam = new ArrayList<>();
                updateRichiesteTeam.add(team);
                return true;
            }
        }
        return false;
    }

    //Questo metodo riceve in input un utente che è stato rifiutato dal team e e imposta la sua richiesta a falso.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean refuseRichiestaTeam(String nomeUtente){
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if(team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                Partecipante partecipante = team.seekRichiesta(nomeUtente);
                if (partecipante != null) {
                    team.setRichiestaAnswer(false);
                }
                if(updateRichiesteTeam == null)
                    updateRichiesteTeam = new ArrayList<>();
                updateRichiesteTeam.add(team);
                return true;
            }
        }
        return false;
    }

    //Questo metodo restiuisce una lista contenente l'id e il testo di ogni progresso pubblicato da un team.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public List<String> listaProgressiTeam(){
        ArrayList<String> listaProgressi = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.firstProgresso();
                if (progresso != null) {
                    listaProgressi = new ArrayList<>();
                    while (progresso != null) {
                        listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                        progresso = team.nextProgresso();
                    }
                }
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette di pubblicare un progresso.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProgresso(String testo){
        Progresso progresso = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null && team.getTeamLeader().equals(partecipanteCorrente.getNomeUtente())) {
                    try {
                        progresso = new Progresso(testo, team.getIdTeam());
                        DAO.addProgressoDB(progresso);
                        team.addProgresso(progresso);
                        return true;
                    } catch (SQLException e) {
                        logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
                    }
                }

        }
        return false;
    }

    //Questo metodo restutuisce una lista contenente tutti i commenti ricevuti da un progresso.
    //Ogni elemento della lista segue il formato: Giudice Testo

    public List<String> listaCommentiProgresso(int idProgresso)
    {
        ArrayList<String> listaCommenti = null;
        if (partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Progresso progresso = team.seekProgresso(idProgresso);
                if (progresso != null) {
                    Commento commento = progresso.firstCommento();
                    while (commento != null) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<>();
                        listaCommenti.add(commento.getGiudice() + " " + commento.getTesto());
                        commento = progresso.nextCommento();
                    }
                }
            }
        }
        return listaCommenti;
    }


    //Questo metodo restutuisce una lista contenente tutti i voti ricevuti da un team.
    //Ogni elemento della lista segue il formato: Giudice Valore

    public List<String> listaVotiTeam()
    {
        ArrayList<String> listaVoti = null;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                Voto voto = team.firstVoto();
                while (voto != null) {
                    if(listaVoti == null)
                        listaVoti = new ArrayList<>();
                    listaVoti.add(voto.getGiudice() + " " + voto.getValore());
                    voto = team.nextVoto();
                }
            }
        }
        return listaVoti;
    }

    //Questo metodo restituisce la media di tutti i voti ricevuti da un team.

    public int mediaVotiTeam(){
        int mediaVoti = 0;
        if(partecipanteCorrente != null) {
            Team team = partecipanteCorrente.getTeam();
            if (team != null) {
                mediaVoti = team.mediaVoti();
            }
        }
        return mediaVoti;
    }

    /*******************************************************************************************************/

    //Operazioni Organizzatore

    //Questo metodo restituisce una stringa contenente tutti i dati dell'evento.
    //La stringa segue il formato: Titolo IndirizzoSede NCivicoSede DataInizio DataFine MaxIscritti MaxTeam
    // DataInizioReg DataFineReg DescrizioneProblema

    public String datiEvento(){
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                return evento.getTitolo() + " "
                        + evento.getIndirizzoSede() + " " + evento.getnCivicoSede()
                        + " " + evento.getDataInizio() + " " + evento.getDataFine()
                        + " " + evento.getMaxIscritti() + " " + evento.getMaxTeam()
                        + " " + evento.getDataInizioReg() + " " + evento.getDataFineReg()
                        + " " + evento.getDescrizioneProblema();
            }
        }
        return null;
    }

    //Questo metodo permette di impostare i dati dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean inserisciDatiEvento(String indirizzoSede, int nCivico, int maxIscritti, int maxTeam){
        if(organizzatoreCorrente != null) {
            try {
                DAO.updateEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), indirizzoSede, nCivico, maxIscritti, maxTeam);
                Evento evento = organizzatoreCorrente.getEvento();
                evento.setIndirizzoSede(indirizzoSede);
                evento.setnCivicoSede(nCivico);
                evento.setMaxIscritti(maxIscritti);
                evento.setMaxTeam(maxTeam);
                return true;
            }
            catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setDateEvento(LocalDate dataInizio, LocalDate dataFine){
        if(organizzatoreCorrente != null) {
            try {
                DAO.updateDateEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                organizzatoreCorrente.getEvento().setDate(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            } catch (NullPointerException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di copiare le date dell'evento.");
            }
        }
        return false;
    }

    //Questo metodo permette di impostare le date di registrazione dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean setRegistrazioniEvento(LocalDate dataInizio, LocalDate dataFine){
        if(organizzatoreCorrente != null) {
            try {
                DAO.updateDateRegEventoDB(organizzatoreCorrente.getEvento().getIdEvento(), dataInizio, dataFine);
                organizzatoreCorrente.getEvento().setDateReg(dataInizio, dataFine);
                return true;
            }catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            } catch (NullPointerException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di accedere ai dati dell'evento.");
            } catch (DateTimeParseException _) {
                logger.log(Level.SEVERE, "Errore nel tentativo di copiare le date registrazione evento.");
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente il nome utente di tutti i partecipanti all'evento.
    //Ogni elemento della lista segue il formato: NomeUtente

    public List<String> listaPartecipantiEvento(){
        List<String> listaPartecipanti = null;
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                evento.firstPartecipante();
                while (evento.getPartecipante() != null) {
                    if (listaPartecipanti == null)
                        listaPartecipanti = new ArrayList<>();
                    listaPartecipanti.add(evento.getPartecipante().getNomeUtente());
                    evento.nextPartecipante();
                }
            }
        }
        return listaPartecipanti;
    }

    //Questo metodo aggiunge un partecipante alla lista di coloro che sono stati invitati a essere giudici.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean invitaGiudice(String nomeUtente){
        if(organizzatoreCorrente != null){
            Evento evento = organizzatoreCorrente.getEvento();
            if(evento != null){
                Partecipante partecipante = evento.seekPartecipante(nomeUtente);
                evento.addInvitoGiudice(partecipante);
                if(updateInvitiGiudice == null)
                    updateInvitiGiudice = new ArrayList<>();
                updateInvitiGiudice.add(evento);
                return true;
            }
        }
        return false;
    }

    public List<String> listaGiudiciEvento(){
        List<String> listaGiudici = null;
        if(organizzatoreCorrente != null) {
            Evento evento = organizzatoreCorrente.getEvento();
            if (evento != null) {
                evento.firstGiudice();
                while (evento.getGiudice() != null) {
                    if (listaGiudici == null)
                        listaGiudici = new ArrayList<>();
                    listaGiudici.add(evento.getGiudice().getNomeUtente());
                    evento.nextGiudice();
                }
            }
        }
        return listaGiudici;
    }

    /*******************************************************************************************************/

    //Operazioni Giudice

    //Questo metodo permette a un giudice di pubblicare la descrizione del problema dell'evento.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean pubblicaProblema(String problema){
        if(giudiceCorrente != null){
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                evento.setDescrizioneProblema(problema);
                if(pubblicaProblema == null)
                    pubblicaProblema = new ArrayList<>();
                pubblicaProblema.add(evento);
                return true;
            }
        }
        return false;
    }

    //Questo metodo restituisce un lista con tutti i team iscritti ad un evento giudicato dall'utente.
    //Ogni elemento della lista segue il formato: IdTeam Nome

    public List<String> listaTeamGiudicati(){
        ArrayList<String> listaTeam = null;
        if(giudiceCorrente != null){
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.firstTeam();
                while(team != null){
                    if(listaTeam == null)
                        listaTeam = new ArrayList<>();
                    listaTeam.add(team.getIdTeam() + " " + team.getNome());
                    team = evento.nextTeam();
                }
            }
        }
        return listaTeam;
    }

    //Questo metodo permette a un giudice di attribuire un voto ad un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean giveVotoTeam(int idTeam, int valore){
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if (evento != null) {
                Team team = evento.seekTeam(idTeam);
                if (team != null) {
                    Voto voto = new Voto(team.getIdTeam(), valore, giudiceCorrente.getNomeUtente());
                    team.addVoto(voto);
                    if(addVoti == null)
                        addVoti = new ArrayList<>();
                    addVoti.add(voto);
                    return true;
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce una lista contenente tutti i voti attribuire dall'utente in qualità di giudice.
    //Ogni elemento della lista segue il formato: IdTeam Valore

    public List<String> listaVotiTeamGiudicati(){
        ArrayList<String> listaVoti = null;
        if(giudiceCorrente != null) {
            try {
                ArrayList<Voto> voti = DAO.getAllVotiDB(giudiceCorrente.getNomeUtente());
                if (voti != null) {
                    for (Voto voto : voti) {
                        if (listaVoti == null)
                            listaVoti = new ArrayList<>();
                        listaVoti.add(voto.getIdTeam() + " " + voto.getValore());
                    }
                }
            }
            catch (SQLException e) {
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaVoti;
    }

    //Questo metodo restituisce un lista con tutti i progressi di un team giudicato dell'utente.
    //Ogni elemento della lista segue il formato: IdProgresso TestoDocumento

    public List<String> listaProgressiTeamGiudicato(int idTeam){
        ArrayList<String> listaProgressi = null;
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.seekTeam(idTeam);
                if(team != null){
                    Progresso progresso = team.firstProgresso();
                    while(progresso != null) {
                        if (listaProgressi == null)
                            listaProgressi = new ArrayList<>();
                        listaProgressi.add(progresso.getIdProgresso() + " " + progresso.getTestoDocumeto());
                        progresso = team.nextProgresso();
                    }
                }
            }
        }
        return listaProgressi;
    }

    //Questo metodo permette a un giudice di commentare il progresso di un team.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean commentaProgresso(int idProgresso, String testoCommento){
        if(giudiceCorrente != null) {
            Evento evento = giudiceCorrente.getEvento();
            if(evento != null){
                Team team = evento.getTeam();
                if(team != null){
                    Progresso progresso = team.seekProgresso(idProgresso);
                    if(progresso != null) {
                        Commento commento = new Commento(progresso.getIdProgresso(), testoCommento, giudiceCorrente.getNomeUtente());
                        progresso.addCommento(commento);
                        if(addCommenti == null)
                            addCommenti = new ArrayList<>();
                        addCommenti.add(commento);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    //Questo metodo restituisce un lista con tutti i commenti pubblicati da un giudice.
    //Ogni elemento della lista segue il formato: IdProgresso Testo

    public List<String> listaCommenti(){
        ArrayList<String> listaCommenti = null;
        if(giudiceCorrente != null) {
            try{
                ArrayList<Commento> commenti = DAO.getAllCommentiDB(giudiceCorrente.getNomeUtente());
                if (commenti != null) {
                    for (Commento commento : commenti) {
                        if (listaCommenti == null)
                            listaCommenti = new ArrayList<>();
                        listaCommenti.add(commento.getIdProgresso() + " " + commento.getTesto());
                    }
                }
            }
            catch(SQLException e){
                logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
            }
        }
        return listaCommenti;
    }

    //Questo metodo serve a riportare tutti i cambiamenti avvenuti sul database all'uscita dall'applicazione.
    //Restituisce true se l'operazione va a buon fine, altrimenti false.

    public boolean exitApplication(){
        try {
            updateInviti();
            updatePartecipante();
            updateOrganizzatore();
            updateGiudice();
            DAO.disconnect();
            return true;
        }catch(SQLException e){
            logger.log(Level.SEVERE, Arrays.toString(e.getStackTrace()));
        }
        return false;
    }

    private void updateInviti() throws SQLException{
        if(utenteCorrente != null && updateInvitiGiudice != null)
            DAO.addInvitiGiudiceDB(updateInvitiGiudice);
    }

    private void updatePartecipante() throws SQLException{
        if(partecipanteCorrente != null) {
            if(updatePartecipanteEvento != null)
                DAO.addPartecipanteEventoDB(partecipanteCorrente.getNomeUtente(), updatePartecipanteEvento);
            if(updateRichiesteTeam != null)
                DAO.addRichiesteTeamDB(updateRichiesteTeam);
        }
    }

    private void updateOrganizzatore() throws SQLException{
        if(organizzatoreCorrente != null) {
            if (addOrganizzatore)
                DAO.addOrganizzatoreDB(organizzatoreCorrente);
            if(updateOrganizzatoreEvento != null)
                DAO.addOrganizzatoreEventoDB(organizzatoreCorrente.getNomeUtente(), updateOrganizzatoreEvento);
        }
    }

    private void updateGiudice() throws  SQLException{
        if(giudiceCorrente != null) {
            if(pubblicaProblema != null)
                DAO.updateProblemaDB(pubblicaProblema);
            if(addVoti != null)
                DAO.addAllVotiDB(addVoti);
            if(addCommenti != null)
                DAO.addAllCommentiDB(addCommenti);
        }
    }
}