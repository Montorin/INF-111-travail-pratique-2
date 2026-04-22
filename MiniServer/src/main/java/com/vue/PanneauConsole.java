package com.vue;

import com.gestionnaireLivraisons.GestionnaireLivraisons;
import com.observer.Observable;
import com.observer.Observateur;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

/**
 * La classe qui constitue la console dans l'interface graphique.
 *
 */
public class PanneauConsole extends JPanel implements Observateur
{

    private JTextArea console;
    private GestionnaireLivraisons gestionnaireLivraisons;

    /**
     * Constructeur pour cette classe.
     *
     * @param gestionnaireLivraisons Le gestionnaire de livraisons associé.
     */
    public PanneauConsole(GestionnaireLivraisons gestionnaireLivraisons) {
        this.gestionnaireLivraisons = gestionnaireLivraisons;
        this.gestionnaireLivraisons.ajouterObservateur(this);

        this.setLayout(new BorderLayout());

        this.console = new JTextArea(10, 40);
        this.console.setEditable(false);
        this.console.setBackground(Color.BLACK);
        this.console.setForeground(Color.GREEN);
        this.console.setFont(Config.fonte);

        JScrollPane scrollPane = new JScrollPane(this.console);
        this.add(scrollPane, BorderLayout.CENTER);
    }

    public void seMettreAJour(Observable observable) {
        String trace = this.gestionnaireLivraisons.consommerTrace();
        if (trace != null && !trace.isEmpty()) {
            this.console.append(trace + "\n");
            this.console.setCaretPosition(this.console.getDocument().getLength());
        }
    }

}
