import java.util.ArrayList;
import java.util.List;

public abstract class Compte implements Comparable<Compte> {
    protected List<Operation> operations;
    protected String proprietaire;

    public Compte(String proprietaire) {
        this.proprietaire = proprietaire;
        this.operations = new ArrayList<>();
    }

    public void crediter(double montant) {
        operations.add(new Operation(montant, Mouvement.CREDIT));
    }

    public void debiter(double montant) {
        operations.add(new Operation(montant, Mouvement.DEBIT));
    }

    public void crediter(double montant, Compte compteADebiter) {
        this.crediter(montant);
        compteADebiter.debiter(montant);
    }

    public void debiter(double montant, Compte compteACrediter) {
        this.debiter(montant);
        compteACrediter.crediter(montant);
    }

    public double calculSolde() {
        return operations.stream()
                .mapToDouble(Operation::getMontant)
                .sum();
    }

    public abstract double calculBenefice();

    public double soldeFinal() {
        return calculSolde() + calculBenefice();
    }

    public void information() {
        System.out.println("Résumé du compte de " + proprietaire);
        System.out.println("*******************************************");
        System.out.println("Solde : " + String.format("%.2f", calculSolde()));
    }

    @Override
    public int compareTo(Compte autreCompte) {
        return Double.compare(this.soldeFinal(), autreCompte.soldeFinal());
    }

    public String getProprietaire() {
        return proprietaire;
    }
}
