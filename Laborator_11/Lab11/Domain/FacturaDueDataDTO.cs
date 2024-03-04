namespace Lab11.Domain;

public class FacturaDueDateDTO
{
    public string nume { get; set; }
    public DateTime DataScadenta { get; set; }
    
    public override string ToString()
    {
        return $"Numele facturii: {nume}, Data Expirare: {DataScadenta}";
    }
}