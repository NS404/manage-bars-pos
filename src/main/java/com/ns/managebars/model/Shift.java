package com.ns.managebars.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class Shift {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate date;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private int total;
    private boolean finished;

    @OneToMany(fetch = FetchType.EAGER,mappedBy = "shift")
    private List<Order> orders;


    public Shift(User actualUser) {
        this.date = LocalDate.now();
        this.user = actualUser;
        this.total = 0;
        this.orders = new ArrayList<>();
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        this.total += order.getTotal();
    }


    public void calculateTotal() {
        for (Order o : this.orders) {
            this.total += o.getTotal();
        }
    }

    public ObservableList<Order> getObservableOrders(){
        orders = FXCollections.observableArrayList(orders);
        return (ObservableList<Order>) orders;
    }
}
