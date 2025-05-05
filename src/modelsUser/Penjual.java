package modelsUser;

import modelsProduct.ProductRepository;

public class Penjual extends User{
    private ProductRepository productRepo;

    public Penjual(String username, String password, String role, String namaToko) {
        super(username, password, role);
        this.productRepo = new ProductRepository(namaToko);
    }

    public ProductRepository getRepo() {
        return productRepo;
    }
}
