package com.indocyber.SoalOBS.repositories;

import com.indocyber.SoalOBS.entity.Item;
import com.indocyber.SoalOBS.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRespository extends JpaRepository<Order, String> {

    Optional<Order> findById(String orderNo);

    @Query("""
            SELECT o FROM Order o WHERE o.qty > :qty
            """)
    Page<Order> findByQty(@Param("qty") Integer qty,
                           Pageable pageable);

    @Query("""
            SELECT o FROM Order o
            """)
    Page<Order> getAllData(Pageable pageable);

    @Query(value = """
            SELECT order_no FROM orders ORDER BY created_at DESC LIMIT 1
            """, nativeQuery = true)
    String getlastData();
}
