namespace Lab11.Domain;

public class DocumentDataEmitereDTO
{
    public string nume { get; set; }
    public DateTime DataEmitere { get; set; }

    public override string ToString()
    {
        return $"Nume document: {nume}, Data emiterii: {DataEmitere}";
    }
}