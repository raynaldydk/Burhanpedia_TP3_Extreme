package modelsTransaction;

import java.util.ArrayList;
import java.util.Date;

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

    public void prosesTransaksi(String TransaksiId) {
        Transaksi transaksi = getTransaksiById(TransaksiId);

        if(transaksi.getCurrentStatus().equalsIgnoreCase("Sedang Dikemas")){
            TransactionStatus statusTransaksi = new TransactionStatus(new Date(), "Menunggu Pengirim");
            transaksi.getHistoryStatus().add(statusTransaksi);
        }
        else if(transaksi.getCurrentStatus().equalsIgnoreCase("Menunggu Pengirim")){
            TransactionStatus statusTransaksi = new TransactionStatus(new Date(), "Sedang Dikirim");
            transaksi.getHistoryStatus().add(statusTransaksi);
        }
        else if(transaksi.getCurrentStatus().equalsIgnoreCase("Sedang Dikirim")){
            TransactionStatus statusTransaksi = new TransactionStatus(new Date(), "Pesanan Selesai");
            transaksi.getHistoryStatus().add(statusTransaksi);
        }
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
