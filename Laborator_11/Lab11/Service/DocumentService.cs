using Lab11.Domain;
using Lab11.Repository;

namespace Lab11.Service;

public class DocumentService
{
    private readonly IRepository<string, Document> _documentRepository;

    public DocumentService(IRepository<string, Document> documentRepository)
    {
        _documentRepository = documentRepository;
    }

    public List<Document> GetAllDocuments()
    {
        return _documentRepository.FindAll().ToList();
    }

    public List<DocumentDataEmitereDTO> GetDocumentInAn(int year)
    {
        return _documentRepository.FindAll().Where(document => document.DataEmitere.Year == year)
            .Select(document => new DocumentDataEmitereDTO()
            {
                nume = document.Nume,
                DataEmitere = document.DataEmitere
            }).ToList();
    }
}