package DAO;

import ClassModel.*;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class ImplementazioneDAO implements InterfacciaDAO {
    private Connection connection = null;

    private static final String IDEVENTO = "idevento";
    private static final String TITOLO = "titolo";
    private static final String INDIRIZZO = "indirizzosede";
    private static final String NCIVICO = "ncivicosede";
    private static final String DATAINIZIO = "datainizio";
    private static final String DATAFINE = "datafine";
    private static final String MAXISCRITTI = "maxiscritti";
    private static final String MAXTEAM = "maxteam";
    private static final String DATAINIZIOREG = "datainizioreg";
    private static final String DATAFINEREG = "datafinereg";
    private static final String DESCRIZIONEPROBLEMA = "descrizioneprob";

    private static final String NOMEUTENTE = "nomeutente";
    private static final String PASSWORD = "password";
    private static final String FNOME = "fnome";
    private static final String MNOME = "mnome";
    private static final String LNOME = "lnome";
    private static final String DATANASCITA = "datanascita";

    private static final String NOMEPARTECIPANTE = "nomepartecipante";
    private static final String NOMEGIUDICE = "nomegiudice";

    private static final String IDTEAM = "idteam";
    private static final String NOMETEAM = "nome";
    private static final String TEAMLEADER = "teamleader";

    private static final String IDPROGRESSO = "idprogresso";
    private static final String DATAPUBBLICAZIONE = "datapubblicazione";
    private static final String TESTOPROGRESSO = "testoprogresso";

    private static final String TESTOCOMMENTO = "testocommento";

    private static final String VALORE = "valore";

    private static final String WHERE_UTENTE = " WHERE NomeUtente = ";
    private static final String ORDER = " ORDER BY t.idTeam;";

    public ImplementazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().Connection;
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean checkLoginDB(String nomeUtente, String password) throws SQLException{
        boolean success = true;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT NomeUtente, Password FROM Partecipante" +
                " WHERE NomeUtente = '" + nomeUtente + "' AND Password = '" + password + "';")) {
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                success = false;
            }
            rs.close();
        }
        return success;
    }

    public int getIdEventoDB() throws SQLException{
        int result;
        try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(IdEvento) FROM Evento;")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            result = rs.getInt(1);
            rs.close();
        }
        return result;
    }

    public Evento getEventoDB(int idEvento) throws SQLException{
        Evento evento = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Evento WHERE IdEvento =" + idEvento + ";")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                evento = new Evento(idEvento);
                rs.next();
                evento.setTitolo(rs.getString(TITOLO));
                evento.setIndirizzoSede(rs.getString(INDIRIZZO));
                evento.setnCivicoSede(rs.getInt(NCIVICO));
                evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                evento.setMaxTeam(rs.getInt(MAXTEAM));
                evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                rs.close();
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
            }
        }
        return evento;
    }

    public ArrayList<Evento> getEventiApertiDB(String nomeUtente) throws SQLException{
        ArrayList<Evento> eventi = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Evento " +
                "WHERE DataInizioReg <= NOW() AND DataFineReg >= NOW()\n" +
                "AND IdEvento NOT IN( " +
                "SELECT pe.idEvento FROM PartecipanteEvento AS pe " +
                "JOIN OrganizzatoreEvento AS oe ON pe.idEvento = oe.idEvento " +
                "JOIN GiudiceEvento AS ge ON pe.idEvento = ge.idEvento " +
                "WHERE pe.NomePartecipante = '" + nomeUtente + "');")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                eventi = new ArrayList<>();
                while (rs.next()) {
                    Evento evento = new Evento(rs.getInt(IDEVENTO));
                    evento.setTitolo(rs.getString(TITOLO));
                    evento.setIndirizzoSede(rs.getString(INDIRIZZO));
                    evento.setnCivicoSede(rs.getInt(NCIVICO));
                    evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                    evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                    evento.setMaxTeam(rs.getInt(MAXTEAM));
                    evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                    eventi.add(evento);
                }
                rs.close();
            }
        }
        return eventi;
    }

    public void addEventoDB(Evento evento) throws SQLException {
            evento.setIdEvento(addEventoDB(evento.getTitolo(), evento.getIndirizzoSede(), evento.getnCivicoSede(), evento.getDataInizio(), evento.getDataFine(), evento.getMaxIscritti(), evento.getMaxTeam(), evento.getDataInizioReg(), evento.getDataFineReg(), evento.getDescrizioneProblema()));
    }

    public int addEventoDB(String titolo, String indirizzo, int nCivico, LocalDate dataInizio, LocalDate dataFine, int maxIscritti, int maxTeam, LocalDate dataInizioReg, LocalDate dataFineReg, String descrizioneProb) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede, DataInizio, DataFine," +
                " MaxIscritti, MaxTeam, DataInizioReg, DataFineReg, DescrizioneProb)" +
                " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);")) {
            ps.setString(1, titolo);
            ps.setString(2, indirizzo);
            if (nCivico == -1)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, nCivico);
            ps.setObject(4, dataInizio);
            ps.setObject(5, dataFine);
            if (maxIscritti == -1)
                ps.setNull(6, Types.INTEGER);
            else
                ps.setInt(6, maxIscritti);
            if (maxTeam == -1)
                ps.setNull(7, Types.INTEGER);
            else
                ps.setInt(7, maxTeam);
            ps.setObject(8, dataInizioReg);
            ps.setObject(9, dataFineReg);
            ps.setString(10, descrizioneProb);
            ps.executeUpdate();
        }
        return getIdEventoDB();
    }

    public void updateEventoDB(int idEvento, String indirizzo, int nCivico, int maxIscritti, int maxTeam) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE Evento SET Titolo = ?, IndirizzoSede = ?, NCivicoSede = ?, " +
                "MaxIscritti = ?, MaxTeam = ? WHERE IdEvento = ?;")) {
            ps.setString(1, indirizzo);
            if (nCivico == -1)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, nCivico);
            if (maxIscritti == -1)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, maxIscritti);
            if (maxTeam == -1)
                ps.setNull(4, Types.INTEGER);
            else
                ps.setInt(4, maxTeam);
            ps.setInt(5, idEvento);
            ps.executeUpdate();
        }
    }

    public void updateDateEventoDB(int idEvento, LocalDate dataInizio, LocalDate dataFine) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Evento SET DataInizio = ?, DataFine = ? WHERE IdEvento = ? ;")) {
            ps.setObject(1, dataInizio);
            ps.setObject(2, dataFine);
            ps.setInt(3, idEvento);
            ps.executeUpdate();
        }
    }

    public void updateDateRegEventoDB(int idEvento, LocalDate dataInizioReg, LocalDate dataFineReg) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement("UPDATE Evento SET DataInizioReg = ?, DataFineReg = ? WHERE IdEvento = ? ;")) {
            ps.setObject(1, dataInizioReg);
            ps.setObject(2, dataFineReg);
            ps.setInt(3, idEvento);
            ps.executeUpdate();
        }
    }

    public void updateProblemaDB(ArrayList<Evento> eventiProblema) throws SQLException{
        if(eventiProblema != null){
            HashSet<Evento> eventi = new HashSet<>(eventiProblema);
            StringBuilder codiceSQL = new StringBuilder();
            for (Evento evento : eventi) {
                codiceSQL.append("UPDATE Evento SET DescrizioneProb = '").append(evento.getDescrizioneProblema()).append("' WHERE IdEvento = ").append(evento.getIdEvento()).append(";");
            }
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public Utente getUtenteDB(String nomeUtente) throws SQLException{
        Utente utente = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Partecipante WHERE NomeUtente = '" + nomeUtente + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                utente = new Utente(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                utente.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                rs.close();
            }

        }
        return utente;
    }

    public void addUtenteDB(String nomeUtente, String password) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                + nomeUtente + "','" + password + "');")) {
            ps.executeUpdate();
        }
    }

    public void getAllInvitiGiudiceDB(Evento evento) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM InvitoGiudice WHERE Risposta IS NULL AND idEvento = " + evento.getIdEvento() + ";")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Partecipante partecipante = evento.seekPartecipante(rs.getString(NOMEPARTECIPANTE));
                    if (partecipante != null)
                        evento.addInvitoGiudice(partecipante);
                }
                rs.close();
            }

        }
    }

    public void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM InvitoGiudice WHERE Risposta IS NULL AND NomePartecipante = '" + utente.getNomeUtente() + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Evento evento = partecipante.seekEvento(rs.getInt(IDEVENTO));
                    if (evento != null)
                        utente.addInvitoGiudiceEvento(evento);
                }
                rs.close();
            }

        }
    }

    public void addInvitiGiudiceDB(ArrayList<Evento> eventiInvitati) throws SQLException{
        if(eventiInvitati != null) {
            HashSet<Evento> eventi = new HashSet<>(eventiInvitati);
            StringBuilder codiceSQL = new StringBuilder();
            for (Evento evento : eventi) {
                Partecipante partecipante = evento.firstInvitoGiudice();
                while(partecipante != null) {
                    codiceSQL.append("CALL updateInvitoGiudice('").append(partecipante.getNomeUtente()).append("',").append(evento.getIdEvento()).append(",").append(evento.getInvitoGiudiceAnswer()).append(");");
                    partecipante = evento.nextInvitoGiudice();
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public Partecipante getPartecipanteDB(String nomePartecipante) throws SQLException{
        Partecipante partecipante = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Partecipante AS p " +
                "JOIN PartecipanteEvento AS pe ON p.NomeUtente = pe.NomePartecipante " +
                "JOIN Evento AS e ON pe.idEvento = e.IdEvento " +
                "WHERE e.DataFine >= NOW() AND p.NomeUtente = '" + nomePartecipante + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                partecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                partecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<>();
                Evento evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                evento.setMaxTeam(rs.getInt(MAXTEAM));
                evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento, partecipante));
                getAllPartecipantiSingoliDB(evento, partecipante);
                getAllInvitiGiudiceDB(evento);
                eventi.add(evento);
                while (rs.next()) {
                    evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                    evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                    evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                    evento.setMaxTeam(rs.getInt(MAXTEAM));
                    evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                    evento.setOrganizzatore(getOrganizzatoreDB(evento));
                    evento.setGiudici(getAllGiudiciDB(evento));
                    evento.setTeamIscritti(getAllTeamDB(evento, partecipante));
                    getAllPartecipantiSingoliDB(evento, partecipante);
                    getAllInvitiGiudiceDB(evento);
                    eventi.add(evento);
                }
                getAllRichiesteTeamDB(partecipante);
                getAllVotiDB(partecipante);
                getAllProgressiDB(partecipante);
                rs.close();
                partecipante.setEventi(eventi);
            }
        }
        return partecipante;
    }

    public void getAllPartecipantiSingoliDB(Evento evento) throws SQLException{
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante " +
                "WHERE idEvento = " + idEvento + " AND NomeUtente NOT IN( " +
                "SELECT ct.NomePartecipante FROM Team AS t " +
                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "WHERE t.idEvento = " + idEvento + ");")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Partecipante partecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                    partecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                    partecipante.addEvento(evento);
                    evento.addPartecipante(partecipante);
                }
                rs.close();
            }
        }
    }

    public void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException{
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante " +
                "WHERE idEvento = " + idEvento + " AND NomeUtente NOT IN( " +
                "SELECT ct.NomePartecipante FROM Team AS t " +
                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "WHERE t.idEvento = " + idEvento + ");")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    if (partecipante.getNomeUtente().equals(rs.getString(NOMEUTENTE))) {
                        partecipante.addEvento(evento);
                        evento.addPartecipante(partecipante);
                    } else {
                        Partecipante nuovoPartecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                        nuovoPartecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                        nuovoPartecipante.addEvento(evento);
                        evento.addPartecipante(nuovoPartecipante);
                    }
                }
                rs.close();
            }
        }
    }

    public void addPartecipanteEventoDB(String nomePartecipante, ArrayList<Evento> partecipantiEvento) throws SQLException{
        if(partecipantiEvento != null) {
            HashSet<Evento> eventi = new HashSet<>(partecipantiEvento);
            StringBuilder codiceSQL = new StringBuilder();
            codiceSQL.append("INSERT INTO PartecipanteEvento(NomePartecipante, idEvento) VALUES");
            for (Evento evento : eventi) {
                if (evento != partecipantiEvento.getFirst())
                    codiceSQL.append(",");
                codiceSQL.append("('").append(nomePartecipante).append("',").append(evento.getIdEvento()).append(")");
            }
            codiceSQL.append(";");
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public void updatePartecipanteDB(String nomePartecipante, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE Partecipante SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                        WHERE_UTENTE + nomePartecipante + ";")) {
            ps.setString(1, fNome);
            ps.setString(2, mNome);
            ps.setString(3, lNome);
            ps.setObject(4, dataNascita);
            ps.executeUpdate();
        }
    }

    public Organizzatore getOrganizzatoreDB(String nomeUtente) throws SQLException {
        Organizzatore organizzatore = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Organizzatore AS o " +
                "JOIN OrganizzatoreEvento AS oe ON o.NomeUtente = oe.NomeOrganizzatore " +
                "JOIN Evento AS e ON oe.idEvento = e.IdEvento " +
                "WHERE e.DataFine >= NOW() AND o.NomeUtente = '" + nomeUtente + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                organizzatore = new Organizzatore(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                organizzatore.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<>();
                Evento evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                evento.setMaxTeam(rs.getInt(MAXTEAM));
                evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                evento.setOrganizzatore(organizzatore);
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
                eventi.add(evento);
                while (rs.next()) {
                    evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                    evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                    evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                    evento.setMaxTeam(rs.getInt(MAXTEAM));
                    evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                    evento.setOrganizzatore(organizzatore);
                    evento.setGiudici(getAllGiudiciDB(evento));
                    evento.setTeamIscritti(getAllTeamDB(evento));
                    getAllPartecipantiSingoliDB(evento);
                    getAllInvitiGiudiceDB(evento);
                    eventi.add(evento);
                }
                rs.close();
                organizzatore.setEventi(eventi);
            }
        }
        return organizzatore;
    }

    public Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException{
        Organizzatore organizzatore = null;
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Organizzatore JOIN OrganizzatoreEvento ON NomeUtente = NomeOrganizzatore " +
                "WHERE idEvento =" + idEvento + ";")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                organizzatore = new Organizzatore(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                organizzatore.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                rs.close();
                organizzatore.addEvento(evento);
            }
        }
        return organizzatore;
    }

    public void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException{
        addOrganizzatoreDB(organizzatore.getNomeUtente(), organizzatore.getPasswordUtente(), organizzatore.getfNome(), organizzatore.getmNome(), organizzatore.getlNome(), organizzatore.getDataNascita());
    }

    public void addOrganizzatoreDB(String nomeUtente, String password, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException{
        PreparedStatement ps =
                connection.prepareStatement(
                        "INSERT INTO Organizzatore(NomeUtente, Password," +
                        " FNome, MNome, LNome, DataNascita, idEvento)" +
                        "VALUES(?, ?, ?, ?, ?, ?, ?);");
        try {
            ps.setString(1, nomeUtente);
            ps.setString(2, password);
            ps.setString(3, fNome);
            ps.setString(4, mNome);
            ps.setString(5, lNome);
            ps.setObject(6, dataNascita);
            ps.executeUpdate();
        }
        finally{
            ps.close();
        }
    }

    public void addOrganizzatoreEventoDB(String nomeOrganizzatore, ArrayList<Evento> eventiOrganizzatore) throws SQLException{
        if(eventiOrganizzatore != null) {
            HashSet<Evento> eventi = new HashSet<>(eventiOrganizzatore);
            StringBuilder codiceSQL = new StringBuilder();
            codiceSQL.append("INSERT INTO OrganizzatoreEvento(NomeOrganizzatore, idEvento) VALUES");
            for (Evento evento : eventi) {
                if (evento != eventiOrganizzatore.getFirst())
                    codiceSQL.append(",");
                codiceSQL.append("('").append(nomeOrganizzatore).append("',").append(evento.getIdEvento()).append(")");
            }
            codiceSQL.append(";");
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public void updateOrganizzatoreDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE Organizzatore SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                        WHERE_UTENTE + nomeUtente + ";")) {
            ps.setString(1, fNome);
            ps.setString(2, mNome);
            ps.setString(3, lNome);
            ps.setObject(4, dataNascita);
            ps.executeUpdate();
        }
    }

    public Giudice getGiudiceDB(String nomeUtente) throws SQLException {
        Giudice giudice = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Giudice AS g " +
                "JOIN GiudiceEvento AS ge ON g.NomeUtente = ge.NomeGiudice " +
                "JOIN Evento AS e ON ge.idEvento = e.IdEvento " +
                "WHERE e.DataFine >= NOW() AND g.NomeUtente = '" + nomeUtente + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                giudice = new Giudice(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                giudice.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<>();
                Evento evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                evento.setMaxTeam(rs.getInt(MAXTEAM));
                evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento, giudice));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
                getAllVotiDB(evento);
                getAllProgressiDB(evento);
                eventi.add(evento);
                while (rs.next()) {
                    evento = new Evento(rs.getInt(IDEVENTO), rs.getString(TITOLO), rs.getString(INDIRIZZO), rs.getInt(NCIVICO));
                    evento.setDate(rs.getObject(DATAINIZIO, LocalDate.class), rs.getObject(DATAFINE, LocalDate.class));
                    evento.setMaxIscritti(rs.getInt(MAXISCRITTI));
                    evento.setMaxTeam(rs.getInt(MAXTEAM));
                    evento.setDateReg(rs.getObject(DATAINIZIOREG, LocalDate.class), rs.getObject(DATAFINEREG, LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString(DESCRIZIONEPROBLEMA));
                    evento.setOrganizzatore(getOrganizzatoreDB(evento));
                    evento.setGiudici(getAllGiudiciDB(evento, giudice));
                    evento.setTeamIscritti(getAllTeamDB(evento));
                    getAllPartecipantiSingoliDB(evento);
                    getAllInvitiGiudiceDB(evento);
                    getAllVotiDB(evento);
                    getAllProgressiDB(evento);
                    eventi.add(evento);
                }
                rs.close();
                giudice.setEventi(eventi);
            }
        }
        return giudice;
    }

    public ArrayList<Giudice> getAllGiudiciDB(Evento evento) throws SQLException{
        ArrayList<Giudice> giudici = null;
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Giudice " +
                "JOIN GiudiceEvento ON NomeUtente = NomeGiudice" +
                " WHERE idEvento = " + idEvento + ";")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                giudici = new ArrayList<>();
                while (rs.next()) {
                    Giudice giudice = new Giudice(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                    giudice.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                    giudice.addEvento(evento);
                    giudici.add(giudice);
                }
                rs.close();
            }
        }
        return giudici;
    }

    public ArrayList<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException{
        ArrayList<Giudice> giudici = new ArrayList<>();
        giudici.add(giudice);
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Giudice " +
                "JOIN GiudiceEvento ON NomeUtente = NomeGiudice " +
                "WHERE idEvento = " + evento.getIdEvento() +
                " AND NomeGiudice <> '" + giudice.getNomeUtente() + "';")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                while (rs.next()) {
                    Giudice nuovoGiudice = new Giudice(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                    nuovoGiudice.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                    nuovoGiudice.addEvento(evento);
                    giudici.add(nuovoGiudice);
                }
                rs.close();
            }
        }
        return giudici;
    }

    public void updateGiudiceDB(String nomeUtente, String fNome, String mNome, String lNome, LocalDate dataNascita) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "UPDATE Giudice SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                        WHERE_UTENTE + nomeUtente + ";")) {
            ps.setString(1, fNome);
            ps.setString(2, mNome);
            ps.setString(3, lNome);
            ps.setObject(4, dataNascita);
            ps.executeUpdate();
        }
    }

    public ArrayList<Team> getAllTeamDB(Evento evento) throws SQLException{
        ArrayList<Team> teams = null;
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "JOIN Partecipante AS p ON ct.NomePartecipante = p.NomeUtente " +
                "WHERE t.idEvento = " + idEvento + ORDER)) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                teams = new ArrayList<>();
                rs.next();
                int idTeam = rs.getInt(IDTEAM);
                Team team = new Team(idTeam, rs.getString(NOMETEAM), rs.getString(TEAMLEADER));
                team.setEventoIscritto(evento);
                Partecipante partecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                partecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                partecipante.addEvento(evento);
                partecipante.addTeam(team);
                team.addMembroTeam(partecipante);
                evento.addPartecipante(partecipante);
                while (rs.next()) {
                    if (idTeam != rs.getInt(IDTEAM)) {
                        teams.add(team);
                        idTeam = rs.getInt(IDTEAM);
                        team = new Team(idTeam, rs.getString(NOMETEAM), rs.getString(TEAMLEADER));
                        team.setEventoIscritto(evento);
                    }
                    partecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                    partecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                    partecipante.addEvento(evento);
                    partecipante.addTeam(team);
                    team.addMembroTeam(partecipante);
                    evento.addPartecipante(partecipante);
                }
                teams.add(team);
                rs.close();
            }
        }
        return teams;
    }

    public ArrayList<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException{
        ArrayList<Team> teams = null;
        int idEvento = evento.getIdEvento();
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "JOIN Partecipante AS p ON ct.NomePartecipante = p.NomeUtente " +
                "WHERE t.idEvento = " + idEvento + ORDER)) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                teams = new ArrayList<>();
                rs.next();
                int idTeam = rs.getInt(IDTEAM);
                Team team = new Team(idTeam, rs.getString(NOMETEAM), rs.getString(TEAMLEADER));
                team.setEventoIscritto(evento);
                Partecipante nuovoPartecipante = null;
                if (partecipante.getNomeUtente().equals(rs.getString(NOMEUTENTE))) {
                    partecipante.addTeam(team);
                    team.addMembroTeam(partecipante);
                    evento.addPartecipante(partecipante);
                } else {
                    nuovoPartecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                    nuovoPartecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                    nuovoPartecipante.addEvento(evento);
                    nuovoPartecipante.addTeam(team);
                    team.addMembroTeam(nuovoPartecipante);
                    evento.addPartecipante(nuovoPartecipante);
                }
                while (rs.next()) {
                    if (idTeam != rs.getInt(IDTEAM)) {
                        teams.add(team);
                        idTeam = rs.getInt(IDTEAM);
                        team = new Team(idTeam, rs.getString(NOMETEAM), rs.getString(TEAMLEADER));
                        team.setEventoIscritto(evento);
                    }
                    if (partecipante.getNomeUtente().equals(rs.getString(NOMEUTENTE))) {
                        partecipante.addTeam(team);
                        team.addMembroTeam(partecipante);
                        evento.addPartecipante(partecipante);
                    } else {
                        nuovoPartecipante = new Partecipante(rs.getString(NOMEUTENTE), rs.getString(PASSWORD));
                        nuovoPartecipante.setDati(rs.getString(FNOME), rs.getString(MNOME), rs.getString(LNOME), rs.getObject(DATANASCITA, LocalDate.class));
                        nuovoPartecipante.addEvento(evento);
                        nuovoPartecipante.addTeam(team);
                        team.addMembroTeam(nuovoPartecipante);
                        evento.addPartecipante(nuovoPartecipante);
                    }
                }
                teams.add(team);
                rs.close();
            }
        }
        return teams;
    }

    public int getIdTeamDB() throws SQLException{
        int id;
        try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(IdTeam) FROM Team;")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        return id;
    }

    public int addTeamDB(Team team) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement("CALL CreaTeam('" + team.getNome() + "','" + team.getTeamLeader() + "'," + team.getEventoIscritto().getIdEvento() + ");")) {
            ps.executeUpdate();
        }
        return getIdTeamDB();
    }

    public void getAllRichiesteTeamDB(Partecipante partecipante) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM RichiestaTeam " +
                "WHERE Risposta IS NULL AND idTeam IN(" +
                "SELECT idTeam FROM CompTeam " +
                "WHERE NomePartecipante = '" + partecipante.getNomeUtente() + "');")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                Team team = partecipante.seekTeam(rs.getInt(IDTEAM));
                team.addRichiesta(team.getEventoIscritto().seekPartecipante(rs.getString(NOMEPARTECIPANTE)));
                while (rs.next()) {
                    if (team.getIdTeam() != rs.getInt(IDTEAM))
                        team = partecipante.seekTeam(rs.getInt(IDTEAM));
                    team.addRichiesta(team.getEventoIscritto().seekPartecipante(rs.getString(NOMEPARTECIPANTE)));
                }
            }
        }
    }

    public void addRichiesteTeamDB(ArrayList<Team> richiesteTeam) throws SQLException{
        if(richiesteTeam != null) {
            HashSet<Team> teams = new HashSet<>(richiesteTeam);
            StringBuilder codiceSQL = new StringBuilder();
            for (Team team : teams) {
                Partecipante partecipante = team.firstRichiesta();
                while(partecipante != null) {
                    codiceSQL.append("CALL updateRichiestaTeam('").append(partecipante.getNomeUtente()).append("',").append(team.getIdTeam()).append(",").append(team.getRichiestaAnswer()).append(");");
                    partecipante = team.nextRichiesta();
                }
            }
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public void getAllProgressiDB(Evento evento) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT t.IdTeam, p.IdProgresso, p.DataPubblicazione," +
                "p.Testo AS TestoProgresso, c.NomeGiudice, c.Testo AS TestoCommento " +
                "FROM Team AS t JOIN Progresso AS p ON t.IdTeam = p.idTeam " +
                "JOIN Commento AS c ON p.IdProgresso = c.idProgresso " +
                "WHERE t.idEvento = " + evento.getIdEvento() +
                " ORDER BY t.IdTeam, p.IdProgresso;")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                ArrayList<Progresso> progressi = new ArrayList<>();
                ArrayList<Commento> commenti = new ArrayList<>();
                Team team = evento.seekTeam(rs.getInt(IDTEAM));
                Progresso progresso = new Progresso(rs.getInt(IDPROGRESSO), team.getIdTeam(), rs.getObject(DATAPUBBLICAZIONE, LocalDate.class), rs.getString(TESTOPROGRESSO));
                commenti.add(new Commento(progresso.getIdProgresso(), rs.getString(TESTOCOMMENTO), rs.getString(NOMEGIUDICE)));
                while (rs.next()) {
                    if (progresso.getIdProgresso() != rs.getInt(IDPROGRESSO)) {
                        progresso.setCommenti(commenti);
                        progressi.add(progresso);
                        progresso = new Progresso(rs.getInt(IDPROGRESSO), team.getIdTeam(), rs.getObject(DATAPUBBLICAZIONE, LocalDate.class), rs.getString(TESTOPROGRESSO));
                    }
                    if (team.getIdTeam() != rs.getInt(IDTEAM)) {
                        team.setProgressi(progressi);
                        team = evento.seekTeam(rs.getInt(IDTEAM));
                    }
                    commenti.add(new Commento(progresso.getIdProgresso(), rs.getString(TESTOCOMMENTO), rs.getString(NOMEGIUDICE)));
                }
                progresso.setCommenti(commenti);
                progressi.add(progresso);
                team.setProgressi(progressi);
            }
        }
    }

    public void getAllProgressiDB(Partecipante partecipante) throws SQLException{
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT ct.idTeam, p.IdProgresso, p.DataPubblicazione," +
                "p.Testo AS TestoProgresso, c.NomeGiudice, c.Testo AS TestoCommento " +
                "FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "JOIN Evento AS e ON t.idEvento = e.IdEvento " +
                "JOIN Progresso AS p ON ct.idTeam = p.idTeam " +
                "JOIN Commento AS c ON p.IdProgresso = c.idProgresso " +
                "WHERE e.DataFine >= NOW() AND ct.NomePartecipante = '" + partecipante.getNomeUtente() + "' " +
                "ORDER BY ct.idTeam, p.IdProgresso;")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                ArrayList<Progresso> progressi = new ArrayList<>();
                ArrayList<Commento> commenti = new ArrayList<>();
                Team team = partecipante.seekTeam(rs.getInt(IDTEAM));
                Progresso progresso = new Progresso(rs.getInt(IDPROGRESSO), team.getIdTeam(), rs.getObject(DATAPUBBLICAZIONE, LocalDate.class), rs.getString(TESTOPROGRESSO));
                commenti.add(new Commento(progresso.getIdProgresso(), rs.getString(TESTOCOMMENTO), rs.getString(NOMEGIUDICE)));
                while (rs.next()) {
                    if (progresso.getIdProgresso() != rs.getInt(IDPROGRESSO)) {
                        progresso.setCommenti(commenti);
                        progressi.add(progresso);
                        progresso = new Progresso(rs.getInt(IDPROGRESSO), team.getIdTeam(), rs.getObject(DATAPUBBLICAZIONE, LocalDate.class), rs.getString(TESTOPROGRESSO));
                    }
                    if (team.getIdTeam() != rs.getInt(IDTEAM)) {
                        team.setProgressi(progressi);
                        team = partecipante.seekTeam(rs.getInt(IDTEAM));
                    }
                    commenti.add(new Commento(progresso.getIdProgresso(), rs.getString(TESTOCOMMENTO), rs.getString(NOMEGIUDICE)));
                }
                progresso.setCommenti(commenti);
                progressi.add(progresso);
                team.setProgressi(progressi);
            }
        }
    }


    public int getIdProgressoDB() throws SQLException{
        int id = -1;
        try (PreparedStatement ps = connection.prepareStatement("SELECT MAX(IdProgresso) FROM Progresso;")) {
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        return id;
    }

    public void addProgressoDB(Progresso progresso) throws SQLException{
        progresso.setIdProgresso(addProgressoDB(progresso.getIdTeam(), progresso.getTestoDocumeto()));
    }

    public int addProgressoDB(int idTeam, String testo) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO Progresso(idTeam, Testo) VALUES("
                + idTeam + ",'" + testo + "');")) {
            ps.executeUpdate();
        }
        return getIdProgressoDB();
    }

    public ArrayList<Commento> getAllCommentiDB(String giudice) throws SQLException{
        ArrayList<Commento> commenti = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Commento WHERE NomeGiudice = " + giudice + ";")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Commento commento = new Commento(rs.getInt(IDPROGRESSO), giudice, rs.getString("testo"));
                if (commenti == null) {
                    commenti = new ArrayList<>();
                }
                commenti.add(commento);
            }
        }
        return commenti;
    }

    public void addAllCommentiDB(ArrayList<Commento> nuoviCommenti) throws SQLException{
        if(nuoviCommenti != null){
            HashSet<Commento> commenti = new HashSet<>(nuoviCommenti);
            StringBuilder codiceSQL = new StringBuilder();
            codiceSQL.append("INSERT INTO Commento(NomeGiudice, idProgresso, Testo) VALUES");
            for (Commento commento : commenti) {
                if(commento != nuoviCommenti.getFirst())
                    codiceSQL.append(",");
                codiceSQL.append("('").append(commento.getGiudice()).append("',").append(commento.getIdProgresso()).append(",'").append(commento.getTesto()).append("')");
            }
            codiceSQL.append(";");
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }

    public ArrayList<Voto> getAllVotiDB(String giudice) throws SQLException{
        ArrayList<Voto> voti = null;
        try (PreparedStatement ps = connection.prepareStatement("SELECT * FROM Voto WHERE NomeGiudice = " + giudice + ";")) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Voto voto = new Voto(rs.getInt(IDTEAM), rs.getInt(VALORE), giudice);
                if (voti == null)
                    voti = new ArrayList<>();
                voti.add(voto);
            }
        }
        return voti;
    }

    public void getAllVotiDB(Evento evento) throws SQLException{
        ArrayList<Voto> voti = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT * FROM Team AS t " +
                "JOIN Voto AS v ON t.IdTeam = v.idTeam " +
                "WHERE t.idEvento = " + evento.getIdEvento() +
                        ORDER)) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                voti = new ArrayList<>();
                Team team = evento.seekTeam(rs.getInt(IDTEAM));
                voti.add(new Voto(rs.getInt(IDTEAM), rs.getInt(VALORE), rs.getString(NOMEGIUDICE)));
                while (rs.next()) {
                    if (team.getIdTeam() != rs.getInt(IDTEAM)) {
                        team.setVoti(voti);
                        voti = new ArrayList<>();
                        team = evento.seekTeam(rs.getInt(IDTEAM));
                    }
                    voti.add(new Voto(rs.getInt(IDTEAM), rs.getInt(VALORE), rs.getString(NOMEGIUDICE)));
                }
                team.setVoti(voti);
            }
        }
    }

    public void getAllVotiDB(Partecipante partecipante) throws SQLException{
        ArrayList<Voto> voti = null;
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT t.IdTeam, v.Valore, v.NomeGiudice FROM Team AS t " +
                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                "JOIN Evento AS e ON t.idEvento = e.IdEvento " +
                "JOIN Voto AS v ON ct.idTeam = v.idTeam " +
                "WHERE e.DataFine >= NOW() " +
                "AND ct.NomePartecipante = '" + partecipante.getNomeUtente() + "' " +
                "ORDER BY t.IdTeam;")) {
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                voti = new ArrayList<>();
                Team team = partecipante.seekTeam(rs.getInt(IDTEAM));
                voti.add(new Voto(rs.getInt(IDTEAM), rs.getInt(VALORE), rs.getString(NOMEGIUDICE)));
                while (rs.next()) {
                    if (team.getIdTeam() != rs.getInt(IDTEAM)) {
                        team.setVoti(voti);
                        voti = new ArrayList<>();
                        team = partecipante.seekTeam(rs.getInt(IDTEAM));
                    }
                    voti.add(new Voto(rs.getInt(IDTEAM), rs.getInt(VALORE), rs.getString(NOMEGIUDICE)));
                }
                team.setVoti(voti);
            }
        }
    }

    public void addAllVotiDB(ArrayList<Voto> nuoviVoti) throws SQLException{
        if(nuoviVoti != null){
            HashSet<Voto> voti = new HashSet<>(nuoviVoti);
            StringBuilder codiceSQL = new StringBuilder();
            codiceSQL.append("INSERT INTO Voto(NomeGiudice, idTeam, Valore) VALUES");
            for (Voto voto : voti) {
                if(voto != nuoviVoti.getFirst())
                    codiceSQL.append(",");
                codiceSQL.append("('").append(voto.getGiudice()).append("',").append(voto.getIdTeam()).append(",").append(voto.getValore()).append(")");
            }
            codiceSQL.append(";");
            try (PreparedStatement ps = connection.prepareStatement(codiceSQL.toString())) {
                ps.executeUpdate();
            }
        }
    }
}
