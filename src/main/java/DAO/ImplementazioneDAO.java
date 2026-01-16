package DAO;

import ClassModel.*;
import Database.ConnessioneDatabase;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

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

    public boolean checkRegisteredDB(String NomeUtente) throws SQLException{
        boolean success = true;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT NomeUtente, Password FROM Partecipante" +
                            " WHERE NomeUtente = '" + NomeUtente + "';");
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

    public ArrayList<Utente> getAllRuoliDB(Utente utente) throws SQLException{
        ArrayList<Utente> ruoli = null;
        String NomeUtente = utente.getNomeUtente();
        Partecipante partecipante = getPartecipanteDB(NomeUtente);
        Organizzatore organizzatore = getOrganizzatoreDB(NomeUtente);
        Giudice giudice = getGiudiceDB(NomeUtente);
        if(partecipante != null || organizzatore != null || giudice != null) {
            ruoli = new ArrayList<Utente>();
            if (partecipante != null) ruoli.add(partecipante);
            if (organizzatore != null) ruoli.add(organizzatore);
            if (giudice != null) ruoli.add(giudice);
        }
        return ruoli;
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

    public Evento getDatiEventoDB(int IdEvento) throws SQLException{
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
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return evento;
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

    public Evento getEventoDB(int IdEvento, Organizzatore organizzatore) throws SQLException{
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
                evento.setPartecipanti(getAllPartecipantiDB(evento));
                evento.setOrganizzatore(organizzatore);
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.setTeamIscritti(getAllTeamDB(evento));
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return evento;
    }

    public Evento getEventoDB(int IdEvento, Giudice giudice) throws SQLException{
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
                evento.setPartecipanti(getAllPartecipantiDB(evento));
                evento.setOrganizzatore(getOrganizzatoreDB(evento));
                evento.setGiudici(getAllGiudiciDB(evento));
                evento.seekAndRemoveGiudice(giudice.getNomeUtente());
                evento.addGiudice(giudice);
                evento.setTeamIscritti(getAllTeamDB(evento));
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return evento;
    }

    public ArrayList<Evento> getAllEventiDB(Organizzatore organizzatore) throws SQLException{
        ArrayList<Evento> eventi = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Evento AS e " +
                                                "JOIN OrganizzatoreEvento AS oe ON e.IdEvento = oe.idEvento " +
                                                "WHERE oe.NomeOrganizzatore = '"+ organizzatore.getNomeUtente() +"';");
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
                    evento.setOrganizzatore(organizzatore);
                    evento.setGiudici(getAllGiudiciDB(evento));
                    evento.setTeamIscritti(getAllTeamDB(evento));
                    getAllPartecipantiSingoliDB(evento);
                    getAllInvitiGiudiceDB(evento);
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

    public ArrayList<Evento> getEventiApertiDB() throws SQLException{
        ArrayList<Evento> eventi = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Evento WHERE DataInizioReg <= NOW() AND DataFineReg >= NOW();");
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

    public int addEventoDB(String Titolo, String Indirizzo, int NCivico) throws SQLException{
        int id;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Evento(Titolo, IndirizzoSede, NCivicoSede) VALUES('"
                            + Titolo + "','" + Indirizzo + "'," + NCivico + ");");
            ps.executeUpdate();
            id = getIdEventoDB();
        }
        catch (SQLException e) {
            throw e;
        }
        return id;
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

    public void addAllEventiDB(ArrayList<Evento> eventi) throws SQLException {
        for(Evento currentEvento : eventi){
            try {
                addEventoDB(currentEvento);
            }
            catch (SQLException e) {
                throw e;
            }
        }
    }

    public void updateEventoDB(Evento evento) throws SQLException{
        try {
            updateEventoDB(evento.getIdEvento(), evento.getIndirizzoSede(), evento.getNCivicoSede(), evento.getMaxIscritti(), evento.getMaxTeam());
        }
        catch (SQLException e) {
            throw e;
        }
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


    public void updateEventoDB(int IdEvento, LocalDate DataInizio, LocalDate DataFine, int MaxIscritti, int MaxTeam, LocalDate DataInizioReg, LocalDate DataFineReg, String DescrizioneProb) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("UPDATE Evento SET DataInizio = ?, DataFine = ?," +
                            " MaxIscritti = ?, MaxTeam = ?, DataInizioReg = ?, DataFineReg = ?, DescrizioneProb = ?" +
                            " WHERE IdEvento = " + IdEvento + ";");
            ps.setObject(1, DataInizio);
            ps.setObject(2, DataFine);
            ps.setInt(3, MaxIscritti);
            ps.setInt(4, MaxTeam);
            ps.setObject(5, DataInizioReg);
            ps.setObject(6, DataFineReg);
            ps.setString(7, DescrizioneProb);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void updateDateEventoDB(Evento evento) throws SQLException{
        try {
            updateDateEventoDB(evento.getIdEvento(), evento.getDataInizio(), evento.getDataFine());
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

    public void updateDateRegEventoDB(Evento evento) throws SQLException{
        try {
            updateDateRegEventoDB(evento.getIdEvento(), evento.getDataInizioReg(), evento.getDataFineReg());
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

    public void addUtenteDB(Utente utente) throws SQLException{
        try {
            addUtenteDB(utente.getNomeUtente(), utente.getPasswordUtente(), utente.getFNome(), utente.getMNome(), utente.getLNome(), utente.getDataNascita());
        }
        catch (SQLException e) {
            throw e;
        }
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

    public void addUtenteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password," +
                            " FNome, MNome, LNome, DataNascita" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
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

    public void addAllUtentiDB(ArrayList<Utente> utenti) throws SQLException{
        for(Utente currentUtente : utenti){
            try {
                addUtenteDB(currentUtente);
            }
            catch (SQLException e) {
                throw e;
            }
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

    public Partecipante getPartecipanteDB(String NomePartecipante) throws SQLException{
        Partecipante partecipante = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante AS p " +
                                                "JOIN PartecipanteEvento AS pe ON p.NomeUtente = pe.NomePartecipante " +
                                                "JOIN Evento AS e ON pe.idEvento = e.IdEvento " +
                                                "WHERE p.NomeUtente = '" + NomePartecipante + "';");
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

    public Partecipante getPartecipanteDB(String NomePartecipante, Evento evento) throws SQLException{
        Partecipante partecipante = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante" +
                            " WHERE NomeUtente = '" + NomePartecipante + "' AND idEvento = " + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                partecipante = new Partecipante();
                rs.next();
                partecipante.setNomeUtente(rs.getString("nomeutente"));
                partecipante.setPasswordUtente(rs.getString("password"));
                partecipante.setFNome(rs.getString("fnome"));
                partecipante.setMNome(rs.getString("mnome"));
                partecipante.setLNome(rs.getString("lnome"));
                partecipante.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
                partecipante.addEvento(evento);
                partecipante.setTeamUniti(getAllTeamDB(partecipante));
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return partecipante;
    }

    public Partecipante getPartecipanteDB(String NomePartecipante, Team team) throws SQLException{
        Partecipante partecipante = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante" +
                            " WHERE NomeUtente = '" + NomePartecipante + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                partecipante = new Partecipante();
                rs.next();
                partecipante.setNomeUtente(rs.getString("nomeutente"));
                partecipante.setPasswordUtente(rs.getString("password"));
                partecipante.setFNome(rs.getString("fnome"));
                partecipante.setMNome(rs.getString("mnome"));
                partecipante.setLNome(rs.getString("lnome"));
                partecipante.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                partecipante.addEvento(getEventoDB(rs.getInt("idevento")));
                while(rs.next()){
                    partecipante.addEvento(getEventoDB(rs.getInt("idevento")));
                }
                partecipante.addTeam(team);
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return partecipante;
    }

    public ArrayList<Partecipante> getAllPartecipantiDB(Evento evento) throws SQLException{
        ArrayList<Partecipante> partecipanti = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante" +
                            " WHERE idEvento = " + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                partecipanti = new ArrayList<Partecipante>();
                while(rs.next()) {
                    Partecipante partecipante = new Partecipante();
                    partecipante.setNomeUtente(rs.getString("nomeutente"));
                    partecipante.setPasswordUtente(rs.getString("password"));
                    partecipante.setFNome(rs.getString("fnome"));
                    partecipante.setMNome(rs.getString("mnome"));
                    partecipante.setLNome(rs.getString("lnome"));
                    partecipante.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                    partecipante.addEvento(evento);
                    partecipanti.add(partecipante);
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return partecipanti;
    }

    public void getAllPartecipantiSingoliDB(Evento evento) throws SQLException{
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Partecipante JOIN PartecipanteEvento ON NomeUtente = NomePartecipante " +
                                                "WHERE idEvento = " + idEvento + " AND NomeUtente NOT IN( " +
                                                "SELECT NomePartecipante FROM CompTeam WHERE idEvento = " + idEvento + ");");
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
                                                "SELECT NomePartecipante FROM CompTeam WHERE idEvento = " + idEvento + ");");
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
                        evento.addPartecipante(partecipante);
                    }
                }
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteDB(Partecipante partecipante, int idEvento) throws SQLException{
        try {
            addPartecipanteDB(partecipante.getNomeUtente(), partecipante.getPasswordUtente(), partecipante.getFNome(), partecipante.getMNome(), partecipante.getLNome(), partecipante.getDataNascita(), idEvento);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');CALL IscriviEvento('" + NomeUtente + "',"
                            + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addPartecipanteDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Partecipante(NomeUtente, Password," +
                            " FNome, MNome, LNome, DataNascita" +
                            " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?);");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.executeUpdate();
            ps = connection.prepareStatement("CALL IscriviEvento('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllPartecipanteDB(ArrayList<Partecipante> partecipanti, int idEvento) throws SQLException {
        for(Partecipante currentPartecipante : partecipanti){
            try {
                addPartecipanteDB(currentPartecipante, idEvento);
            }
            catch (SQLException e) {
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
                                                "WHERE o.NomeUtente = '" + NomeUtente + "';");
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

    public void addOrganizzatoreDB(Organizzatore organizzatore, Evento evento) throws SQLException{
        try {
            if(organizzatore.sizeEventi() > 1)
                addOrganizzatoreEventoDB(organizzatore.getNomeUtente(), evento.getIdEvento());
            else
                addOrganizzatoreDB(organizzatore.getNomeUtente(), organizzatore.getPasswordUtente(), organizzatore.getFNome(), organizzatore.getMNome(), organizzatore.getLNome(), organizzatore.getDataNascita(), evento.getIdEvento());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, idEvento) VALUES('"
                            + NomeUtente + "','" + Password + "'," + idEvento + " );");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Organizzatore(NomeUtente, Password, FNome, MNome, LNome, DataNascita, idEvento)" +
                            "VALUES(?, ?, ?, ?, ?, ?, ?); " +
                            "INSERT INTO OrganizzatoreEvento(NomeOrganizzatore, idEvento) " +
                            "VALUES('" +  NomeUtente + "'," + idEvento + ");");
            ps.setString(1, NomeUtente);
            ps.setString(2, Password);
            ps.setString(3, FNome);
            ps.setString(4, MNome);
            ps.setString(5, LNome);
            ps.setObject(6, DataNascita);
            ps.setObject(7, idEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addOrganizzatoreEventoDB(String NomeUtente, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO OrganizzatoreEvento(NomeOrganizzatore, idEvento) " +
                                                "VALUES('" +  NomeUtente + "'," + idEvento + ");");
            ps.setString(1, NomeUtente);
            ps.setObject(2, idEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllOrganizzatoriDB(ArrayList<Organizzatore> organizzatori) throws SQLException{
        for(Organizzatore currentOrganizzatore : organizzatori){
            try {
                addOrganizzatoreDB(currentOrganizzatore, currentOrganizzatore.getEvento());
            }
            catch (SQLException e) {
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
                                                "WHERE g.NomeUtente = '" + NomeUtente + "';");
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

    public Giudice getGiudiceDB(String NomeUtente, Evento evento) throws SQLException {
        Giudice giudice = null;
        try {
            int idEvento = evento.getIdEvento();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Giudice JOIN GiudiceEvento ON NomeUtente = NomeGiudice " +
                            "WHERE NomeUtente = '" + NomeUtente + "' AND idEvento =" + idEvento + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                giudice = new Giudice();
                rs.next();
                giudice.setNomeUtente(rs.getString("nomeutente"));
                giudice.setPasswordUtente(rs.getString("password"));
                giudice.setFNome(rs.getString("fNome"));
                giudice.setMNome(rs.getString("mNome"));
                giudice.setLNome(rs.getString("lNome"));
                giudice.setDataNascita(rs.getObject("datanascita", LocalDate.class));
                rs.close();
                giudice.addEvento(evento);
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

    public void addGiudiceDB(Giudice giudice) throws SQLException{
        try {
            addGiudiceDB(giudice.getNomeUtente(), giudice.getPasswordUtente(), giudice.getFNome(), giudice.getMNome(), giudice.getLNome(), giudice.getDataNascita(), giudice.getEvento().getIdEvento());
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addGiudiceDB(String NomeUtente, String Password, int idEvento) throws SQLException{
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Giudice(NomeUtente, Password) VALUES('"
                            + NomeUtente + "','" + Password + "');INSERT INTO GiudiceEvento(NomeGiudice, idEvento)" +
                            " VALUES('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addGiudiceDB(String NomeUtente, String Password, String FNome, String MNome, String LNome, LocalDate DataNascita, int idEvento) throws SQLException{
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
            ps.setObject(7, idEvento);
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllGiudiciDB(ArrayList<Giudice> giudici) throws SQLException{
        for(Giudice currentGiudice : giudici){
            try {
                addGiudiceDB(currentGiudice);
            }
            catch (SQLException e) {
                throw e;
            }
        }
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

    public Team getTeamDB(int IdTeam) throws SQLException{
        Team team = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                            "WHERE t.IdTeam = " + IdTeam + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                team = new Team(IdTeam);
                rs.next();
                Evento evento = getEventoDB(rs.getInt("idevento"));
                Partecipante part = getPartecipanteDB(rs.getString("nomepartecipante"), evento);
                team.addMembroTeam(part);
                while(rs.next()) {
                    part = getPartecipanteDB(rs.getString("nomepartecipante"), evento);
                    team.addMembroTeam(part);
                }
                rs.close();
                team.setEventoIscritto(evento);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return team;
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
                Partecipante partecipante = new Partecipante(rs.getString("nomeutente"), rs.getString("password"));
                partecipante.setDati(rs.getString("fnome"), rs.getString("mnome"), rs.getString("lnome"), rs.getObject("datanascita", LocalDate.class));
                partecipante.addEvento(evento);
                partecipante.addTeam(team);
                team.addMembroTeam(partecipante);
                evento.addPartecipante(partecipante);
                while(rs.next()) {
                    if(idTeam != rs.getInt("idteam")) {
                        team.setEventoIscritto(evento);
                        teams.add(team);
                        idTeam = rs.getInt("idteam");
                        team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
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

    public ArrayList<Team> getAllTeamDB(Partecipante partecipante) throws SQLException{
        ArrayList<Team> teams = null;
        try {
            String nomeUtente = partecipante.getNomeUtente();
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Team AS t JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                            "WHERE ct.NomePartecipante <> '" + nomeUtente + "' AND " +
                            "ct.idTeam IN(SELECT idTeam FROM CompTeam WHERE NomePartecipante = '" + nomeUtente + "');");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                teams = new ArrayList<Team>();
                rs.next();
                int idTeam = rs.getInt("idteam");
                Team team = new Team(idTeam);
                Partecipante newPartecipante = getPartecipanteDB(rs.getString("nomepartecipante"), team);
                team.addMembroTeam(partecipante);
                team.addMembroTeam(newPartecipante);
                while(rs.next()) {
                    if(idTeam != rs.getInt("idteam")) {
                        teams.add(team);
                        team = new Team(rs.getInt("idteam"));
                        team.addMembroTeam(partecipante);
                    }
                    newPartecipante = getPartecipanteDB(rs.getString("nomepartecipante"), team);
                    team.addMembroTeam(newPartecipante);
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
                        team.setEventoIscritto(evento);
                        teams.add(team);
                        idTeam = rs.getInt("idteam");
                        team = new Team(idTeam, rs.getString("nome"), rs.getString("teamleader"));
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

    public int addTeamDB(String NomeUtente, int idEvento) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL CreaTeam('" + NomeUtente + "'," + idEvento + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
        return getIdTeamDB();
    }

    public void addCompTeamDB(String NomeUtente, int idTeam) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("CALL JoinTeam('" + NomeUtente + "'," + idTeam + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void addAllCompTeamDB(ArrayList<Partecipante> partecipanti, int idTeam) throws SQLException {
        for(Partecipante currentPartecipante : partecipanti){
            try {
                addCompTeamDB(currentPartecipante.getNomeUtente(), idTeam);
            }
            catch (SQLException e) {
                throw e;
            }
        }
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

    public Progresso getProgressoDB(int IdProgresso) throws SQLException{
        Progresso progresso = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Progresso WHERE IdProgresso = " + IdProgresso + ";");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                progresso = new Progresso(IdProgresso);
                rs.next();
                progresso.setIdTeam(rs.getInt("idteam"));
                progresso.setDataProgresso(rs.getObject("datapubblicazione", LocalDate.class));
                progresso.setTestoDocumeto(rs.getString("testo"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return progresso;
    }

    public ArrayList<Progresso> getAllProgressiDB(int idTeam) throws SQLException{
        ArrayList<Progresso> progressi = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Progresso WHERE idTeam = " + idTeam + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Progresso progresso = new Progresso(rs.getInt("idprogresso"));
                progresso.setIdTeam(idTeam);
                progresso.setDataProgresso(rs.getObject("datapubblicazione", LocalDate.class));
                progresso.setTestoDocumeto(rs.getString("testo"));
                if(progressi == null){
                    progressi = new ArrayList<Progresso>();
                }
                progressi.add(progresso);
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return progressi;
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
                                                "FROM CompTeam AS ct JOIN Progresso AS p ON ct.idTeam = p.idTeam " +
                                                "JOIN Commento AS c ON p.IdProgresso = c.idProgresso " +
                                                "WHERE ct.NomePartecipante = '"+ partecipante.getNomeUtente() +"' " +
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

    public Commento getCommentoDB(int idProgresso, String Giudice) throws SQLException{
        Commento commento = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Commento WHERE idProgresso = " + idProgresso +
                                                "AND NomeGiudice = '" + Giudice + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                commento = new Commento(idProgresso, Giudice);
                rs.next();
                commento.setTesto(rs.getString("testo"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return commento;
    }

    public ArrayList<Commento> getAllCommentiDB(int idProgresso) throws SQLException{
        ArrayList<Commento> commenti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Commento WHERE idProgresso = " + idProgresso + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Commento commento = new Commento(idProgresso, rs.getString("nomegiudice"), rs.getString("testo"));
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

    public void addCommentoDB(Commento commento) throws SQLException{
        try{
            addCommentoDB(commento.getGiudice(), commento.getIdProgresso(), commento.getTesto());
        }
        catch(SQLException e){
            throw e;
        }
    }

    public void addCommentoDB(String NomeGiudice, int idProgresso, String testo) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Commento(NomeGiudice, idProgresso, Testo) VALUES('"
                            + NomeGiudice + "'," + idProgresso + ",'" + testo + "');");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public Voto getVotoDB(int idTeam, String Giudice) throws SQLException {
        Voto voto = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Voto WHERE idTeam = " + idTeam +
                            "AND NomeGiudice = '" + Giudice + "';");
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                voto = new Voto(idTeam, Giudice);
                rs.next();
                voto.setValore(rs.getInt("valore"));
                rs.close();
            }
        }
        catch(SQLException e) {
            throw e;
        }
        return voto;
    }

    public ArrayList<Voto> getAllVotiDB(int idTeam) throws SQLException{
        ArrayList<Voto> voti = null;
        try {
            PreparedStatement ps =
                    connection.prepareStatement("SELECT * FROM Voto WHERE idTeam = " + idTeam + ";");
            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Voto voto = new Voto(idTeam, rs.getInt("valore"), rs.getString("nomegiudice"));
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
                    connection.prepareStatement("SELECT * FROM Team AS t " +
                                                "JOIN CompTeam AS ct ON t.IdTeam = ct.idTeam " +
                                                "JOIN Voto AS v ON ct.idTeam = v.idTeam " +
                                                "WHERE ct.NomePartecipante = '" + partecipante.getNomeUtente() +"' " +
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


    public void addVotoDB(Voto voto) throws SQLException{
        try{
            addVotoDB(voto.getGiudice(), voto.getIdTeam(), voto.getValore());
        }
        catch(SQLException e){
            throw e;
        }
    }

    public void addVotoDB(String NomeGiudice, int idTeam, int valore) throws SQLException {
        try {
            PreparedStatement ps =
                    connection.prepareStatement("INSERT INTO Voto(NomeGiudice, idTeam, Valore) VALUES('"
                            + NomeGiudice + "'," + idTeam + "," + valore + ");");
            ps.executeUpdate();
        }
        catch (SQLException e) {
            throw e;
        }
    }
}
