package ClassModel;

public class Commento {

    private String Testo;
    private String Giudice;

    Commento(String Testo) {
    
        this.Testo = Testo;
    
    }

    public void setGiudice(String giudice) {Testo = giudice;}

    public String getTesto() {return Testo;}
    public String getGiudice() {return Giudice;}
}
