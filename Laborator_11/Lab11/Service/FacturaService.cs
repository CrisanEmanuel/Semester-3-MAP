using Lab11.Domain;
using Lab11.Repository;

namespace Lab11.Service;

public class FacturaService
{
    private readonly IRepository<string, Factura> _facturaRepository;

    public FacturaService(IRepository<string, Factura> facturaRepository)
    {
        _facturaRepository = facturaRepository;
    }

    public List<Factura> GetAllFacturi()
    {
        return _facturaRepository.FindAll().ToList();
    }

    public List<FacturaDueDateDTO> GetFacturiLunaCurenta()
    {
        var data = DateTime.Now.Year;
        return _facturaRepository.FindAll().Where(bill =>
                bill.DataScadenta.Month == DateTime.Now.Month && bill.DataScadenta.Year == DateTime.Now.Year)
            .Select(bill => new FacturaDueDateDTO()
            {
                nume = bill.Nume,
                DataScadenta = bill.DataScadenta
            }).ToList();
    }

    public List<FacturaProduseDTO> GetFacturiCuMacar3Prod()
    {
        return _facturaRepository.FindAll().Where(bill =>
        {
            return bill.Achizitii.Sum(purchase => purchase.Cantitate) >= 3;
        }).Select(bill => new FacturaProduseDTO()
        {
            nume = bill.Nume,
            nrProd = bill.Achizitii.Sum(purchase => purchase.Cantitate)
        }).ToList();
    }

    public TipCategorie GetCategorieCuCmmBaniCheltuiti()
    {
        TipCategorie category = _facturaRepository.FindAll().GroupBy(bill => bill.Categorie)
            .Select(group => new
            {
                category = group.Key,
                totalAmount = group.Sum(bill => bill.Achizitii.Sum(purchase => purchase.PretProdus * purchase.Cantitate))
            }).OrderByDescending(group => group.totalAmount).First().category;
        return category;
    }
}