using Lab11.Domain;

namespace Facturi.domain.validators;

class FacturaValidator : IValidator<Factura>
{
    public void Validate(Factura e)
    {
        bool valid = true;
        if (valid == false)
        {
            throw new ValidationException("Obiectul nu e valid");
        }
    }
}