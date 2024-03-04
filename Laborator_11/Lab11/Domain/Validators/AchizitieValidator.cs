using Lab11.Domain;

namespace Facturi.domain.validators;

class AchizitieValidator : IValidator<Achizitie>
{
    public void Validate(Achizitie e)
    {
        bool valid = true;
        if (valid == false)
        {
            throw new ValidationException("Obiectul nu e valid");
        }
    }
}