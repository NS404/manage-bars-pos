package com.ns.managebars.sevice;

import com.ns.managebars.model.Category;
import com.ns.managebars.model.Product;
import com.ns.managebars.repo.CategoryRepo;
import com.ns.managebars.repo.ProductRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final CategoryRepo categoryRepo;
    private final ProductRepo productRepo;

    public ProductService(CategoryRepo categoryRepo, ProductRepo productRepo) {
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
    }


    public List<Category> queryCategories(){
        List<Category> categories = categoryRepo.getAllCategories();
        for (Category cat : categories) {
            cat.setProducts(productRepo.getProductsByCategory(cat.getId()));
        }
        return categories;
    }


    public void updateCategoryName(Category category, String newName) {
        categoryRepo.updateCategoryName(category,newName);
    }

    public void saveNewCategory(Category category) {
        categoryRepo.save(category);
    }

    public void removeCategory(Category category) {
        categoryRepo.removeCategory(category.getId());
    }

    public void removeProduct(Product product) {
        productRepo.removeProduct(product.getId());
    }

    public void updateProductName(Product product, String newName) {
        productRepo.updateProductName(product,newName);
    }

    public void updateProductPrice(Product product, int newPrice) {
        productRepo.updateProductPrice(product,newPrice);
    }

    public void saveNewProduct(Product product) {
        productRepo.save(product);
    }
}
