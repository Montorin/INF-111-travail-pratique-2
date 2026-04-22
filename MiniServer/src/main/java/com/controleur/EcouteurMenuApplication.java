package com.controleur;


import com.vue.AjoutLivraisonDialogue;
import com.vue.MiniServerUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Ecouteur pour les items "Ajouter Livraison" et "Statistiques" du Menu Application de la barre de menu
 *
 */
public class EcouteurMenuApplication implements ActionListener {


    public static final String CMD_AJOUTER_LIVRAISON = "AJOUTER_LIVRAISON";
    public static final String CMD_AFFICHER_STATISTIQUES = "AFFICHER_STATISTIQUES";

    final private MiniServerUI miniServerUI;

    public EcouteurMenuApplication(MiniServerUI miniServerUI) {
        this.miniServerUI = miniServerUI;
    }

    public void actionPerformed(ActionEvent e) {
        String cmd = e.getActionCommand();

        if(CMD_AJOUTER_LIVRAISON.equals(cmd)) {
            new AjoutLivraisonDialogue(this.miniServerUI, this.miniServerUI.getGestionnaireLivraisons());
        } else if(CMD_AFFICHER_STATISTIQUES.equals(cmd)) {
            new AjoutLivraisonDialogue(this.miniServerUI, this.miniServerUI.getGestionnaireLivraisons());
        }
    }
}
