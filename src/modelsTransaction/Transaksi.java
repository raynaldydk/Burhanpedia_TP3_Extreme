package modelsTransaction;

import java.util.ArrayList;

public class Transaksi {
    private String id;
    private String namePembeli;
    private String namePenjual;
    private String namePengirim;
    private String idDiskon;
    private ArrayList<TransactionStatus> historyStatus;
    private ArrayList<TransaksiProduct> produkDibeli;
    private String jenisTransaksi;
    private long biayaOngkir;

    public Transaksi() {

    }

    public String refund(){
        // TODO
        return "";
    }

    public String getCurrentStatus(){
        return this.historyStatus.get(historyStatus.size() - 1).getStatus();
    }
}
