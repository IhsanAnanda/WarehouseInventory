package com.indocyber.SoalOBS.repositories;

import com.indocyber.SoalOBS.entity.Inventory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    List<Inventory> findByItemId(Integer itemId);

    @Query("""
            SELECT inv FROM Inventory inv WHERE inv.type = :type
            """)
    Page<Inventory> findByType(@Param("type") String type,
                          Pageable pageable);

    @Query("""
            SELECT inv FROM Inventory inv
            """)
    Page<Inventory> getAllData(Pageable pageable);

    @Query(value = """
            SELECT
                SUM(
                    CASE
                        WHEN type = 'T' THEN qty
                        WHEN type = 'W' THEN -qty
                        ELSE 0
                    END
                ) AS remaining_stock
            FROM inventory WHERE item_id = :itemId
            """, nativeQuery = true)
    Integer getItemStock(@Param("itemId") Integer itemId);
}
