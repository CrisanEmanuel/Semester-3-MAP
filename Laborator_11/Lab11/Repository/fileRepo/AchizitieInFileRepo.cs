using Lab11.Domain;

namespace Lab11.Repository.fileRepo;

public class AchizitieInFileRepo : InFileRepository<string, Achizitie>
{
    public AchizitieInFileRepo(string fileName, CreateEntity<Achizitie> createEntity) : base(fileName, createEntity)
    {
        loadFromFile();
    }

    private void loadFromFile()
    {
        var documente = DataReader.ReadData(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Documente.txt", EntityToFileMapping.CreateDocument);

        var facturi = DataReader.ReadData(@"C:\Users\EMANUEL\Desktop\MAP\Lab11\Lab11\Facturi.txt", EntityToFileMapping.CreateBill);

        var achizitii = DataReader.ReadData(FileName, EntityToFileMapping.CreatePurchase);

        achizitii.ForEach(achizitie =>
        {
            achizitie.Factura = facturi.Find(bill => bill.Id == achizitie.Factura.Id)!;
            achizitie.Factura.Nume = documente.Find(document => document.Id == achizitie.Factura.Id)!.Nume;
            Entities[achizitie.Id] = achizitie;
        });
    }
}