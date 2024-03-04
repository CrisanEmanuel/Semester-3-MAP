using Facturi.domain;

namespace Lab11.Domain;

public class Document : Entity<string>
{
    public string Nume { get; set; }

    public DateTime DataEmitere { get; set; }
    
    public override string ToString()
    {
        return Nume + " " + DataEmitere;
    }
}