package modelsProduct;

import java.util.ArrayList;
import java.util.UUID;

public class ProductRepository {
    private String namaToko;
    private ArrayList<Product> productList;

    public ProductRepository(String namaToko) {
        this.namaToko = namaToko;
        productList = new ArrayList<>();
    }

    public Product getProductById(UUID id){
        for (Product product : productList) {
            if(product.getProductId().equals(id)){
                return product;
            }
        }
        return null;
    }

    public String getNamaToko() {
        return namaToko;
    }

    public void setNamaToko(String namaToko) {
        this.namaToko = namaToko;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }
}
