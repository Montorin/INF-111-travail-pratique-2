package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.gestionnaireLivraisons.Livraison;
import com.gestionnaireLivraisons.Priorite;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * La classe pour les boites de dialogues d'ajout d'une livraison.
 *
 */
public class AjoutLivraisonDialogue extends JDialog {
    final private MiniServerUI miniServerUI;
    final private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Le constructeur pour la boite de dialogue d'ajout d'une livrason.
     *
     * @param miniServerUI La fenêtre propriétaire de cette boite de dialogue.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public AjoutLivraisonDialogue(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        super(miniServerUI, "Ajout d'une livraison", true);
        this.miniServerUI = miniServerUI;
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();
    }

    /**
     * Affichage et gestion de la boite de dialogue pour l'ajout d'une livraison.
     *
     */
    public void initialiserComposants() {
        this.setLayout(new BorderLayout(10, 10));

        JPanel panneauForm = new JPanel(new GridLayout(2, 2, 8, 8));
        JLabel labelLot = new JLabel("Lot :");
        JTextField champLot = new JTextField();
        JLabel labelPriorite = new JLabel("Priorité :");
        JComboBox<Priorite> comboPriorite = new JComboBox<>(Priorite.values());

        panneauForm.add(labelLot);
        panneauForm.add(champLot);
        panneauForm.add(labelPriorite);
        panneauForm.add(comboPriorite);

        JPanel panneauBoutons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton boutonAjouter = new JButton("Ajouter");
        JButton boutonAnnuler = new JButton("Annuler");
        panneauBoutons.add(boutonAjouter);
        panneauBoutons.add(boutonAnnuler);

        boutonAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int lot = Integer.parseInt(champLot.getText().trim());
                    Priorite priorite = (Priorite) comboPriorite.getSelectedItem();
                    ajouterLivraison(lot, priorite);
                } catch (NumberFormatException ex) {
                    afficherErreur("Le numéro de lot doit être un entier.");
                }
            }
        });

        boutonAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        this.add(panneauForm, BorderLayout.CENTER);
        this.add(panneauBoutons, BorderLayout.SOUTH);

        this.pack();
        this.setResizable(false);
        this.setLocationRelativeTo(this.miniServerUI);
        this.setVisible(true);
    }

    /**
     * Affiche une boite de dialogue pour les erreurs de saisies.
     *
     * @param msg Le message à afficher dans la boite.
     */
    private void afficherErreur(String msg) {
        String[] erreurOptions = {"Opps..."};
        JOptionPane.showOptionDialog(this.miniServerUI, msg,
                "Erreur", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE,
                null, erreurOptions, erreurOptions[0]);
    }

    /**
     * Ajoute une livraison à la liste des livraisons à effectuer (mise à jour du modèle)
     *
     * @param lot      Le lot de la livraison.
     * @param priorite La priorité de la livraison.
     */
    private void ajouterLivraison(int lot, Priorite priorite) {
        int lotMaxPermis = Math.max(1, Livraison.lotMaximal() + 1);
        if (!Livraison.validerLotLivraison(lot) && !(Livraison.lotMaximal() < 0 && lot == 1)) {
            afficherErreur("Numéro de lot invalide. Valeurs permises : 1 à " + lotMaxPermis + ".");
            return;
        }

        Livraison livraison = new Livraison(priorite, lot);
        this.gestionnaireLivraisons.getLivraisonsAEffectuer().ajouter(livraison);
        this.gestionnaireLivraisons.notifierObservateurs();

        JOptionPane.showMessageDialog(
                this.miniServerUI,
                "Livraison ajoutée avec succès.",
                "Confirmation",
                JOptionPane.INFORMATION_MESSAGE
        );
        this.dispose();
    }

}
