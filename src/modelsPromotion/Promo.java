package modelsPromotion;

import java.util.Date;

public class Promo {
    private String id;
    private Date berlakuHingga;

    public Promo(String id, Date berlakuHingga) {
        this.id = id;
        this.berlakuHingga = berlakuHingga;
    }

    public int calculateDisc(){
        // TODO
        return 0;
    }

    public String getId() {
        return id;
    }

    public Date getBerlakuHingga() {
        return berlakuHingga;
    }
}
