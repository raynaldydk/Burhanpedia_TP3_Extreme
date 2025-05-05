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

    public Transaksi getTransaksiById(String transaksiId) {
        for (Transaksi transaksi : transaksiList) {
            if(transaksi.getId().equals(transaksiId)){
                return transaksi;
            }
        }
        return null;
    }

    public void deleteTransaksibyID(String transaksiID) {
        this.getList().remove(this.getTransaksiById(transaksiID));
    }
}
