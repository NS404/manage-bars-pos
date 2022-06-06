package com.ns.managebars.viewControllers;

import com.ns.managebars.JFXApplication;
import com.ns.managebars.model.Category;
import com.ns.managebars.model.Product;
import com.ns.managebars.model.User;
import com.ns.managebars.uiManager.ProductManager;
import com.ns.managebars.uiManager.UserManager;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.converter.IntegerStringConverter;
import net.rgielen.fxweaver.core.FxWeaver;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
@FxmlView("/views/manager-view.fxml")
public class ManagerController {
    public TableView<Category> categoryTable;
    public TableView<Product> productTable;
    public TableColumn<Product,String> productNameColumn;
    public TableColumn<Product,Integer> priceColumn;
    public TableColumn<Category,String> categoryNameColumn;
    private final ProductManager productManager;
    private final FxWeaver fxWeaver;
    public HBox categoryCreationBox;
    public TextField newCategoryNameField;
    public HBox productCreationBox;
    public TextField newProductNameField;
    public TextField newProductPriceField;
    public TableView<User> waiterTable;
    public TableColumn<User,String> waiterNameColumn;
    public VBox waiterCreationBox;
    public TextField newWaiterNameField;
    public PasswordField newWaiterPasscodeField;
    private final UserManager userManager;

    public ManagerController(ProductManager productManager,
                             FxWeaver fxWeaver,
                             UserManager userManager) {
        this.productManager = productManager;
        this.fxWeaver = fxWeaver;
        this.userManager = userManager;
    }

    @FXML
    public void initialize(){
        makeTableColumnsEditable();
        populateCategoryTable();
        populateWaiterTable();
    }

    private void makeTableColumnsEditable(){
        productNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        priceColumn.setCellFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
        categoryNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        waiterNameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }





    private void populateProductTable(Category selectedCategory) {
        List<Product> products = selectedCategory.getProducts();
        productTable.setItems(FXCollections.observableArrayList(products));
    }

    public void toggleProductCreationBox(ActionEvent event) {
        productCreationBox.setVisible(((ToggleButton) event.getSource()).isSelected());
    }

    public void toggleEditableOnProducts(ActionEvent event) {
        productTable.setEditable(((ToggleButton) event.getSource()).isSelected());
    }

    public void commitProductNameEdit(TableColumn.CellEditEvent<Product, String> editEvent) {
        boolean confirmed = showConfirmationDialog("Produktit do ti ndryshohet emri");
        if (confirmed){
            Product product = editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
            String newName = editEvent.getNewValue();
            productManager.changeProductName(product,newName);
            productTable.refresh();
        }

    }

    public void commitProductPriceEdit(TableColumn.CellEditEvent<Product, Integer> editEvent) {
        boolean confirmed = showConfirmationDialog("Produktit do ti ndryshohet cmimi");
        if (confirmed){
            Product product = editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
            int newPrice = editEvent.getNewValue();
            productManager.changeProductPrice(product,newPrice);
            productTable.refresh();
        }

    }

    public void submitProductCreationAction() {
        String newProductName = newProductNameField.getText();
        String newProductPrice = newProductPriceField.getText();
        if(!newProductName.isBlank() && !newProductPrice.isBlank()){

            boolean confirmed  = showConfirmationDialog("Nje produkt i re do te shtohet");
            if(confirmed) {
                Category selectedCategory = categoryTable.getSelectionModel().getSelectedItem();
                Product newProduct = productManager.createNewProduct(newProductName,
                                                                    Integer.parseInt(newProductPrice),
                                                                    selectedCategory);
                productTable.getItems().add(newProduct);
                newProductNameField.clear();
                newProductPriceField.clear();
            }
        }



    }

    public void deleteProductAction() {
        SelectionModel<Product> selectionModel = productTable.getSelectionModel();

        if(!selectionModel.isEmpty()){
            boolean confirmed = showConfirmationDialog("Prdodukti i zgjedhur do te fshihet");

            if(confirmed) {
                Product selectedProduct = selectionModel.getSelectedItem();
                productManager.deleteProduct(selectedProduct);
                productTable.getItems().remove(selectedProduct);
            }
        }
    }




    private void populateCategoryTable(){
        categoryTable.setItems(FXCollections.observableArrayList(productManager.getCategories()));
    }

    public void toggleEditableOnCategories(ActionEvent event) {
        categoryTable.setEditable(((ToggleButton) event.getSource()).isSelected());
    }

    public void toggleCategoryCreationBox(ActionEvent event) {
    categoryCreationBox.setVisible(((ToggleButton) event.getSource()).isSelected());
    }

    public void commitCategoryNameEdit(TableColumn.CellEditEvent<Category, String> editEvent) {
        boolean confirmed = showConfirmationDialog("Kategorise do ti ndryshohet emri");
        if (confirmed){
            Category category = editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
            String newName = editEvent.getNewValue();
            productManager.changeCategoryName(category,newName);
            categoryTable.refresh();
        }
    }

    public void submitCategoryCreationAction() {
        String newCategoryName = newCategoryNameField.getText();
        if(!newCategoryName.isBlank()){

            boolean confirmed  = showConfirmationDialog("Nje kategori e re do te shtohet");
            if(confirmed) {
                Category newCategory = productManager.createNewCategory(newCategoryName);
                categoryTable.getItems().add(newCategory);
                newCategoryNameField.clear();
            }
        }
    }

    public void deleteCategoryAction() {
        SelectionModel<Category> selectionModel = categoryTable.getSelectionModel();

        if(!selectionModel.isEmpty()){
            boolean confirmed = showConfirmationDialog("Kategoria e zgjedhur do te fshihet");

            if(confirmed) {
                Category selectedCategory = selectionModel.getSelectedItem();
                productManager.deleteCategory(selectedCategory);
                categoryTable.getItems().remove(selectedCategory);
                productTable.getItems().clear();
            }
        }
    }

    public void categorySelected() {
        SelectionModel<Category> selectionModel = categoryTable.getSelectionModel();
        if(!selectionModel.isEmpty()){
            Category selectedCategory = selectionModel.getSelectedItem();
            populateProductTable(selectedCategory);
        }
    }




    private void populateWaiterTable(){
        waiterTable.setItems(FXCollections.observableArrayList(userManager.getUsers()));
    }

    public void toggleWaiterCreationBox(ActionEvent event) {
        waiterCreationBox.setVisible(((ToggleButton) event.getSource()).isSelected());
    }

    public void toggleEditableOnWaiters(ActionEvent event) {
        waiterTable.setEditable(((ToggleButton) event.getSource()).isSelected());
    }

    public void deleteWaiterAction() {
        SelectionModel<User> selectionModel = waiterTable.getSelectionModel();

        if(!selectionModel.isEmpty()){
            boolean confirmed = showConfirmationDialog("Kamarieri i zgjedhur do te fshihet");
            if(confirmed) {
                User selectedUser = selectionModel.getSelectedItem();
                userManager.deleteUser(selectedUser);
                waiterTable.getItems().remove(selectedUser);
            }
        }
    }

    public void submitWaiterCreationAction() {
        String newWaiterName = newWaiterNameField.getText();
        String newWaiterPasscode = newWaiterPasscodeField.getText();
        if(!newWaiterName.isBlank() && !newWaiterPasscode.isBlank()){

            boolean confirmed  = showConfirmationDialog("Nje kamarier i ri do te shtohet");
            if(confirmed) {
                User newWaiter = userManager.createNewUser(newWaiterName,newWaiterPasscode);
                waiterTable.getItems().add(newWaiter);
                newWaiterNameField.clear();
                newWaiterPasscodeField.clear();
            }
        }



    }

    public void commitWaiterNameEdit(TableColumn.CellEditEvent<User,String> editEvent) {
        boolean confirmed = showConfirmationDialog("Kamarierit do ti ndryshohet emri");
        if (confirmed){
            User waiter = editEvent.getTableView().getItems().get(editEvent.getTablePosition().getRow());
            String newName = editEvent.getNewValue();
            userManager.changeUserName(waiter,newName);
            waiterTable.refresh();
        }
    }




    public static boolean showConfirmationDialog(String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Konfirmo!");
        alert.setContentText(content);
        return (alert.showAndWait().get().equals(ButtonType.OK));
    }

    public void returnToLogin() {
        JFXApplication.mainStage.setScene(new Scene(fxWeaver.loadView(LoginController.class)));

    }
}
