package mg.core.scaffolding;

public class ScAnnotation {
    private String nom;
    private String valeur;
    private String importName;

    public ScAnnotation(String nom, String valeur, String importName) {
        setNom(nom);
        setValeur(valeur);
        setImportName(importName);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getValeur() {
        return valeur;
    }

    public void setValeur(String valeur) {
        this.valeur = valeur;
    }

    public String getImportName() {
        return importName;
    }

    public void setImportName(String importName) {
        this.importName = importName;
    }
}
