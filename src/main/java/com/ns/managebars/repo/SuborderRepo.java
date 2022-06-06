package com.ns.managebars.repo;

import com.ns.managebars.model.Order;
import com.ns.managebars.model.Suborder;
import com.ns.managebars.model.SuborderId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SuborderRepo extends JpaRepository<Suborder,SuborderId > {

    @Query("SELECT s FROM Suborder s WHERE s.order = :order")
    List<Suborder> getOrderDetails(@Param("order") Order order);

}
