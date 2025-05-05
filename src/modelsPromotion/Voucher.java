package modelsPromotion;

import java.util.Date;

public class Voucher {
    private String id;
    private int sisaPemakaian;
    private Date berlakuHingga;

    public Voucher(String id, int sisaPemakaian, Date berlakuHingga) {
        this.id = id;
        this.sisaPemakaian = sisaPemakaian;
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

    public int getSisaPemakaian() {
        return sisaPemakaian;
    }
}
