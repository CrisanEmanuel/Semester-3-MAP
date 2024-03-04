namespace Lab11.Domain;

public class FacturaProduseDTO
{
    public string nume { get; set; }
    public int nrProd { get; set; }

    public override string ToString()
    {
        return $"Numele facturii: {nume}, Nr de produse: {nrProd}";
    }
}