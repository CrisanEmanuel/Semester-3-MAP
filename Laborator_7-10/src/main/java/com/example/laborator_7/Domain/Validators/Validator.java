package com.example.laborator_7.Domain.Validators;

public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
