package com.gestionnaireLivreurs;

import com.atoudeft.client.Client;
import com.atoudeft.commun.evenement.Arguments;
import com.atoudeft.commun.evenement.Evenement;
import com.atoudeft.commun.evenement.GestionnaireEvenement;
import com.atoudeft.commun.net.Connexion;

import java.util.Objects;

/**
 * La classe qui permet de gérer des évènements provenant du serveur (gestionnaire de livraisons).
 *
 */
public class GestionnaireLivreur implements GestionnaireEvenement {
    private static final int LIVREUR_NON_IDENTIFIE = -1;

    private final Client client;
    private int idLivreur;
    private GestionnaireMessages gestionnaireMessages;

    /**
     * Construit un nouveau gestionnaire côté client.
     *
     * @param client Le client associé à ce gestionnaire.
     */
    public GestionnaireLivreur(Client client) {
        this.client = client;
        this.idLivreur = LIVREUR_NON_IDENTIFIE;
    }

    public Client getClient() {
        return this.client;
    }

    public int getIdLivreur() {
        return idLivreur;
    }

    public boolean estIdentifie() {
        return this.idLivreur != LIVREUR_NON_IDENTIFIE;
    }

    public void ajouterMessage(Message msg) {
        this.gestionnaireMessages.ajouter(msg);
    }

    /**
     * Traitement d'un évènement CONNECTED provenant du serveur
     * Chaîne de caractères : CONNECTED
     *
     */
    private void traiterCONNECTED() {
        System.out.printf("Connexion acceptée par le serveur %s:%s.\n", client.getAdrServeur(), client.getPortServeur());
        this.gestionnaireMessages = new GestionnaireMessages(this.client, idLivreur);
        this.gestionnaireMessages.start();
    }

    /**
     * Traitement d'un évènement REGISTERED provenant du serveur
     * Chaîne de caractères : REGISTERED <id livreur> <nom livreur>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterREGISTERED(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            Integer idLivreur = Integer.parseInt(arguments.extraireArgumentSuivant());
            String nom = arguments.lire();

            if (!Objects.equals(nom, "")) {
                System.out.printf("Livreur %s enregistré avec l'id %d.\n", nom, idLivreur);
            } else {
                System.out.println("Trop d'arguments pour REGISTERED");
            }
        } catch (
                NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement AUTHORIZED provenant du serveur
     * Chaîne de caractères : AUTHORIZED <id livreur> <nom livreur>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterAUTHORIZED(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            this.idLivreur = Integer.parseInt(arguments.extraireArgumentSuivant());
            String nom = arguments.lire();

            if (!Objects.equals(nom, "")) {
                System.out.printf("Bonjour %s (livreur #%d).\n", nom, this.idLivreur);
            } else {
                System.out.println("Trop d'arguments pour AUTHORIZED");
            }
        } catch (
                NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement DELIVERIES provenant du serveur
     * Chaîne de caractères : DELIVERIES <nombre de livraisons> {<id livraison> <lot livraison> <priorité> <numéro de tentative>}+
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterDELIVERIES(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            int nb = Integer.parseInt(arguments.extraireArgumentSuivant());

            for (int i = 0; i < nb; i++) {
                Integer id = Integer.parseInt(arguments.extraireArgumentSuivant());
                Integer lot = Integer.parseInt(arguments.extraireArgumentSuivant());
                String priorite = arguments.extraireArgumentSuivant();
                Integer tentative = Integer.parseInt(arguments.extraireArgumentSuivant());

                System.out.printf("Livraison : #%d, lot %d, priorité %s, tentative %d\n", id, lot, priorite, tentative);
            }

            System.out.printf("Vous avez %d nouvelle(s) livraison(s) à effectuer...\n", nb);
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement DELIVERIES_INFO provenant du serveur
     * Chaîne de caractères : DELIVERIES_INFO <nombre de livraisons> {<id livraison> <lot livraison> <priorité> <numéro de tentative>}+
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterDELIVERIES_INFO(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            int nb = Integer.parseInt(arguments.extraireArgumentSuivant());

            if (nb == 0) {
                System.out.println("Vous n'avez pas de livraisons en cours.");
            } else {
                System.out.println("Voici vos livraisons :");
                for (int i = 0; i < nb; i++) {
                    Integer id = Integer.parseInt(arguments.extraireArgumentSuivant());
                    Integer lot = Integer.parseInt(arguments.extraireArgumentSuivant());
                    String priorite = arguments.extraireArgumentSuivant();
                    Integer tentative = Integer.parseInt(arguments.extraireArgumentSuivant());

                    System.out.printf("Livraison : #%d, lot %d, priorité %s, tentative %d\n", id, lot, priorite, tentative);
                }
            }
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement DELIVERED_OK provenant du serveur
     * Chaîne de caractères : DELIVERED_OK <id livraison>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterDELIVERED_OK(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            Integer id = Integer.parseInt(arguments.extraireArgumentSuivant());
            if (Objects.equals(arguments.lire(), "")) {
                System.out.printf("Livraison #%d acceptée. Merci.", id);
            } else {
                System.out.println("Trop d'arguments pour DELIVERED_OK");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement FAILED_CONTINUE provenant du serveur
     * Chaîne de caractères : FAILED_CONTINUE <id livraison>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterFAILED_CONTINUE(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            Integer id = Integer.parseInt(arguments.extraireArgumentSuivant());

            if (Objects.equals(arguments.lire(), "")) {
                System.out.printf("Livraison #%d retournée...\n", id);
            } else {
                System.out.println("Trop d'arguments pour FAILED_CONTINUE");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement FAILED_ABORT provenant du serveur
     * Chaîne de caractères : FAILED_ABORT <id livraison>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterFAILED_ABORT(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            Integer id = Integer.parseInt(arguments.extraireArgumentSuivant());

            if (Objects.equals(arguments.lire(), "")) {
                System.out.printf("La livraison #%d a atteint sa limite de tentatives...\n", id);

            } else {
                System.out.println("Trop d'arguments pour FAILED_ABORT");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement REVENU provenant du serveur
     * Chaîne de caractères : REVENU <montant> <nombre de livraisons>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterREVENU(Evenement evenement) {
        Arguments arguments = new Arguments(evenement);

        try {
            Double montant = Double.parseDouble(arguments.extraireArgumentSuivant());
            Integer nbLivraisons = Integer.parseInt(arguments.extraireArgumentSuivant());

            if (Objects.equals(arguments.lire(), "")) {
                System.out.printf("Vous avez effectué %d livraison(s) pour un montant total de %.2f$\n", nbLivraisons, montant);
            } else {
                System.out.println("Trop d'arguments pour FAILED_ABORT");
            }
        } catch (NumberFormatException nfe) {
            System.out.println("La réponse du serveur n'est pas correcte.");
        }
    }

    /**
     * Traitement d'un évènement ACK provenant du serveur.
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterACK(Evenement evenement) {
        System.out.println("Message bien reçu");
        this.gestionnaireMessages.traiterACK(evenement);
    }

    /**
     * Traitement d'un évènement MSG provenant du serveur.
     * Chaine de caracteres : MSG <id livreur> <message>
     *
     * @param evenement L'évènement à traiter.
     */
    private void traiterMSG(Evenement evenement) {
        Arguments args = new Arguments(evenement);

        int idLivreur = Integer.parseInt(args.extraireArgumentSuivant());
        String msg = args.lire();

        System.out.println("Message de #" + idLivreur + " : " + msg);
    }

    /**
     * Traite un évènement reçu du serveur.
     *
     * @param evenement L'événement a géré.
     */
    @Override
    public void traiter(Evenement evenement) {
        Object source = evenement.getSource();

        if (source instanceof Connexion) {
            System.out.println("CLIENT-Recu : " + evenement.getType() + " " + evenement.getArgument());

            switch (evenement.getType().toUpperCase()) {
                case "END":
                    client.deconnecter();
                    break;
                case "CONNECTED":
                    this.traiterCONNECTED();
                    break;
                case "REGISTERED":
                    this.traiterREGISTERED(evenement);
                    break;
                case "AUTHORIZED":
                    this.traiterAUTHORIZED(evenement);
                    break;
                case "DELIVERIES":
                    this.traiterDELIVERIES(evenement);
                    break;
                case "DELIVERIES_INFO":
                    this.traiterDELIVERIES_INFO(evenement);
                    break;
                case "DELIVERED_OK":
                    this.traiterDELIVERED_OK(evenement);
                    break;
                case "FAILED_CONTINUE":
                    this.traiterFAILED_CONTINUE(evenement);
                    break;
                case "FAILED_ABORT":
                    this.traiterFAILED_ABORT(evenement);
                    break;
                case "EMPTY":
                    System.out.println("Le serveur n'a plus de livraisons à effectuer.");
                    break;
                case "REVENU":
                    this.traiterREVENU(evenement);
                    break;
                case "ACK":
                    this.traiterACK(evenement);
                    break;
                case "MSG":
                    this.traiterMSG(evenement);
                    break;

                //  gestion des évènements d'erreur
                case "AUTHENTICATION_ERROR":
                    System.out.println("ERREUR : Le serveur n'a pas pu vous identifier.");
                    break;
                case "ALREADY_AUTHENTIFIED_ERROR":
                    System.out.println("Vous êtes déjà identifié auprès du serveur.");
                    break;
                case "TOO_MANY_CONNECTIONS_ERROR":
                    System.out.println("ERREUR : Ce livreur est déjà identifié sur un autre client.");
                    break;
                case "COMMAND_ERROR":
                    System.out.println("ERREUR : Le serveur n'a pas reconnu cette commande.");
                    break;
                case "BAD_ARGUMENT_ERROR":
                    System.out.println("ERREUR : Mauvais argument(s) pour cette commande.");
                    break;
                case "NO_DELIVERY_ERROR":
                    System.out.println("ERREUR : Vous n'avez pas de livraisons en cours.");
                    break;
                case "BAD_DELIVERY_ERROR":
                    System.out.println("ERREUR : Cette livraison ne fait pas partie des vos livraisons en cours.");
                    break;
                case "UNIMPLEMENTED_ERROR":
                    System.out.println("ERREUR : Cette commande est valide mais non implémentée sur le serveur...");
                    break;
                case "ALREADY_REGISTERED_ERROR":
                    System.out.println("ERREUR : Vous êtes déjà enregistré.");
                    break;
                default:
                    System.out.printf("ERREUR : Commande '%s(%s)' non reconnue par le client...\n", evenement.getType(), evenement.getArgument());
            }
        }
    }
}
