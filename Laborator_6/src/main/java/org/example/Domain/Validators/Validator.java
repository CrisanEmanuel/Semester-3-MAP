package org.example.Domain.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
