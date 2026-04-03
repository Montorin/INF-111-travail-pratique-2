package com.vue;

import com.gestionnaireLivraisons.*;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * Classe qui affiche la boite de dialogue pour les informations d'un livreur.
 *
 */
public class InfoLivreurDialogue extends JDialog implements Observateur {

    private Livreur livreur;

    private ComposantTable grilleLivraisonsEnCours;
    private ComposantTable grilleLivraisonsEffectuees;

    private final String[] nomsColonnes = {"Id", "Lot", "Priorité", "Tentatives"};

    /**
     * Le constructeur de cette boite de dialogue.
     *
     * @param miniServerUI           La fenêtre contenant cette boite de dialogue.
     * @param livreur                Le livreur dont on veut afficher les données (infos personnelles et livraisons).
     */
    public InfoLivreurDialogue(MiniServerUI miniServerUI, Livreur livreur) {
        super(miniServerUI, "Informations livreur", false);
        this.livreur = livreur;
        this.livreur.ajouterObservateur(this); // Question 2.5

        this.initialiserComposants();

        // Taille de la fenetre
        this.pack();
        this.setMinimumSize(new Dimension(800, 300));
        this.setLocationRelativeTo(miniServerUI);
        this.setVisible(true);
    }

    /**
     * Afficher la boite de dialogue contenant les infos du livreur.
     * Cette méthode est invoquée par le constructeur
     *
     */
    private void initialiserComposants() {
        this.setLayout(new BorderLayout(10, 10));

        // 1. Infos du livreur
        JPanel panneauInfos = new JPanel();
        panneauInfos.setLayout(new BoxLayout(panneauInfos, BoxLayout.Y_AXIS));
        panneauInfos.setBorder(new EmptyBorder(10, 10, 10, 10));

        String typeLivreur = "";
        if (this.livreur instanceof LivreurVelo)
            typeLivreur = "vélo";
        else if (this.livreur instanceof LivreurCamion)
            typeLivreur = "camion";
        else if (this.livreur instanceof LivreurVoiture)
            typeLivreur = "voiture";

        panneauInfos.add(Box.createVerticalGlue());
        panneauInfos.add(new JLabel("Id : " + this.livreur.getId()));
        panneauInfos.add(new JLabel("Nom : " + this.livreur.getNom()));
        panneauInfos.add(new JLabel("Type : " + typeLivreur));
        panneauInfos.add(new JLabel("Capacité : " + this.livreur.capaciteLivraison()));
        panneauInfos.add(Box.createVerticalGlue());

        this.add(panneauInfos, BorderLayout.WEST);

        // 2. Livraison en cours/effectuer
        JPanel panneauTables = new JPanel(new GridLayout(1, 2, 10, 0));

        this.grilleLivraisonsEnCours = new ComposantTable("Livraisons en cours", 300, 200, nomsColonnes);
        this.grilleLivraisonsEffectuees = new ComposantTable("Livraisons effectuées", 300, 200, nomsColonnes);

        panneauTables.add(this.grilleLivraisonsEnCours);
        panneauTables.add(this.grilleLivraisonsEffectuees);

        this.add(panneauTables, BorderLayout.CENTER);
        this.seMettreAJour(this.livreur);

        // 3. Bouton fermer
        JPanel panneauBouton = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton boutonFermer = new JButton("Fermer");

        boutonFermer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {dispose();}
        });

        panneauBouton.add(boutonFermer);
        this.add(panneauBouton, BorderLayout.SOUTH);
    }

    /**
     * Prépare les données à afficher pour une liste de livraisons.
     *
     * @param livraisons La liste de livraisons.
     * @return La matrice des données relatives à la liste de livraisons.
     */
    private Vector<Vector<String>> calculerDonnees(IListeLivraisons livraisons) {
        Vector<Vector<String>> matriceDonnees = new Vector<>();

        if (livraisons != null) {
            for (Livraison livraison : livraisons) {
                Vector<String> ligne = new Vector<>();
                ligne.add(String.valueOf(livraison.getId()));
                ligne.add(String.valueOf(livraison.getLot()));
                ligne.add(livraison.getPriorite().toString());
                ligne.add(String.valueOf(livraison.getTentative()));
                matriceDonnees.add(ligne);
            }
        }
        return matriceDonnees;
    }

    @Override
    public void seMettreAJour(Observable observable) {
        Vector<Vector<String>> donneesEnCours = calculerDonnees(this.livreur.getLivraisonsEnCours());
        Vector<Vector<String>> donneesEffectuees = calculerDonnees(this.livreur.getLivraisonsEffectuees());

        this.grilleLivraisonsEnCours.mettreAJour(donneesEnCours);
        this.grilleLivraisonsEffectuees.mettreAJour(donneesEffectuees);
    }
}
