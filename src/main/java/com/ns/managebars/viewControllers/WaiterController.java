package com.ns.managebars.viewControllers;


import com.ns.managebars.JFXApplication;
import com.ns.managebars.model.Category;
import com.ns.managebars.model.Order;
import com.ns.managebars.model.Product;
import com.ns.managebars.model.Suborder;
import com.ns.managebars.uiManager.OrderManager;
import com.ns.managebars.uiManager.ProductManager;
import com.ns.managebars.uiManager.UserManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@FxmlView("/views/waiter-view.fxml")
public class WaiterController{

    @FXML
    private VBox categoriesVBox;
    @FXML
    private FlowPane productsFlowPane;
    @FXML
    private TableView<Suborder> orderTable;
    @FXML
    private Label  totalLabel;


    private final UserManager userManager;
    private final List<Category> categories;
    private final OrderManager orderManager;


    private final FxWeaver fxWeaver;

    private int neededPages;
    private int currentPage;
    private List<Product> currentProducts;

    private Order order;

    public WaiterController(ProductManager productManager,
                            UserManager userManager,
                            OrderManager orderManager,
                            FxWeaver fxWeaver) {
        this.userManager = userManager;
        this.categories = productManager.getCategories();
        this.orderManager = orderManager;
        this.fxWeaver = fxWeaver;
    }


    @FXML
    public void initialize() {

        populateCategoriesVBox();

        order = new Order();
        orderTable.setItems(order.toObservable());
    }



    private void populateCategoriesVBox() {

        ToggleGroup categoryGroup = new ToggleGroup();

        for (Category category : categories) {

            RadioButton radioButton = new RadioButton(category.getName());
            radioButton.setUserData(category);
            radioButton.setId("category-button");
            radioButton.getStyleClass().remove("radio-button");
            radioButton.getStyleClass().add("toggle-button");
            radioButton.setToggleGroup(categoryGroup);
            radioButton.setOnAction(this::categoryButtonAction);

            categoriesVBox.getChildren().add(radioButton);
        }

    }

    public void categoryButtonAction(ActionEvent event) {
        Category category = (Category) ((RadioButton) event.getSource()).getUserData();
        currentProducts = category.getProducts();
        neededPages = (int) Math.ceil(currentProducts.size()/productsFlowPane.getChildren().size());
        currentPage = 0;

        populateProductsFlowPane();
    }

    private void populateProductsFlowPane(){

        var productButtons = productsFlowPane.getChildren();

        for (int i = 0; i < productButtons.size() ; i++) {

            Button productButton = (Button) productButtons.get(i);
            productButton.setDisable(false);
            productButton.setVisible(true);

            try {
                int u = i+productButtons.size()*currentPage;
                Product product = currentProducts.get(u);
                productButton.setText(product.getName());
                productButton.setUserData(product);

            }catch (IndexOutOfBoundsException e){
                productButton.setDisable(true);
                productButton.setVisible(false);

            }
        }
    }

    @FXML
    private void productButtonAction(ActionEvent event){

        Button button = (Button)event.getSource();
        Product product = (Product)button.getUserData();

        Suborder subOrder = new Suborder(product);


        order.addSuborder(subOrder);
        updateTotalLabel();


    }

    @FXML
    private void rightButton() {

        if(currentPage == neededPages)
            return;

        currentPage++;
        populateProductsFlowPane();
    }

    @FXML
    private void leftButton() {

        if(currentPage == 0)
            return;

        currentPage--;
       populateProductsFlowPane();
    }

    @FXML
    private void pushOrderAction() {
        if (order.getTotal() != 0) {
            order.setTime(LocalTime.parse(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))));
            order.setDate(LocalDate.now());
            orderManager.pushOrder(order);
            order = new Order();
            orderTable.setItems(order.toObservable());
            updateTotalLabel();
        }

    }

    @FXML
    private void deleteButtonAction() {
        TableSelectionModel<Suborder> selectionModel = orderTable.getSelectionModel();

        if(!order.getOrderList().isEmpty()){
            if(selectionModel.isEmpty()) {
                order.removeSubOrder();
                updateTotalLabel();
            }
            else {
                order.removeSubOrder(selectionModel.getSelectedItem());
                updateTotalLabel();
                selectionModel.clearSelection();
            }
        }


    }

    @FXML
    private void historyButtonAction()  {
        Stage stage = new Stage();
        stage.setScene(new Scene(fxWeaver.loadView(ShiftHistoryController.class)));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();
    }

    @FXML
    private void endShiftAction() {
        boolean confirmed = ManagerController.showConfirmationDialog("Konfirmo Mbylljen e turnit");
        if(confirmed) {
            orderManager.endShift();
            backButtonAction();
        }
    }

    @FXML
    private void backButtonAction() {
       JFXApplication.mainStage.setScene(new Scene(fxWeaver.loadView(LoginController.class)));
        userManager.clearActualUser();

    }

    private void updateTotalLabel() {
        totalLabel.setText(String.valueOf(order.getTotal()));
    }

}
