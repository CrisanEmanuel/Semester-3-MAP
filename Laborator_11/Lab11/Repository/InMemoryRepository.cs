using Lab11.Domain;
namespace Lab11.Repository;

public class InMemoryRepository<TId, TE> : IRepository<TId, TE> where TE : Entity<TId> where TId : notnull
{
    protected readonly IDictionary<TId, TE> Entities = new Dictionary<TId, TE>();

    public IEnumerable<TE> FindAll()
    {
        return Entities.Values.ToList();
    }
}