package com.ns.managebars.viewControllers;

import com.ns.managebars.model.Order;
import com.ns.managebars.model.Suborder;
import com.ns.managebars.uiManager.OrderManager;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import net.rgielen.fxweaver.core.FxmlView;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Component;

@Component
@FxmlView("/views/shift-history-view.fxml")
public class ShiftHistoryController {

    @FXML
    private TableView<Order> ordersTable;
    @FXML
    private TableView<Suborder> orderDetailsTable;
    @FXML
    private Label totalLabel;
    private final OrderManager orderManager;

    public ShiftHistoryController(OrderManager orderManager) {
        this.orderManager = orderManager;
    }

    @FXML
    public void initialize(){
        if(orderManager.getActualShift() != null) {
            ordersTable.setItems(orderManager.getActualShift().getObservableOrders());
            totalLabel.setText((String.valueOf(orderManager.getActualShift().getTotal())));
        }
    }

    public void orderSelected() {
        SelectionModel<Order> selectionModel = ordersTable.getSelectionModel();

        if(!selectionModel.isEmpty()){
            Order selectedOrder = selectionModel.getSelectedItem();
            try {
                orderDetailsTable.setItems(FXCollections.observableArrayList(selectedOrder.getOrderList()));
            }catch (LazyInitializationException e) {
                orderManager.getOrderDetails(selectedOrder);
                orderDetailsTable.setItems(FXCollections.observableArrayList(selectedOrder.getOrderList()));

            }
        }
    }
}
