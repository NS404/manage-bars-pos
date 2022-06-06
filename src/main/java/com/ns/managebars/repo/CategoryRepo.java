package com.ns.managebars.repo;

import com.ns.managebars.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface CategoryRepo extends JpaRepository<Category, Long> {


    @Query(value = "select * from category c where c.actual = 1",nativeQuery = true)
    List<Category> getAllCategories();

    @Transactional
    @Modifying
    @Query("update Category c set c.name = :newName WHERE c = :category")
    void updateCategoryName(@Param("category") Category category,
                            @Param("newName") String newName);

    @Transactional
    @Modifying
    @Query(value = "Update category c set c.actual = false WHERE c.id = :id",nativeQuery = true)
    void removeCategory(@Param("id") long id);
}
