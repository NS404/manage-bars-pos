package com.ns.managebars.uiManager;

import com.ns.managebars.model.Order;
import com.ns.managebars.model.Shift;
import com.ns.managebars.model.User;
import com.ns.managebars.sevice.ShiftService;
import com.ns.managebars.uiManager.ObservablePattern.Observer;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderManager implements Observer {

    private final ShiftService shiftService;

    private final UserManager userManager;
    private Shift actualShift;
    private List<Shift> ongoingShifts;

    public OrderManager(ShiftService shiftService, UserManager userManager) {
        this.shiftService = shiftService;
        this.userManager = userManager;
        setOngoingShifts();
        userManager.registerObserver(this,0);
    }


    private void setOngoingShifts(){
        List<Shift> ongoingShifts = shiftService.queryOngoingShifts();
        for (Shift shift : ongoingShifts) {
            shift.calculateTotal();
        }
        this.ongoingShifts = ongoingShifts;
    }

    public void pushOrder(Order order){

        if(this.actualShift == null)
            startShift();

        order.setShift(actualShift);
        shiftService.addOrder(order);
        actualShift.addOrder(order);
    }

    public Shift getActualShift() {
        return actualShift;
    }

    private void startShift() {
        Shift shift = new Shift(userManager.getActualUser());
        shiftService.addShift(shift);
        this.actualShift = shift;
        this.ongoingShifts.add(shift);
    }

    public void endShift() {
        shiftService.endShift(this.actualShift);
        this.ongoingShifts.remove(actualShift);
        actualShift = null;
    }

    public Shift determineActualShift(User user){

        for (Shift shift : ongoingShifts) {
            if (shift.getUser().equals(user))
                return shift;
        }
        return null;
    }
    @Override
    public void update() {
        actualShift = determineActualShift(userManager.getActualUser());
        System.out.println(actualShift);
    }

    public void getOrderDetails(Order order) {
        order.setOrderList(shiftService.queryOrderDetails(order));
    }
}
