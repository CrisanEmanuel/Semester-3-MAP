namespace Facturi.domain.validators;

interface IValidator<E>
{
    void Validate(E e);
}