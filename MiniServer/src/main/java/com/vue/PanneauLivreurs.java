package com.vue;

import com.gestionnaireLivraisons.*;
import com.controleur.EcouteurListeLivreurs;

import com.observer.Observateur;
import com.observer.Observable;
import java.util.Vector;

import javax.swing.*;
import java.awt.*;

/**
 * Classe de type JPanel pour lister les livreurs enregistrés.
 *
 *
 */
public class PanneauLivreurs extends JPanel implements Observateur {
    // private final JTable table;
    private ComposantTable tableLivreurs;

    final private String[] nomsColonnes = {"Id", "Nom", "Type", "Authentifié"};
    final private boolean[] donneesCentrees = new boolean[]{true, false, true, true};

    private MiniServerUI miniServerUI;
    private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour cette classe.
     *
     * @param miniServerUI                La fenêtre qui contient cette gruille.
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauLivreurs(MiniServerUI miniServerUI, GestionnaireLivraisons gestionnaireLivraisons) {
        this.miniServerUI = miniServerUI;
        this.gestionnaireLivraisons = gestionnaireLivraisons;

        // Question 2.1
        this.tableLivreurs = new ComposantTable("Liste des livreurs", 450, 250, this.nomsColonnes, this.donneesCentrees);
        this.setLayout(new BorderLayout());
        this.add(this.tableLivreurs, BorderLayout.CENTER);

        // Question 2.2
        this.gestionnaireLivraisons.ajouterObservateur(this);
    }

    /**
     * Getter pour l'attribut MiniServerUI de cette classe.
     *
     * @return La fenêtre graphique principale dans laquelle se trouve ce panneau.
     */
    public MiniServerUI getMiniServerUI() {
        return this.miniServerUI;
    }

    /**
     * Enregistrer un écouteur pour ce panneau de livreurs.
     *
     * @param ecouteurLL L'écouteur à ajouter.
     */
    public void enregisterEcouteur(EcouteurListeLivreurs ecouteurLL) {
        // this.tableLivreurs.enregistrerEcouteur(ecouteurLL); // TODO: TO FIX
    }

    /**
     * Retourne l'objet Livreur sélectionné dans la Table,
     * Null si aucun livreur n'est sélectionné
     *
     * @return Le livreur sélectionné dans cette table.
     */
    public Livreur livreurSelectionne() {
        int ligne = this.tableLivreurs.ligneSelectionnee();

        // Null si aucun livreur n'est sélectionné
        if (ligne <= -1) { return null; }

        // Retourne l'objet Livreur sélectionné dans la Table
        String idTexte = this.tableLivreurs.lireCase(ligne, 0);
        int id = Integer.parseInt(idTexte);
        return this.gestionnaireLivraisons.getLivreursEnregistres().rechercher(id);
    }

    @Override
    public void seMettreAJour(Observable observable) {
        // Préparer la structure de données pour la table
        Vector<Vector<String>> matriceDonnees = new Vector<>();

        for (Livreur livreur : this.gestionnaireLivraisons.getLivreursEnregistres()) {
            Vector<String> ligne = new Vector<>();

            // Id
            ligne.add(String.valueOf(livreur.getId()));

            // Nom
            ligne.add(livreur.getNom());

            // Type
            if (livreur instanceof LivreurVelo) {
                ligne.add("vélo");
            } else if (livreur instanceof LivreurCamion) {
                ligne.add("camion");
            } else if (livreur instanceof LivreurVoiture) {
                ligne.add("voiture");
            } else {
                ligne.add("inconnu");
            }

            // isAuthentifie
            if (livreur.isAuthentifie()) {
                ligne.add("✔");
            } else {
                ligne.add("✘");
            }

            matriceDonnees.add(ligne);
        }

        // Mettre à jour l'affichage visuel
        this.tableLivreurs.mettreAJour(matriceDonnees);
    }
}
