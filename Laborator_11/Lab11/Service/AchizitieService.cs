using Facturi.domain;
using Lab11.Domain;
using Lab11.Repository;

namespace Lab11.Service;

public class AchizitieService
{
    private readonly IRepository<string, Achizitie> _achizitieRepository;

    public AchizitieService(IRepository<string, Achizitie> achizitieRepository)
    {
        _achizitieRepository = achizitieRepository;
    }

    public List<Achizitie> GetAllPurchases()
    {
        return _achizitieRepository.FindAll().ToList();
    }

    public List<AchizitieFacturaDTO> GetAllAchizitiiInCategorie(string utilities)
    {
        return _achizitieRepository.FindAll().Where(purchase => purchase.Factura.Categorie == TipCategorie.Utilities)
            .Select(purchase => new AchizitieFacturaDTO()
            {
                produs = purchase.Produs,
                numeFactura = purchase.Factura.Nume
            }).ToList();
    }
}