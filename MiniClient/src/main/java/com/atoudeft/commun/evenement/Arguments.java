package com.atoudeft.commun.evenement;

/**
 * La classe pour gérer les arguments d'un évènement.
 */
public class Arguments {
    private String arguments;

    /**
     * Constructeur pour ce type.
     *
     * @param evenement L'évènement duquel on veut extraire les arguments.
     */
    public Arguments(Evenement evenement) {
        this.arguments = evenement.getArgument().trim();
    }

    /**
     * Enlève et retourne le prochain argument.
     *
     * @return Le mot suivant dans la chaîne d'arguments.
     */
    public String extraireArgumentSuivant() {
        String resultat = null;

        if (!this.arguments.isEmpty()) {
            int i = this.arguments.indexOf(' ');
            if (i == -1) {
                resultat = this.arguments;
                this.arguments = "";
            } else {
                resultat = this.arguments.substring(0, i);
                this.arguments = this.arguments.substring(i).trim();
            }
        }

        return resultat;
    }

    /**
     * Retourne la chaîne des arguments restants.
     *
     * @return La chaîne de caractères restante.
     */
    public String lire() {
        return this.arguments;
    }

    /**
     * Méthode main de test.
     *
     * @param args D'éventuels arguments à passer au programme.
     */
    public static void main(String[] args) {
        Evenement evenement = new Evenement(null, "SEND", "SEND 15 Le texte à envoyer.");

        Arguments arguments = new Arguments(evenement);

        String cmd = arguments.extraireArgumentSuivant();
        String id = arguments.extraireArgumentSuivant();
        String str = arguments.lire();

        System.out.printf("CMD : %s\n", cmd);
        System.out.printf("ID : %s\n", id);
        System.out.printf("CHAÎNE : |%s|\n", str);
    }
}
