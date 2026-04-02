package com.demo;

import com.atoudeft.client.Client;
import com.atoudeft.commun.evenement.Arguments;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.gestionnaireLivreurs.GestionnaireLivreur;
import com.gestionnaireLivreurs.Message;

import java.util.Scanner;

/**
 * Programme simple de démonstration d'un client. Le programme utilise un client pour se connecter à un serveur
 * et lui envoie les textes saisis par l'utilisateur jusqu'à ce que celui-ci saisisse le texte EXIT.
 *
 * @author Abdelmoumène Toudeft (Abdelmoumene.Toudeft@etsmtl.ca)
 * @version 1.0
 * @since 2023-09-01
 */
public class Main {
    /**
     * Affiche la liste des commandes utilisables dans la console du livreur.
     *
     */
    private static void afficherListecommandes() {
        System.out.println("Liste des commandes disponibles :");
        System.out.println("  - HELP : afficher cette liste de commandes.");
        System.out.println("  - READY : se connecter au serveur.");
        System.out.println("  - REGISTER <id livreur> <type livreur> <nom livreur> : s'enregistrer auprès du serveur comme nouveau livreur.");
        System.out.println("  - ID <id livreur> : s'identifier auprès du serveur.");
        System.out.println("  - GET : demander des livraisons à effectuer.");
        System.out.println("  - DELIVERED <id livraison> : signifier que la livraison <id livraison> a été livrée.");
        System.out.println("  - FAILED <id livraison> : signifier que la livraison <id livraison> n'a pas pu être livrée.");
        System.out.println("  - INFO [<id livraison>] : demander des informations sur les livraisons en cours.");
        System.out.println("  - INCOME : demander le revenu des livraisons effectuées durant cette session.");
        System.out.println("  - SEND {<id livreur>|*} : envoyer un message à un livreur ou à tous les livreurs.");
        System.out.println("  - EXIT : quitter l'application.");
        System.out.println();
    }

    /**
     * Établit une connexion entre un gestionnaire de livreur passé en paramètre et le serveur spécifié dans la configuration.
     *
     * @param gl Le gestionnaire de livreur.
     * @return False si la connexion a échoué. True, sinon.
     */
    private static boolean etablirConnexion(GestionnaireLivreur gl) {
        Client client = gl.getClient();
        if (client.isConnecte()) {
            System.out.printf("Vous êtes déjà connecté à %s:%s.", client.getAdrServeur(), client.getPortServeur());
            return true;
        }

        if (!client.connecter(gl)) {
            System.out.println("Serveur introuvable a l'adresse " + client.getAdrServeur()
                    + " sur le port " + client.getPortServeur());
            return false;
        }

        return true;
    }

    /**
     * Envoie une commande au serveur
     *
     * @param gl Le gestionnaire de livreur qui envoie la commande.
     * @param cmd La commande à envoyer.
     */
    private static void envoyerCommande(GestionnaireLivreur gl, String cmd) {
        Client client = gl.getClient();
        if (!client.isConnecte()) {
            System.out.println("Vous devez vous connecter au serveur avant de lui envoyer des commandes.");
            return;
        }

        if (!cmd.trim().toUpperCase().startsWith("SEND")) {
            client.envoyer(cmd);
        }

        if (cmd.trim().toUpperCase().startsWith("SEND")) {
            if (!gl.estIdentifie()) {
                System.out.println("Vous devez vous identifier avant d'envoyer des messages.");
                return;
            }
            String msg = cmd.trim().substring(5);
            Message message = new Message(gl.getIdLivreur(), msg);
            gl.ajouterMessage(message);
        }
    }

    /**
     * Méthode principale du programme.
     *
     * @param args Arguments du programme
     */
    public static void main(String[] args) {
        Scanner clavier = new Scanner(System.in);
        Client client = new Client();
        GestionnaireLivreur gestionnaireLivreur = new GestionnaireLivreur(client);

        Main.afficherListecommandes();

        boolean fin = false;
        do {
            System.out.print("LIVREUR > ");
            String saisie = clavier.nextLine();

            switch (saisie.toUpperCase()) {
                case "HELP":
                    Main.afficherListecommandes();
                    break;
                case "READY":
                    fin = !Main.etablirConnexion(gestionnaireLivreur);
                    break;
                case "EXIT":
                    Main.envoyerCommande(gestionnaireLivreur, saisie);
                    fin = true;
                    break;
                default:
                    Main.envoyerCommande(gestionnaireLivreur, saisie);
            }
        } while (!fin);
    }
}
