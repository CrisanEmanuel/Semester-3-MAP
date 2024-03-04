using Lab11.Domain;

namespace Facturi.domain.validators;

class DocumentValidator : IValidator<Document>
{
    public void Validate(Document e)
    {
        bool valid = true;
        if (valid == false)
        {
            throw new ValidationException("Obiectul nu e valid");
        }
    }
}