package com.ns.managebars.model;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
@Embeddable
public class SuborderId implements Serializable {
    private Long order;
    private int no;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuborderId that = (SuborderId) o;
        return no == that.no && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(order, no);
    }
}
