package modelsProduct;

import java.util.ArrayList;
import java.util.UUID;

public class Cart {
    private ArrayList<CartProduct> keranjang;

    public Cart() {
        keranjang = new ArrayList<>();
    }

    public void addToCart(UUID productId, int amount){
        CartProduct cartProduct = new CartProduct(productId, amount);
        keranjang.add(cartProduct);
    }

    public String deleteFromCart(UUID productId){
        for(CartProduct cartProduct : keranjang){
            if(cartProduct.getProductId().equals(productId)){
                keranjang.remove(cartProduct);
            }
        }
        return "";
    }

    public ArrayList<CartProduct> getCartContent(){
        return keranjang;
    }
}
