package com.vue;

import com.atoudeft.serveur.Serveur;
import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.controleur.EcouteurMenuApplication;
import  com.controleur.EcouteurListeLivreurs;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * La classe principale de l'interface utilisateur de l'application MiniServer
 *
 */
public class MiniServerUI extends JFrame {
    final private Serveur serveur;
    final private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour l'interface graphique
     *
     * @param serveur La référence du serveur utilisé.
     * @param gestionnaireLivraisons  Le gestionnaire de livraisons.
     */
    public MiniServerUI(Serveur serveur, GestionnaireLivraisons gestionnaireLivraisons) {
        this.serveur = serveur;
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.initialiserComposants();
        this.configurerFenetrePrincipale();
    }

    /**
     * Getter pour le gestionnaire de livraisons.
     *
     * @return Le gestionnaire de livraisons.
     */
    public GestionnaireLivraisons getGestionnaireLivraisons() {
        return gestionnaireLivraisons;
    }

    /**
     * Configure la fenêtre graphique.
     *
     */
    private void configurerFenetrePrincipale() {
        // Configuration de la fenêtre
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setTitle("Gestionnaire de livraisons");
        this.setSize(980, 450);
        this.setLocation(100, 100);

        this.setVisible(true);
    }

    /**
     * Création et placement des composants de la fenêtre principale :
     *   - Création du menu Application est de ses items
     *   - Création des trois panneaux de la fenêtre
     */
    private void initialiserComposants() {
        // Le menu
        JMenuBar menuBar = new JMenuBar();
        JMenu menuApplication = new JMenu("Application");

        JMenuItem itemAjouterLivraison = new JMenuItem("Ajouter Livraison");
        itemAjouterLivraison.setActionCommand(EcouteurMenuApplication.CMD_AJOUTER_LIVRAISON);

        JMenuItem itemAfficherStatistiques = new JMenuItem("Afficher les statistiques");
        itemAfficherStatistiques.setActionCommand(EcouteurMenuApplication.CMD_AFFICHER_STATISTIQUES);

        JMenuItem itemQuitter = new JMenuItem("Quitter l'application");

        EcouteurMenuApplication ecouteurMenu = new EcouteurMenuApplication(this);
        itemAjouterLivraison.addActionListener(ecouteurMenu);
        itemAfficherStatistiques.addActionListener(ecouteurMenu);

        itemQuitter.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                quitter();
            }
        });

        menuApplication.add(itemAjouterLivraison);
        menuApplication.add(itemAfficherStatistiques);
        menuApplication.addSeparator();
        menuApplication.add(itemQuitter);
        menuBar.add(menuApplication);
        this.setJMenuBar(menuBar);

        PanneauLivreurs panneauLivreurs = new PanneauLivreurs(this, this.gestionnaireLivraisons);
        PanneauLivraisons panneauLivraisons = new PanneauLivraisons(this.gestionnaireLivraisons);
        PanneauConsole panneauConsole = new PanneauConsole(this.gestionnaireLivraisons);

        panneauLivreurs.enregisterEcouteur(new EcouteurListeLivreurs(panneauLivreurs));

        JSplitPane haut = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panneauLivreurs, panneauLivraisons);
        haut.setResizeWeight(0.5);
        haut.setBorder(null);

        JSplitPane principale = new JSplitPane(JSplitPane.VERTICAL_SPLIT, haut, panneauConsole);
        principale.setResizeWeight(0.66);
        principale.setBorder(null);

        this.setContentPane(principale);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                quitter();
            }
        });

    }

    /**
     * Quitter l'application :
     * - arrêter le serveur
     * - quitter le gestionnaire de livraisons
     * - libérer la fenêtre
     * - quitter l'application
     */
    public void quitter() {
        this.serveur.arreter();
        this.gestionnaireLivraisons.quitter();
        this.dispose();
        System.exit(0);
    }
}
