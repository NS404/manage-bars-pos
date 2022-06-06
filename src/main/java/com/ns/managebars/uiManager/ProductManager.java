package com.ns.managebars.uiManager;

import com.ns.managebars.model.Category;
import com.ns.managebars.model.Product;
import com.ns.managebars.sevice.ProductService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductManager {

    private final ProductService productService;

    private List<Category> categories;

    public ProductManager(ProductService productService) {
        this.productService = productService;
        setCategories();
    }


    private void setCategories(){
        categories = productService.queryCategories();
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void changeCategoryName(Category category, String newName) {
        productService.updateCategoryName(category,newName);
        category.setName(newName);
    }

    public Category createNewCategory(String categoryName) {
        Category category = new Category(categoryName);
        productService.saveNewCategory(category);
        categories.add(category);
        return category;
    }

    public void deleteCategory(Category category) {
        List<Product> products = category.getProducts();
        for (Product product : products) {
            productService.removeProduct(product);
        }
        products.clear();
        productService.removeCategory(category);
        categories.remove(category);
    }

    public void changeProductName(Product product, String newName) {
        productService.updateProductName(product,newName);
        product.setName(newName);
    }

    public void changeProductPrice(Product product, int newPrice) {
        productService.updateProductPrice(product,newPrice);
        product.setPrice(newPrice);
    }

    public Product createNewProduct(String newProductName, int newProductPrice, Category newProductCategory) {
        Product product = new Product(newProductName,newProductPrice,newProductCategory);
        productService.saveNewProduct(product);
        newProductCategory.getProducts().add(product);
        return product;
    }

    public void deleteProduct(Product selectedProduct) {
        productService.removeProduct(selectedProduct);

    }
}
