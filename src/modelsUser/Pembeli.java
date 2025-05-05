package modelsUser;

import modelsProduct.Cart;

public class Pembeli extends User{
    private Cart keranjang;

    public Pembeli(String username, String password, String role) {
        super(username, password, role);
    }

    public Cart getCart(){
        return keranjang;
    }

    public void setKeranjang(Cart keranjang) {
        this.keranjang = keranjang;
    }
}
