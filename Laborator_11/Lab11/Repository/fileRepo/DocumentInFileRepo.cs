using Lab11.Domain;

namespace Lab11.Repository.fileRepo;

public class DocumentInFileRepo : InFileRepository<string, Document>
{
    public DocumentInFileRepo(string fileName, CreateEntity<Document> createEntity) : base(fileName, createEntity)
    {
        LoadFromFile();
    }
}