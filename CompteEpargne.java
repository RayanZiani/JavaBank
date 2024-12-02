public class CompteEpargne extends Compte {
    private double tauxAbondement;

    public CompteEpargne(String proprietaire, double tauxAbondement) {
        super(proprietaire);
        this.tauxAbondement = tauxAbondement;
    }

    @Override
    public double calculBenefice() {
        return calculSolde() * tauxAbondement;
    }

    @Override
    public void information() {
        System.out.println("Résumé du compte épargne de " + proprietaire);
        System.out.println("*******************************************");
        System.out.println("Solde : " + String.format("%.2f", soldeFinal()));
        System.out.println("Taux d'abondement : " + String.format("%.1f %%", tauxAbondement * 100));
        System.out.println("Opérations :");
        for (Operation op : operations) {
            System.out.println(op);
        }
        System.out.println("*******************************************");
    }
}