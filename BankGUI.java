import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class BankGUI extends JFrame {
    private GestionDeComptes gestion = new GestionDeComptes();
    private DefaultListModel<String> listModel = new DefaultListModel<>();
    private JList<String> compteList;

    public BankGUI() {
        setTitle("Gestion Bancaire");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLocationRelativeTo(null);

        // Style moderne
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Layout principal
        setLayout(new BorderLayout(10, 10));
        add(createMenuPanel(), BorderLayout.WEST);
        add(createMainPanel(), BorderLayout.CENTER);
    }

    private JPanel createMenuPanel() {
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(new Color(44, 62, 80));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        menuPanel.setPreferredSize(new Dimension(250, getHeight()));

        JLabel titleLabel = new JLabel("Menu Bancaire");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuPanel.add(titleLabel);
        menuPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        String[] buttonLabels = {
                "Nouveau Compte", "Créditer", "Débiter", "Virement", "Trier Comptes"
        };

        for (String label : buttonLabels) {
            menuPanel.add(createStyledButton(label));
            menuPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        return menuPanel;
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(200, 40));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFont(new Font("Arial", Font.PLAIN, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 73, 94));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(52, 152, 219));
            }

            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(52, 73, 94));
            }
        });

        button.addActionListener(e -> handleButtonClick(text));

        return button;
    }

    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel titleLabel = new JLabel("Liste des Comptes");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        compteList = new JList<>(listModel);
        compteList.setFont(new Font("Arial", Font.PLAIN, 14));
        compteList.setBorder(BorderFactory.createLineBorder(new Color(224, 224, 224)));

        JScrollPane scrollPane = new JScrollPane(compteList);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        return mainPanel;
    }

    private void handleButtonClick(String action) {
        switch (action) {
            case "Nouveau Compte": showNewAccountDialog(); break;
            case "Créditer": showCreditDialog(); break;
            case "Débiter": showDebitDialog(); break;
            case "Virement": showTransferDialog(); break;
            case "Trier Comptes": trierComptes(); break;
        }
    }

    private void showNewAccountDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        String[] types = {"Compte Courant", "Compte Épargne"};
        JComboBox<String> typeCombo = new JComboBox<>(types);
        JTextField nomField = new JTextField();
        JTextField valueField = new JTextField();

        typeCombo.addActionListener(e -> {
            boolean isCompteCourant = typeCombo.getSelectedItem().equals("Compte Courant");
            panel.getComponent(5).setName(isCompteCourant ? "Découvert autorisé:" : "Taux (%):");
        });

        panel.add(new JLabel("Type de compte:"));
        panel.add(typeCombo);
        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Découvert autorisé:"));
        panel.add(valueField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Nouveau Compte", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION && !nomField.getText().isEmpty() && !valueField.getText().isEmpty()) {
            try {
                double value = Double.parseDouble(valueField.getText().replace(",", "."));
                Compte compte;
                if (typeCombo.getSelectedItem().equals("Compte Courant")) {
                    compte = new CompteCourant(nomField.getText(), value);
                } else {
                    compte = new CompteEpargne(nomField.getText(), value / 100);
                }
                gestion.ajouterCompte(compte);
                updateCompteList();
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer une valeur numérique valide",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showCreditDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nomField = new JTextField();
        JTextField montantField = new JTextField();

        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Montant:"));
        panel.add(montantField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Créditer un compte", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Compte compte = gestion.trouverCompte(nomField.getText());
                if (compte != null) {
                    compte.crediter(Double.parseDouble(montantField.getText()));
                    updateCompteList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Compte non trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un montant valide",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showDebitDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField nomField = new JTextField();
        JTextField montantField = new JTextField();

        panel.add(new JLabel("Nom:"));
        panel.add(nomField);
        panel.add(new JLabel("Montant:"));
        panel.add(montantField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Débiter un compte", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Compte compte = gestion.trouverCompte(nomField.getText());
                if (compte != null) {
                    compte.debiter(Double.parseDouble(montantField.getText()));
                    updateCompteList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Compte non trouvé", "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un montant valide",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void showTransferDialog() {
        JPanel panel = new JPanel(new GridLayout(0, 2, 10, 10));
        JTextField sourceField = new JTextField();
        JTextField destField = new JTextField();
        JTextField montantField = new JTextField();

        panel.add(new JLabel("De:"));
        panel.add(sourceField);
        panel.add(new JLabel("Vers:"));
        panel.add(destField);
        panel.add(new JLabel("Montant:"));
        panel.add(montantField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Virement", JOptionPane.OK_CANCEL_OPTION);

        if (result == JOptionPane.OK_OPTION) {
            try {
                Compte source = gestion.trouverCompte(sourceField.getText());
                Compte dest = gestion.trouverCompte(destField.getText());
                if (source != null && dest != null) {
                    source.debiter(Double.parseDouble(montantField.getText()), dest);
                    updateCompteList();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Un des comptes n'a pas été trouvé",
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                        "Veuillez entrer un montant valide",
                        "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void trierComptes() {
        gestion.trierComptes();
        updateCompteList();
    }

    private void updateCompteList() {
        listModel.clear();
        for (Compte compte : gestion.getComptes()) {
            String type = compte instanceof CompteCourant ? "Compte Courant" : "Compte Épargne";
            String info = String.format("%s - %s | Solde: %.2f€",
                    type, compte.getProprietaire(), compte.soldeFinal());
            listModel.addElement(info);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new BankGUI().setVisible(true);
        });
    }
}