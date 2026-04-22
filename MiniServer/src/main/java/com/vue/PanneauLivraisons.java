package com.vue;

import com.gestionnaireLivraisons.*;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Vector;

/**
 * La classe qui constitue la table des livraisons.
 *
 */
public class PanneauLivraisons extends JPanel {

    private ComposantTable tableLivraisons;

    final private String[] nomsColonnes = {"Id", "Lot", "Priorité", "Tentatives", "Statut"};
    final private boolean[] donneesCentrees = {true, true, true, true, true};

    private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour la classe PanneauLivraisons
     *
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauLivraisons(GestionnaireLivraisons gestionnaireLivraisons) {
        this.gestionnaireLivraisons = gestionnaireLivraisons;

        this.tableLivraisons = new ComposantTable("liste des livraisons", 450, 250, this.nomsColonnes, this.donneesCentrees);

        this.setLayout(new BorderLayout());
        this.add(tableLivraisons, BorderLayout.CENTER);

        this.mettreAJour();
    }

    public void mettreAJour() {
        this.tableLivraisons.mettreAJour(this.calculerDonnees());
    }

    private Vector<Vector<String>> calculerDonnees() {
        Vector<Vector<String>> donnees = new Vector<>();

        for(Livraison livraison : this.gestionnaireLivraisons.getLivraisonsAEffectuer()){
            donnees.add(this.creerLigne(livraison));
        }

        for(Livreur livreur : this.gestionnaireLivraisons.getLivreursEnregistres()){
            for (Livraison livraison : livreur.getLivraisonsEnCours()){
                donnees.add(this.creerLigne(livraison));
            }
            for (Livraison livraison : livreur.getLivraisonsEffectuees()){
                donnees.add(this.creerLigne(livraison));
            }
        }

        for(Livraison livraison : this.gestionnaireLivraisons.getLivraisonsAEffectuer()){
            donnees.add(this.creerLigne(livraison));
        }
        return donnees;
    }

    private Vector<String> creerLigne(Livraison livraison) {
        Vector<String> ligne = new Vector<>();
        ligne.add(String.valueOf(livraison.getId()));
        ligne.add(String.valueOf(livraison.getLot()));
        ligne.add(livraison.getPriorite().toString());
        ligne.add(String.valueOf(livraison.getTentative()));
        ligne.add(this.formaterStatut(livraison.getStatut()));
        return ligne;
    }

    private String formaterStatut(Statut statut) {
        return statut.name().replace("_", " ");
    }

}
