using Lab11.Domain;

namespace Lab11.Repository;

public interface IRepository<TId, out TE> where TE : Entity<TId>
{
    IEnumerable<TE> FindAll();
}