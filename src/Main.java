//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        Organizzatore a = new Organizzatore("mario", "rossi");
        System.out.println(a.NomeUtente + a.PasswordUtente);
        Team Lakers = a.CreaTeam();
        System.out.println("Team id: " + Lakers.idTeam + "\nNumMembers: " + Lakers.GiveNumMembri());
    }
}