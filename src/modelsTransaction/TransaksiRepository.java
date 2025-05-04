package modelsTransaction;

import java.util.ArrayList;

public class TransaksiRepository {
    private ArrayList<Transaksi> transaksiList;

    public TransaksiRepository() {
        transaksiList = new ArrayList<>();
    }

    public void addTransaksi(Transaksi transaksi) {
        transaksiList.add(transaksi);
    }

    public ArrayList<Transaksi> getList() {
        return transaksiList;
    }

    public void prosesTransaksi(String statusTransaksi) {
        // TODO
    }
}
