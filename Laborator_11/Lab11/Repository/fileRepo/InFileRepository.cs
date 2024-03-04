using Lab11.Domain;

namespace Lab11.Repository.fileRepo;
    
public delegate TE CreateEntity<out TE>(string line);

public abstract class InFileRepository<TId, TE> : InMemoryRepository<TId, TE> where TE : Entity<TId>
{
    private readonly CreateEntity<TE> _createEntity;
    protected readonly string FileName;

    public InFileRepository(string fileName, CreateEntity<TE> createEntity)
    {
        FileName = fileName;
        _createEntity = createEntity;
        if (_createEntity != null)
            LoadFromFile();
    }

    protected void LoadFromFile()
    {
        var entities = DataReader.ReadData(FileName, _createEntity);
        entities.ForEach(entity => Entities[entity.Id] = entity);
    }
}