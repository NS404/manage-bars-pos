package com.ns.managebars.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private LocalDate date;
    private LocalTime time;
    private int total;
    @ManyToOne
    @JoinColumn(name = "shift_id")
    private Shift shift;

    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL)
    private List<Suborder> orderList;


    public Order(){
        this.orderList = new ArrayList<>();
    }

    public void calculateTotal(int subTotal){
        total = total + subTotal;

    }

    public void addSuborder(Suborder suborder){
        if(orderList.contains(suborder)){
            int i = orderList.indexOf(suborder);
            suborder = orderList.get(i);
            suborder.incrementQuantity();
            orderList.set(i,suborder);
            calculateTotal(suborder.getProduct().getPrice());
        }else  {
            orderList.add(suborder);
            calculateTotal(suborder.getSubtotal());
            suborder.setNo(orderList.size());
            suborder.setOrder(this);
        }
    }

    public void removeSubOrder(Suborder suborder){
        orderList.remove(suborder);
        calculateTotal(-suborder.getSubtotal());

    }

    public void removeSubOrder(){
        Suborder suborder = orderList.get(orderList.size()-1);
        orderList.remove(suborder);
        calculateTotal(-suborder.getSubtotal());


    }

    public void updateSubTotal(){
        for (int i = 0; i < orderList.size(); i++) {
            Suborder suborder = orderList.get(i);
            suborder.calculateSubTotal();
            orderList.set(i,suborder);
        }

    }

    public ObservableList<Suborder> toObservable(){
        orderList = FXCollections.observableArrayList(orderList);
        return (ObservableList<Suborder>) orderList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return id == order.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
