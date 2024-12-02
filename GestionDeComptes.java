import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GestionDeComptes {
    private List<Compte> comptes;

    public List<Compte> getComptes() {
        return comptes;
    }

    public GestionDeComptes() {
        this.comptes = new ArrayList<>();
    }

    public void ajouterCompte(Compte compte) {
        comptes.add(compte);
    }

    public void afficherComptes() {
        for (Compte compte : comptes) {
            compte.information();
        }
    }

    public void trierComptes() {
        Collections.sort(comptes);
    }

    public Compte trouverCompte(String proprietaire) {
        for (Compte compte : comptes) {
            if (compte.getProprietaire().equalsIgnoreCase(proprietaire)) {
                return compte;
            }
        }
        return null;
    }
}