namespace Lab11.Domain;

public enum TipCategorie { Utilities, Groceries, Fashion, Entertainment, Electronics }

public class Factura : Document
{
    public DateTime DataScadenta { get; set; }

    public List<Achizitie> Achizitii { get; set; }

    public TipCategorie Categorie { get; set; }
    
    public override string ToString()
    {
        return Id + " " + Nume + " "+ DataEmitere+" "+DataScadenta+ " " +Categorie;
    }
}