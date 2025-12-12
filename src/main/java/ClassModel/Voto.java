package ClassModel;

public class Voto {

    private int idTeam;
    private int Valore;
    private String Giudice;

    Voto(int idTeam, int valore, String Giudice) {
        this.idTeam = idTeam;
        this.Valore = valore;
        this.Giudice = Giudice;
    }

    public void setValore(int Valore) {this.Valore = Valore;}
    public void setGiudice(String Giudice) {this.Giudice = Giudice;}

    public int getValore() {return this.Valore;}
    public String getGiudice() {return this.Giudice;}
    public int getIdTeam() {return this.idTeam;}

}