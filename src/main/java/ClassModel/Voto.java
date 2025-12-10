package ClassModel;

public class Voto {

    private int Valore;
    private String Giudice;

    public void setValore(int Valore) {this.Valore = Valore;}
    public void setGiudice(String Giudice) {this.Giudice = Giudice;}

    public int getValore() {return this.Valore;}
    public String getGiudice() {return this.Giudice;}

    Voto(int valore) {

        this.Valore = valore;}

}