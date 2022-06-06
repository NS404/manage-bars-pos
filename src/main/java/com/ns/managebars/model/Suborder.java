package com.ns.managebars.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@IdClass(SuborderId.class)
public class Suborder {

    @Id
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;
    @Id
    private int no;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private int quantity;
    private int subtotal;



    public Suborder(Product product){
        this.product = product;
        this.quantity = 1;
        this.subtotal = product.getPrice();
    }


    public void incrementQuantity(){
        ++this.quantity;
        calculateSubTotal();
    }
    public void calculateSubTotal(){
        this.subtotal = this.quantity * product.getPrice();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Suborder suborder = (Suborder) o;
        return Objects.equals(product, suborder.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(product);
    }
}

