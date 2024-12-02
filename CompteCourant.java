import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class CompteCourant extends Compte {
    private double decouvertAutorise;

    public CompteCourant(String proprietaire, double decouvertAutorise) {
        super(proprietaire);
        this.decouvertAutorise = decouvertAutorise;
    }

    @Override
    public double calculBenefice() {
        return 0;
    }

    @Override
    public void debiter(double montant) {
        if (calculSolde() - montant < -decouvertAutorise) {
            JOptionPane.showMessageDialog(null,
                    "Opération impossible : dépassement du découvert autorisé de " + decouvertAutorise + "€",
                    "Erreur de découvert",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            super.debiter(montant);
        }
    }

    @Override
    public void debiter(double montant, Compte compteACrediter) {
        if (calculSolde() - montant < -decouvertAutorise) {
            JOptionPane.showMessageDialog(null,
                    "Opération impossible : dépassement du découvert autorisé de " + decouvertAutorise + "€",
                    "Erreur de découvert",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            super.debiter(montant);
            compteACrediter.crediter(montant);
        }
    }
}