package modelsUser;

import modelsProduct.ProductRepository;

public class Penjual extends User{
    private ProductRepository productRepo;

    public Penjual(String username, String password, String role) {
        super(username, password, role);
    }

    public ProductRepository getRepo() {
        return productRepo;
    }
}
