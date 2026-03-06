package com.indocyber.SoalOBS.repositories;

import com.indocyber.SoalOBS.entity.Item;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer> {
    Optional<Item> findById(Integer id);

    @Query("""
            SELECT i FROM Item i WHERE i.price > :price
            """)
    Page<Item> findByPrice(@Param("price") Integer price,
                           Pageable pageable);

    @Query("""
            SELECT i FROM Item i
            """)
    Page<Item> getAllData(Pageable pageable);

}
