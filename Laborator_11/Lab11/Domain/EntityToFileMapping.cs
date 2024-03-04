using Facturi.domain;

namespace Lab11.Domain;

public class EntityToFileMapping
{
    public static Document CreateDocument(string line)
    {
        var fields = line.Split(",");
        Document document = new()
        {
            Id = fields[0],
            Nume = fields[1],
            DataEmitere = DateTime.ParseExact(fields[2].Trim(), "yyyy-MM-dd", null)
        };
        return document;
    }

    public static Factura CreateBill(string line)
    {
        var fields = line.Split(",");
        Factura bill = new()
        {
            Id = fields[0],
            DataScadenta = DateTime.ParseExact(fields[1].Trim(), "yyyy-MM-dd", null),
            Categorie = (TipCategorie)Enum.Parse(typeof(TipCategorie), fields[2])
        };
        return bill;
    }

    public static Achizitie CreatePurchase(string line)
    {
        var fields = line.Split(",");
        Achizitie purchase = new()
        {
            Id = fields[0],
            Produs = fields[1],
            Cantitate = int.Parse(fields[2]),
            PretProdus = double.Parse(fields[3]),
            Factura = new Factura() { Id = fields[4] }
        };
        return purchase;
    }
}