package modelsProduct;

import java.util.UUID;

public class Product {
    private UUID productId;
    private String name;
    private int stok;
    private long price;

    public Product(String name, int stok, long price) {
        this.productId = UUID.randomUUID();
        this.name = name;
        this.stok = stok;
        this.price = price;
    }

    public UUID getProductId() {
        return productId;
    }

    public String getProductName() {
        return name;
    }

    public int getProductStock() {
        return stok;
    }

    public long getProductPrice() {
        return price;
    }

    public void setProductStock(int stok) {
        this.stok = stok;
    }

    public void setProductPrice(long price) {
        this.price = price;
    }
}
