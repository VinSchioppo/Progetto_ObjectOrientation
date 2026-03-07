package DAO;

import ClassModel.*;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;

public class ImplementazioneDAO implements InterfacciaDAO {
    private Connection connection = null;

    public ImplementazioneDAO() {
        try {
            connection = ConnessioneDatabase.getInstance().connection;
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

    public boolean checkLoginDB(String NomeUtente, String Password) throws SQLException{
        boolean success = true;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT NomeUtente, Password FROM Partecipante" +
                            " WHERE NomeUtente = '" + NomeUtente + "' AND Password = '" + Password +"';");
            ResultSet rs = ps.executeQuery();
            if (!rs.isBeforeFirst()) {
                success = false;
            }
            rs.close();
        }
        catch(SQLException e) {
            throw e;
        }
        return success;
    }

    public int getIdEventoDB() throws SQLException{
        int result;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdEvento) FROM Evento;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            result =  rs.getInt(1);
            rs.close();
        }
        catch (SQLException e) {
            throw e;
        }
        return result;
    }

    public Evento getEventoDB(int IdEvento) throws SQLException{
        Evento evento = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Evento WHERE IdEvento =" + IdEvento + ";");
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                evento = new Evento(IdEvento);
                rs.next();
                evento.setTitolo(rs.getString("titolo"));
                evento.setIndirizzoSede(rs.getString("indirizzosede"));
                evento.setNCivicoSede(rs.getInt("ncivicosede"));
                evento.setMaxIscritti(rs.getInt("maxiscritti"));
                evento.setMaxTeam(rs.getInt("maxteam"));
                evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                rs.close();
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return evento;
    }

    public ArrayList<Evento> getEventiApertiDB(String NomeUtente) throws SQLException{
        ArrayList<Evento> eventi = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Evento " +
                                                    "WHERE DataInizioReg <= NOW() AND DataFineReg >= NOW()\n" +
                                                    "AND IdEvento NOT IN( " +
                                                    "SELECT pe.idEvento FROM PartecipanteEvento AS pe " +
                                                    "JOIN OrganizzatoreEvento AS oe ON pe.idEvento = oe.idEvento " +
                                                    "JOIN GiudiceEvento AS ge ON pe.idEvento = ge.idEvento " +
                                                    "WHERE pe.NomePartecipante = '" + NomeUtente +"');");
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                eventi = new ArrayList<Evento>();
                while (rs.next()) {
                    Evento evento = new Evento(rs.getInt("idevento"));
                    evento.setTitolo(rs.getString("titolo"));
                    evento.setIndirizzoSede(rs.getString("indirizzosede"));
                    evento.setNCivicoSede(rs.getInt("ncivicosede"));
                    evento.setMaxIscritti(rs.getInt("maxiscritti"));
                    evento.setMaxTeam(rs.getInt("maxteam"));
                    evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                    evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                    eventi.add(evento);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return eventi;
    }

    public void addEventoDB(Evento evento) throws SQLException {
        try {
            evento.setIdEvento(addEventoDB(evento.getTitolo(), evento.getIndirizzoSede(), evento.getNCivicoSede(), evento.getDataInizio(), evento.getDataFine(), evento.getMaxIscritti(), evento.getMaxTeam(), evento.getDataInizioReg(), evento.getDataFineReg(), evento.getDescrizioneProblema()));
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public int addEventoDB(String Titolo, String Indirizzo, int NCivico, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede, DataInizio, DataFine," +
                                                    " MaxIscritti, MaxTeam, DataInizioReg, DataFineReg, DescrizioneProb)" +
                                                    " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, Titolo);
            ps.setString(2, Indirizzo);
            if(NCivico == -1)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, NCivico);
            ps.setObject(4, DataInizio);
            ps.setObject(5, DataFine);
            if(MaxIscritti == -1)
                ps.setNull(6, Types.INTEGER);
            else
                ps.setInt(6, MaxIscritti);
            if(MaxTeam == -1)
                ps.setNull(7, Types.INTEGER);
            else
                ps.setInt(7, MaxTeam);
            ps.setObject(8, DataInizioReg);
            ps.setObject(9, DataFineReg);
            ps.setString(10, DescrizioneProb);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdEventoDB();
    }

    public void updateEventoDB(int IdEvento,  String Indirizzo, int NCivico, int MaxIscritti, int MaxTeam) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Evento SET Titolo = ?, IndirizzoSede = ?, NCivicoSede = ?, " +
                                                "MaxIscritti = ?, MaxTeam = ? WHERE IdEvento = ?;");
            ps.setString(1, Indirizzo);
            if(NCivico == -1)
                ps.setNull(2, Types.INTEGER);
            else
                ps.setInt(2, NCivico);
            if(MaxIscritti == -1)
                ps.setNull(3, Types.INTEGER);
            else
                ps.setInt(3, MaxIscritti);
            if(MaxTeam == -1)
                ps.setNull(4, Types.INTEGER);
            else
                ps.setInt(4, MaxTeam);
            ps.setInt(5, IdEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void updateDateEventoDB(int IdEvento, LocalDate DataInizio, LocalDate DataFine) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Evento SET DataInizio = ?, DataFine = ? WHERE IdEvento = ? ;");
            ps.setObject(1, DataInizio);
            ps.setObject(2, DataFine);
            ps.setInt(3, IdEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void updateDateRegEventoDB(int IdEvento, LocalDate DataInizioReg, LocalDate DataFineReg) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Evento SET DataInizioReg = ?, DataFineReg = ? WHERE IdEvento = ? ;");
            ps.setObject(1, DataInizioReg);
            ps.setObject(2, DataFineReg);
            ps.setInt(3, IdEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void updateProblemaDB(ArrayList<Evento> eventiProblema) throws SQLException{
        if(eventiProblema != null){
            HashSet<Evento> eventi = new HashSet<Evento>(eventiProblema);
            String codiceSQL = "";
            for (Evento evento : eventi) {
                codiceSQL = codiceSQL + "UPDATE Evento SET DescrizioneProb = '" + evento.getDescrizioneProblema() + "' WHERE IdEvento = " + evento.getIdEvento() + ";";
            }
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public Utente getUtenteDB(String NomeUtente) throws SQLException{
        Utente utente = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante WHERE NomeUtente = '" + NomeUtente + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                utente = new Utente();
                rs.next();
                utente.setNomeUtente(rs.getString("nomeutente"));
                utente.setPasswordUtente(rs.getString("password"));
                utente.setFNome(rs.getString("fnome"));
                utente.setMNome(rs.getString("mnome"));
                utente.setLNome(rs.getString("lnome"));
                utente.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
            }

        }
        catch(SQLException e) {
            throw e;
        }
        return utente;
    }

    public void addUtenteDB(String NomeUtente, String Password) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void getAllInvitiGiudiceDB(Evento evento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM InvitoGiudice WHERE Risposta IS NULL AND idEvento = " + evento.getIdEvento() + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    Partecipante partecipante = evento.seekPartecipante(rs.getString("nomepartecipante"));
                    if(partecipante != null)
                        evento.addInvitoGiudice(partecipante);
                }
                rs.close();
            }

        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void getAllInvitiGiudiceDB(Utente utente, Partecipante partecipante) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM InvitoGiudice WHERE Risposta IS NULL AND NomePartecipante = '" + utente.getNomeUtente() + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()){
                    Evento evento = partecipante.seekEvento(rs.getInt("idevento"));
                    if(evento != null)
                        utente.addInvitoGiudiceEvento(evento);
                }
                rs.close();
            }

        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void addInvitiGiudiceDB(ArrayList<Evento> eventiInvitati) throws SQLException{
        if(eventiInvitati != null) {
            HashSet<Evento> eventi = new HashSet<Evento>(eventiInvitati);
            String codiceSQL = "";
            for (Evento evento : eventi) {
                Partecipante partecipante = evento.firstInvitoGiudice();
                while(partecipante != null) {
                    codiceSQL = codiceSQL + "CALL updateInvitoGiudice('" + partecipante.getNomeUtente() + "'," + evento.getIdEvento() + "," + evento.getInvitoGiudiceAnswer() + ");";
                    partecipante = evento.nextInvitoGiudice();
                }
            }
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public Partecipante getPartecipanteDB(String NomePartecipante) throws SQLException{
        Partecipante partecipante = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante AS p " +
                                                "JOIN PartecipanteEvento AS pe ON p.NomeUtente = pe.NomePartecipante " +
                                                "JOIN Evento AS e ON pe.idEvento = e.IdEvento " +
                                                "WHERE e.DataFine >= NOW() AND p.NomeUtente = '" + NomePartecipante + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                partecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                partecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<Evento>();
                Evento evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                evento.setMaxIscritti(rs.getInt("maxiscritti"));
                evento.setMaxTeam(rs.getInt("maxteam"));
                evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento, partecipante));
                getAllPartecipantiSingoliDB(evento, partecipante);
                getAllInvitiGiudiceDB(evento);
                eventi.add(evento);
                while(rs.next()){
                    evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                    evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                    evento.setMaxIscritti(rs.getInt("maxiscritti"));
                    evento.setMaxTeam(rs.getInt("maxteam"));
                    evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
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
        catch(SQLException e) {
            throw e;
        }
        return partecipante;
    }

    public void getAllPartecipantiSingoliDB(Evento evento) throws SQLException{
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante " +
                                                "WHERE idEvento = " + idEvento + " AND NomeUtente NOT IN( " +
                                                "SELECT ct.NomePartecipante FROM Team AS t " +
                                                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "WHERE t.idEvento = " + idEvento + ");");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()) {
                    Partecipante partecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                    partecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                    partecipante.addEvento(evento);
                    evento.addPartecipante(partecipante);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void getAllPartecipantiSingoliDB(Evento evento, Partecipante partecipante) throws SQLException{
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante " +
                                                "WHERE idEvento = " + idEvento + " AND NomeUtente NOT IN( " +
                                                "SELECT ct.NomePartecipante FROM Team AS t " +
                                                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "WHERE t.idEvento = " + idEvento + ");");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()) {
                    if(partecipante.getNomeUtente().equals(rs.getString("nomeutente"))) {
                        partecipante.addEvento(evento);
                        evento.addPartecipante(partecipante);
                    }
                    else {
                        Partecipante nuovoPartecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                        nuovoPartecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                        nuovoPartecipante.addEvento(evento);
                        evento.addPartecipante(nuovoPartecipante);
                    }
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteEventoDB(String NomePartecipante, ArrayList<Evento> partecipantiEvento) throws SQLException{
        if(partecipantiEvento != null) {
            HashSet<Evento> eventi = new HashSet<Evento>(partecipantiEvento);
            String codiceSQL = "INSERT INTO PartecipanteEvento(NomePartecipante, idEvento) VALUES";
            for (Evento evento : eventi) {
                if (evento != partecipantiEvento.getFirst())
                    codiceSQL = codiceSQL + ",";
                codiceSQL = codiceSQL + "('" + NomePartecipante + "'," + evento.getIdEvento() + ")";
            }
            codiceSQL = codiceSQL + ";";
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updatePartecipanteDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Partecipante SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Organizzatore getOrganizzatoreDB(String NomeUtente) throws SQLException {
        Organizzatore organizzatore = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Organizzatore AS o " +
                                                "JOIN OrganizzatoreEvento AS oe ON o.NomeUtente = oe.NomeOrganizzatore " +
                                                "JOIN Evento AS e ON oe.idEvento = e.IdEvento " +
                                                "WHERE e.DataFine >= NOW() AND o.NomeUtente = '" + NomeUtente + "';");
            ResultSet rs = ps.executeQuery();
            if (rs.isBeforeFirst()) {
                rs.next();
                organizzatore = new Organizzatore(rs.getString("nomeutente"), rs.getString("password"));
                organizzatore.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<Evento>();
                Evento evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                evento.setMaxIscritti(rs.getInt("maxiscritti"));
                evento.setMaxTeam(rs.getInt("maxteam"));
                evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                evento.setOrganizzatore(organizzatore);
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
                eventi.add(evento);
                while(rs.next()) {
                    evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                    evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                    evento.setMaxIscritti(rs.getInt("maxiscritti"));
                    evento.setMaxTeam(rs.getInt("maxteam"));
                    evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
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
        } catch (SQLException e) {
            throw e;
        }
        return organizzatore;
    }

    public Organizzatore getOrganizzatoreDB(Evento evento) throws SQLException{
        Organizzatore organizzatore = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Organizzatore JOIN OrganizzatoreEvento ON NomeUtente = NomeOrganizzatore " +
                            "WHERE idEvento =" + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                organizzatore = new Organizzatore(rs.getString("nomeutente"), rs.getString("password"));
                organizzatore.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                rs.close();
                organizzatore.addEvento(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return organizzatore;
    }

    public void addOrganizzatoreDB(Organizzatore organizzatore) throws SQLException{
        try {
            addOrganizzatoreDB(organizzatore.getNomeUtente(), organizzatore.getPasswordUtente(), organizzatore.getFNome(), organizzatore.getMNome(), organizzatore.getLNome(), organizzatore.getDataNascita());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, FNome, MNome, LNome, DataNascita, idEvento)" +
                                                "VALUES(?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreEventoDB(String NomeOrganizzatore, ArrayList<Evento> eventiOrganizzatore) throws SQLException{
        if(eventiOrganizzatore != null) {
            HashSet<Evento> eventi = new HashSet<Evento>(eventiOrganizzatore);
            String codiceSQL = "INSERT INTO OrganizzatoreEvento(NomeOrganizzatore, idEvento) VALUES";
            for (Evento evento : eventi) {
                if (evento != eventiOrganizzatore.getFirst())
                    codiceSQL = codiceSQL + ",";
                codiceSQL = codiceSQL + "('" + NomeOrganizzatore + "'," + evento.getIdEvento() + ")";
            }
            codiceSQL = codiceSQL + ";";
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updateOrganizzatoreDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Organizzatore SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Giudice getGiudiceDB(String NomeUtente) throws SQLException {
        Giudice giudice = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice AS g " +
                                                "JOIN GiudiceEvento AS ge ON g.NomeUtente = ge.NomeGiudice " +
                                                "JOIN Evento AS e ON ge.idEvento = e.IdEvento " +
                                                "WHERE e.DataFine >= NOW() AND g.NomeUtente = '" + NomeUtente + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                giudice = new Giudice(rs.getString("nomeutente"), rs.getString("password"));
                giudice.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                ArrayList<Evento> eventi = new ArrayList<Evento>();
                Evento evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                evento.setMaxIscritti(rs.getInt("maxiscritti"));
                evento.setMaxTeam(rs.getInt("maxteam"));
                evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento, giudice));
                evento.setTeamIscritti(getAllTeamDB(evento));
                getAllPartecipantiSingoliDB(evento);
                getAllInvitiGiudiceDB(evento);
                getAllVotiDB(evento);
                getAllProgressiDB(evento);
                eventi.add(evento);
                while(rs.next()) {
                    evento = new Evento(rs.getInt("idevento"), rs.getString("titolo"), rs.getString("indirizzosede"), rs.getInt("ncivicosede"));
                    evento.setDate(rs.getObject("datainizio", LocalDate.class), rs.getObject("datafine", LocalDate.class));
                    evento.setMaxIscritti(rs.getInt("maxiscritti"));
                    evento.setMaxTeam(rs.getInt("maxteam"));
                    evento.setDateReg(rs.getObject("datainizioreg", LocalDate.class), rs.getObject("datafinereg", LocalDate.class));
                    evento.setDescrizioneProblema(rs.getString("descrizioneprob"));
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
        catch(SQLException e) {
            throw e;
        }
        return giudice;
    }

    public ArrayList<Giudice> getAllGiudiciDB(Evento evento) throws SQLException{
        ArrayList<Giudice> giudici = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice " +
                                                "JOIN GiudiceEvento ON NomeUtente = NomeGiudice" +
                                                " WHERE idEvento = " + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                giudici = new ArrayList<Giudice>();
                while(rs.next()) {
                    Giudice giudice = new Giudice();
                    giudice.setNomeUtente(rs.getString("nomeutente"));
                    giudice.setPasswordUtente(rs.getString("password"));
                    giudice.setFNome(rs.getString("fnome"));
                    giudice.setMNome(rs.getString("mnome"));
                    giudice.setLNome(rs.getString("lnome"));
                    giudice.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                    giudice.addEvento(evento);
                    giudici.add(giudice);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return giudici;
    }

    public ArrayList<Giudice> getAllGiudiciDB(Evento evento, Giudice giudice) throws SQLException{
        ArrayList<Giudice> giudici = new ArrayList<Giudice>();
        giudici.add(giudice);
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice " +
                                                "JOIN GiudiceEvento ON NomeUtente = NomeGiudice " +
                                                "WHERE idEvento = " + evento.getIdEvento() +
                                                " AND NomeGiudice <> '" + giudice.getNomeUtente() + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                while(rs.next()) {
                    Giudice nuovoGiudice = new Giudice(rs.getString("nomeutente"), rs.getString("password"));
                    nuovoGiudice.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                    nuovoGiudice.addEvento(evento);
                    giudici.add(nuovoGiudice);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return giudici;
    }

    public void updateGiudiceDB(String NomeUtente, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Giudice SET FNome = ?, MNome = ?, LNome = ?, DataNascita = ?" +
                            " WHERE NomeUtente = " + NomeUtente + ";");
            ps.setString(1, FNome);
            ps.setString(2, MNome);
            ps.setString(3, LNome);
            ps.setObject(4, DataNascita);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public ArrayList<Team> getAllTeamDB(Evento evento) throws SQLException{
        ArrayList<Team> teams = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "JOIN Partecipante AS p ON ct.NomePartecipante = p.NomeUtente " +
                                                "WHERE t.idEvento = " + idEvento + " ORDER BY t.idTeam;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                teams = new ArrayList<Team>();
                rs.next();
                int idTeam = rs.getInt("idteam");
                Team team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
                team.setEventoIscritto(evento);
                Partecipante partecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                partecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                partecipante.addEvento(evento);
                partecipante.addTeam(team);
                team.addMembroTeam(partecipante);
                evento.addPartecipante(partecipante);
                while(rs.next()) {
                    if(idTeam != rs.getInt("idteam")) {
                        teams.add(team);
                        idTeam = rs.getInt("idteam");
                        team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
                        team.setEventoIscritto(evento);
                    }
                    partecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                    partecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                    partecipante.addEvento(evento);
                    partecipante.addTeam(team);
                    team.addMembroTeam(partecipante);
                    evento.addPartecipante(partecipante);
                }
                teams.add(team);
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return teams;
    }

    public ArrayList<Team> getAllTeamDB(Evento evento, Partecipante partecipante) throws SQLException{
        ArrayList<Team> teams = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "JOIN Partecipante AS p ON ct.NomePartecipante = p.NomeUtente " +
                                                "WHERE t.idEvento = " + idEvento + " ORDER BY t.idTeam;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                teams = new ArrayList<Team>();
                rs.next();
                int idTeam = rs.getInt("idteam");
                Team team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
                team.setEventoIscritto(evento);
                Partecipante nuovoPartecipante = null;
                if(partecipante.getNomeUtente().equals(rs.getString("nomeutente"))){
                    partecipante.addTeam(team);
                    team.addMembroTeam(partecipante);
                    evento.addPartecipante(partecipante);
                }
                else {
                    nuovoPartecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                    nuovoPartecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                    nuovoPartecipante.addEvento(evento);
                    nuovoPartecipante.addTeam(team);
                    team.addMembroTeam(nuovoPartecipante);
                    evento.addPartecipante(nuovoPartecipante);
                }
                while(rs.next()) {
                    if(idTeam != rs.getInt("idteam")) {
                        teams.add(team);
                        idTeam = rs.getInt("idteam");
                        team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
                        team.setEventoIscritto(evento);
                    }
                    if(partecipante.getNomeUtente().equals(rs.getString("nomeutente"))){
                        partecipante.addTeam(team);
                        team.addMembroTeam(partecipante);
                        evento.addPartecipante(partecipante);
                    }
                    else {
                        nuovoPartecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                        nuovoPartecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
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
        catch(SQLException e) {
            throw e;
        }
        return teams;
    }

    public int getIdTeamDB() throws SQLException{
        int id;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdTeam) FROM Team;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
    }

    public int addTeamDB(Team team) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL CreaTeam('" + team.getNome() + "','" + team.getTeamLeader() + "'," + team.getEventoIscritto().getIdEvento() + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdTeamDB();
    }

    public void getAllRichiesteTeamDB(Partecipante partecipante) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM RichiestaTeam " +
                                                "WHERE Risposta IS NULL AND idTeam IN(" +
                                                "SELECT idTeam FROM CompTeam " +
                                                "WHERE NomePartecipante = '" + partecipante.getNomeUtente() + "');");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                Team team = partecipante.seekTeam(rs.getInt("idteam"));
                team.addRichiesta(team.getEventoIscritto().seekPartecipante(rs.getString("nomepartecipante")));
                while(rs.next()){
                    if(team.getIdTeam() != rs.getInt("idteam"))
                        team = partecipante.seekTeam(rs.getInt("idteam"));
                    team.addRichiesta(team.getEventoIscritto().seekPartecipante(rs.getString("nomepartecipante")));
                }
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void addRichiesteTeamDB(ArrayList<Team> richiesteTeam) throws SQLException{
        if(richiesteTeam != null) {
            HashSet<Team> teams = new HashSet<Team>(richiesteTeam);
            String codiceSQL = "";
            for (Team team : teams) {
                Partecipante partecipante = team.firstRichiesta();
                while(partecipante != null) {
                    codiceSQL = codiceSQL + "CALL updateRichiestaTeam('" + partecipante.getNomeUtente() + "'," + team.getIdTeam() + "," + team.getRichiestaAnswer() + ");";
                    partecipante = team.nextRichiesta();
                }
            }
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public void getAllProgressiDB(Evento evento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT t.IdTeam, p.IdProgresso, p.DataPubblicazione," +
                                                "p.Testo AS TestoProgresso, c.NomeGiudice, c.Testo AS TestoCommento " +
                                                "FROM Team AS t JOIN Progresso AS p ON t.IdTeam = p.idTeam " +
                                                "JOIN Commento AS c ON p.IdProgresso = c.idProgresso " +
                                                "WHERE t.idEvento = " + evento.getIdEvento() +
                                                " ORDER BY t.IdTeam, p.IdProgresso;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()) {
                rs.next();
                ArrayList<Progresso> progressi = new ArrayList<Progresso>();
                ArrayList<Commento> commenti = new ArrayList<Commento>();
                Team team = evento.seekTeam(rs.getInt("idteam"));
                Progresso progresso = new Progresso(rs.getInt("idprogresso"), team.getIdTeam(), rs.getObject("datapubblicazione", LocalDate.class), rs.getString("testoprogresso"));
                commenti.add(new Commento(progresso.getIdProgresso(), rs.getString("testocommento"), rs.getString("nomegiudice")));
                while (rs.next()) {
                    if(progresso.getIdProgresso() != rs.getInt("idprogresso")) {
                        progresso.setCommenti(commenti);
                        progressi.add(progresso);
                        progresso = new Progresso(rs.getInt("idprogresso"), team.getIdTeam(), rs.getObject("datapubblicazione", LocalDate.class), rs.getString("testoprogresso"));
                    }
                    if(team.getIdTeam() != rs.getInt("idteam")) {
                        team.setProgressi(progressi);
                        team = evento.seekTeam(rs.getInt("idteam"));
                    }
                    commenti.add(new Commento(progresso.getIdProgresso(), rs.getString("testocommento"), rs.getString("nomegiudice")));
                }
                progresso.setCommenti(commenti);
                progressi.add(progresso);
                team.setProgressi(progressi);
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void getAllProgressiDB(Partecipante partecipante) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT ct.idTeam, p.IdProgresso, p.DataPubblicazione," +
                                                "p.Testo AS TestoProgresso, c.NomeGiudice, c.Testo AS TestoCommento " +
                                                "FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "JOIN Evento AS e ON t.idEvento = e.IdEvento " +
                                                "JOIN Progresso AS p ON ct.idTeam = p.idTeam " +
                                                "JOIN Commento AS c ON p.IdProgresso = c.idProgresso " +
                                                "WHERE e.DataFine >= NOW() AND ct.NomePartecipante = '"+ partecipante.getNomeUtente() +"' " +
                                                "ORDER BY ct.idTeam, p.IdProgresso;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()) {
                rs.next();
                ArrayList<Progresso> progressi = new ArrayList<Progresso>();
                ArrayList<Commento> commenti = new ArrayList<Commento>();
                Team team = partecipante.seekTeam(rs.getInt("idteam"));
                Progresso progresso = new Progresso(rs.getInt("idprogresso"), team.getIdTeam(), rs.getObject("datapubblicazione", LocalDate.class), rs.getString("testoprogresso"));
                commenti.add(new Commento(progresso.getIdProgresso(), rs.getString("testocommento"), rs.getString("nomegiudice")));
                while (rs.next()) {
                    if(progresso.getIdProgresso() != rs.getInt("idprogresso")) {
                        progresso.setCommenti(commenti);
                        progressi.add(progresso);
                        progresso = new Progresso(rs.getInt("idprogresso"), team.getIdTeam(), rs.getObject("datapubblicazione", LocalDate.class), rs.getString("testoprogresso"));
                    }
                    if(team.getIdTeam() != rs.getInt("idteam")) {
                        team.setProgressi(progressi);
                        team = partecipante.seekTeam(rs.getInt("idteam"));
                    }
                    commenti.add(new Commento(progresso.getIdProgresso(), rs.getString("testocommento"), rs.getString("nomegiudice")));
                }
                progresso.setCommenti(commenti);
                progressi.add(progresso);
                team.setProgressi(progressi);
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }


    public int getIdProgressoDB() throws SQLException{
        int id = -1;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT MAX(IdProgresso) FROM Progresso;");
            ResultSet rs = ps.executeQuery();
            rs.next();
            id = rs.getInt(1);
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
    }

    public void addProgressoDB(Progresso progresso) throws SQLException{
        try{
            progresso.setIdProgresso(addProgressoDB(progresso.getIdTeam(), progresso.getTestoDocumeto()));
        }
        catch(SQLException e){
            throw e;
        }
    }

    public int addProgressoDB(int idTeam, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Progresso(idTeam, Testo) VALUES("
                                                + idTeam + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdProgressoDB();
    }

    public ArrayList<Commento> getAllCommentiDB(String Giudice) throws SQLException{
        ArrayList<Commento> commenti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Commento WHERE NomeGiudice = " + Giudice + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Commento commento = new Commento(rs.getInt("idprogresso"), Giudice, rs.getString("testo"));
                if(commenti == null){
                    commenti = new ArrayList<Commento>();
                }
                commenti.add(commento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return commenti;
    }

    public void addCommentiDB(ArrayList<Commento> nuoviCommenti) throws SQLException{
        if(nuoviCommenti != null){
            HashSet<Commento> commenti = new HashSet<Commento>(nuoviCommenti);
            String codiceSQL = "INSERT INTO Commento(NomeGiudice, idProgresso, Testo) VALUES";
            for (Commento commento : commenti) {
                if(commento != nuoviCommenti.getFirst())
                    codiceSQL = codiceSQL + ",";
                codiceSQL = codiceSQL + "('" + commento.getGiudice() + "'," + commento.getIdProgresso() + ",'" + commento.getTesto() + "')";
            }
            codiceSQL = codiceSQL + ";";
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }

    public ArrayList<Voto> getAllVotiDB(String Giudice) throws SQLException{
        ArrayList<Voto> voti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Voto WHERE NomeGiudice = " + Giudice + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Voto voto = new Voto(rs.getInt("idteam"), rs.getInt("valore"), Giudice);
                if(voti == null)
                    voti = new ArrayList<Voto>();
                voti.add(voto);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return voti;
    }

    public void getAllVotiDB(Evento evento) throws SQLException{
        ArrayList<Voto> voti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t " +
                                                "JOIN Voto AS v ON t.IdTeam = v.idTeam " +
                                                "WHERE t.idEvento = " + evento.getIdEvento() +
                                                " ORDER BY t.idTeam;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                voti = new ArrayList<Voto>();
                Team team = evento.seekTeam(rs.getInt("idteam"));
                voti.add(new Voto(rs.getInt("idteam"), rs.getInt("valore"), rs.getString("nomegiudice")));
                while(rs.next()) {
                    if(team.getIdTeam() != rs.getInt("idteam")){
                        team.setVoti(voti);
                        voti = new ArrayList<Voto>();
                        team = evento.seekTeam(rs.getInt("idteam"));
                    }
                    voti.add(new Voto(rs.getInt("idteam"), rs.getInt("valore"), rs.getString("nomegiudice")));
                }
                team.setVoti(voti);
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void getAllVotiDB(Partecipante partecipante) throws SQLException{
        ArrayList<Voto> voti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT t.IdTeam, v.Valore, v.NomeGiudice FROM Team AS t " +
                                                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "JOIN Evento AS e ON t.idEvento = e.IdEvento " +
                                                "JOIN Voto AS v ON ct.idTeam = v.idTeam " +
                                                "WHERE e.DataFine >= NOW() " +
                                                "AND ct.NomePartecipante = '" + partecipante.getNomeUtente() + "' " +
                                                "ORDER BY t.IdTeam;");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                rs.next();
                voti = new ArrayList<Voto>();
                Team team = partecipante.seekTeam(rs.getInt("idteam"));
                voti.add(new Voto(rs.getInt("idteam"), rs.getInt("valore"), rs.getString("nomegiudice")));
                while(rs.next()) {
                    if(team.getIdTeam() != rs.getInt("idteam")){
                        team.setVoti(voti);
                        voti = new ArrayList<Voto>();
                        team = partecipante.seekTeam(rs.getInt("idteam"));
                    }
                    voti.add(new Voto(rs.getInt("idteam"), rs.getInt("valore"), rs.getString("nomegiudice")));
                }
                team.setVoti(voti);
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void addVotiDB(ArrayList<Voto> nuoviVoti) throws SQLException{
        if(nuoviVoti != null){
            HashSet<Voto> voti = new HashSet<Voto>(nuoviVoti);
            String codiceSQL = "INSERT INTO Voto(NomeGiudice, idTeam, Valore) VALUES";
            for (Voto voto : voti) {
                if(voto != nuoviVoti.getFirst())
                    codiceSQL = codiceSQL + ",";
                codiceSQL = codiceSQL + "('" + voto.getGiudice() + "'," + voto.getIdTeam() + "," + voto.getValore() + ")";
            }
            codiceSQL = codiceSQL + ";";
            try {
                PreparedStatement ps = connection.prepareStatement(codiceSQL);
                ps.executeUpdate();
            } catch (SQLException e) {
                throw e;
            }
        }
    }
}
