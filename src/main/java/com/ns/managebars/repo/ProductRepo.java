package com.ns.managebars.repo;

import com.ns.managebars.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Long> {

    @Query(value = "select * from product p where p.actual = 1 and p.category_id = ?",nativeQuery = true)
    List<Product> getProductsByCategory(long category_id);

    @Transactional
    @Modifying
    @Query(value = "Update product p set p.actual = false where p.id = :id",nativeQuery = true)
    void removeProduct(@Param("id") long id);

    @Transactional
    @Modifying
    @Query("update Product p set p.name = :newName where p = :product")
    void updateProductName(@Param("product") Product product,
                           @Param("newName") String newName);

    @Transactional
    @Modifying
    @Query("update Product p set p.price = :newPrice where p = :product")
    void updateProductPrice(@Param("product") Product product,
                            @Param("newPrice") int newPrice);
}
