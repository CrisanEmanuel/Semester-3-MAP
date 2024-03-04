package com.example.laborator_7.Repository.Paging;

import com.example.laborator_7.Domain.Entity;
import com.example.laborator_7.Repository.Repository;

public interface PagingRepository <ID, E extends Entity<ID>>  extends Repository<ID, E> {
    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator
}
