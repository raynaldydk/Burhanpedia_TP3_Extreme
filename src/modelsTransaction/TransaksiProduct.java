package modelsTransaction;

import java.util.UUID;

public class TransaksiProduct {
    private UUID productId;
    private int amount;

    public TransaksiProduct(UUID productId, int amount) {
        this.productId = productId;
        this.amount = amount;
    }

    public UUID getProductId() {
        return productId;
    }

    public int getProductAmount() {
        return amount;
    }
}
