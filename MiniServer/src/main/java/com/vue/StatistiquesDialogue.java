package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import com.observer.Observateur;
import java.util.Observer;

/**
 * La classe des boites de dialogues pour l'affichage des statistiques.
 *
 */
public class StatistiquesDialogue extends JDialog implements Observateur {

    final private GestionnaireLivraisons gestionnaireLivraisons;
    private JPanel infos;   // regroupe les informations de statistiques à afficher


    private JLabel lTotal;
    private JLabel lEffectuees;
    private JLabel lEnCours;
    private JLabel lEchouees;

    /**
     * Le constructeur pour une boite de dialogue de statistiques.
     *
     * @param miniServerUI La fenêtre propriétaire de cette boite de dialogue.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public StatistiquesDialogue(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        super(miniServerUI, "Statistiques", true);
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();

        gestionnaireLivraisons.ajouterObservateur(this);

        updateStats();

        this.pack();
        this.setLocationRelativeTo(miniServerUI);
        this.setVisible(true);

    }


    /**
     * Prépare la boite de dialogue pour les statistiques
     */
    private void initialiserComposants() {

        this.setLayout(new BorderLayout());

        infos = new JPanel(new GridLayout(4, 1, 10, 10));

        lTotal = new JLabel();
        lEffectuees = new JLabel();
        lEnCours = new JLabel();
        lEchouees = new JLabel();

        infos.add(lTotal);
        infos.add(lEffectuees);
        infos.add(lEnCours);
        infos.add(lEchouees);

        this.add(infos, BorderLayout.CENTER);

    }


    private void updateStats(){
        int total = gestionnaireLivraisons.getLivraisonsAEffectuer().taille() + gestionnaireLivraisons.getLivraisonsEchouees().taille();
        int effectuees = gestionnaireLivraisons.getLivraisonsNombreEffectuees();
        int enCours = gestionnaireLivraisons.getLivraisonsNombreEnCours();
        int echouees = gestionnaireLivraisons.getLivraisonsEchouees().taille();

        lTotal.setText("Nombre total de livraison : " + total);
        lEffectuees.setText("Nombre de livraisons effectuées : " + effectuees);
        lEnCours.setText("Livraisons en cours : " + enCours);
        lEchouees.setText("Livraisons échouées : " + echouees);
    }

    @Override
    public void seMettreAJour(Observable observable) {
        updateStats();
    }
}
