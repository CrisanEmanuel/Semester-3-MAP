namespace Lab11.Domain;

public class AchizitieFacturaDTO
{
    public string produs { get; set; }
    public string numeFactura { get; set; }
    
    public override string ToString()
    {
        return $"Achizitie : Produs = {produs}, Nume factura = {numeFactura}";
    }
}