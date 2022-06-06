package com.ns.managebars.sevice;

import com.ns.managebars.model.Order;
import com.ns.managebars.model.Shift;
import com.ns.managebars.model.Suborder;
import com.ns.managebars.repo.OrderRepo;
import com.ns.managebars.repo.ShiftRepo;
import com.ns.managebars.repo.SuborderRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShiftService {

    private final OrderRepo orderRepo;
    private final SuborderRepo suborderRepo;
    private final ShiftRepo shiftRepo;

    public ShiftService(OrderRepo orderRepo,
                        SuborderRepo suborderRepo,
                        ShiftRepo shiftRepo) {
        this.orderRepo = orderRepo;
        this.suborderRepo = suborderRepo;
        this.shiftRepo = shiftRepo;
    }


    public void addShift(Shift shift) {
        shiftRepo.save(shift);
    }

    public void addOrder(Order order) {
        orderRepo.save(order);
    }

    public List<Shift> queryOngoingShifts() {
        return shiftRepo.getOngoingShifts();
    }

    public void endShift(Shift shift) {
        shiftRepo.updateFinished(shift);
    }

    public List<Suborder> queryOrderDetails(Order order) {
       return suborderRepo.getOrderDetails(order);
    }
}
