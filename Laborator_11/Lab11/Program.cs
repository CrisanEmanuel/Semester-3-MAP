using Facturi.domain;
using Lab11.Domain;
using Lab11.Repository;
using Lab11.Repository.fileRepo;
using Lab11.Service;
using Lab11.Ui;

namespace Lab11;

internal static class Program
{
    private static void Main()
    {
        IRepository<string, Factura> billFileRepository =
            new FacturaInFileRepo(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Facturi.txt", EntityToFileMapping.CreateBill);
        IRepository<string, Document> documentFileRepository =
            new DocumentInFileRepo(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Documente.txt", EntityToFileMapping.CreateDocument);
        IRepository<string, Achizitie> purchaseFileRepository =
            new AchizitieInFileRepo(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Achizitii.txt", EntityToFileMapping.CreatePurchase);


        var billService = new FacturaService(billFileRepository);

        var documentService = new DocumentService(documentFileRepository);

        var purchaseService = new AchizitieService(purchaseFileRepository);


        var ui = new Ui.Ui(documentService, billService, purchaseService);

        ui.RunUi();
    }
}