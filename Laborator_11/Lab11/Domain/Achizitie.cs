namespace Lab11.Domain;

public class Achizitie : Entity<string>
{
    public string Produs { get; set; }

    public int Cantitate { get; set; }

    public double PretProdus { get; set; }

    public Factura Factura { get; set; }
    
    public override string ToString()
    {
        return Produs + " " + Cantitate + " " + PretProdus + " " + Factura;
    }
}