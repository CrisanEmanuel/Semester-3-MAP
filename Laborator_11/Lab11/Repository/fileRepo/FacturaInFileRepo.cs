using Facturi.domain;
using Lab11.Domain;

namespace Lab11.Repository.fileRepo;

public class FacturaInFileRepo : InFileRepository<string, Factura>
{
    public FacturaInFileRepo(string fileName, CreateEntity<Factura> createEntity) : base(fileName, createEntity)
    {
        loadFromFile();
    }

    private new void loadFromFile()
    {
        var documente = DataReader.ReadData(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Documente.txt", EntityToFileMapping.CreateDocument);

        var achizitii = DataReader.ReadData(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Achizitii.txt", EntityToFileMapping.CreatePurchase);

        var facturi = DataReader.ReadData(FileName, EntityToFileMapping.CreateBill);


        facturi.ForEach(factura =>
        {
            factura.DataEmitere = documente.Find(document => document.Id == factura.Id)!.DataEmitere;
            factura.Nume = documente.Find(document => document.Id == factura.Id)!.Nume;
            factura.Achizitii = achizitii.FindAll(purchase => purchase.Factura.Id == factura.Id);
            Entities[factura.Id] = factura;
        });
    }
}