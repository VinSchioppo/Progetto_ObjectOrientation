package classmodel;

public class Voto {

    private final int idTeam;
    private int valore;
    private String giudice;

    public Voto(int idTeam, String giudice){
        this.idTeam = idTeam;
        this.giudice = giudice;
    }

    public Voto(int idTeam, int valore, String giudice) {
        this.idTeam = idTeam;
        this.valore = valore;
        this.giudice = giudice;
    }

    public void setValore(int valore) {this.valore = valore;}
    public void setGiudice(String giudice) {this.giudice = giudice;}

    public int getValore() {return this.valore;}
    public String getGiudice() {return this.giudice;}
    public int getIdTeam() {return this.idTeam;}

}