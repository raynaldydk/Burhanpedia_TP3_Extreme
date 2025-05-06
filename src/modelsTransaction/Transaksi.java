package modelsTransaction;

import main.Burhanpedia;
import modelsUser.Pembeli;

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

    public Transaksi(String id, String namePembeli, String namePenjual, String idDiskon, String jenisTransaksi, long biayaOngkir) {
        this.id = id;
        this.namePembeli = namePembeli;
        this.namePenjual = namePenjual;
        this.namePengirim = null;
        this.idDiskon = idDiskon;
        this.jenisTransaksi = jenisTransaksi;
        this.biayaOngkir = biayaOngkir;
        historyStatus = new ArrayList<>();
        produkDibeli = new ArrayList<>();
    }

    public String refund(Burhanpedia mainRepository) {
        // Update status transaksi
        TransactionStatus statusTransaksi = new TransactionStatus(mainRepository.getDate(), "Dikembalikan");
        this.getHistoryStatus().add(statusTransaksi);

        // Kembalikan balance pembeli
        Pembeli pembeli = (Pembeli) mainRepository.getUserRepo().getUserByName(this.getNamePembeli());
        pembeli.setBalance(pembeli.getBalance() + mainRepository.calculateTotalTransaksi(this.getId()));

        return String.format("Transaksi dengan ID %s berhasil di refund!", this.getId());
    }

    public String getCurrentStatus(){
        return this.historyStatus.get(historyStatus.size() - 1).getStatus();
    }

    public String getNamePenjual() {
        return namePenjual;
    }

    public ArrayList<TransaksiProduct> getProdukDibeli() {
        return produkDibeli;
    }

    public ArrayList<TransactionStatus> getHistoryStatus() {
        return historyStatus;
    }

    public String getId() {
        return id;
    }

    public String getIdDiskon() {
        return idDiskon;
    }

    public long getBiayaOngkir() {
        return biayaOngkir;
    }

    public String getNamePembeli() {
        return namePembeli;
    }

    public String getNamePengirim() {
        return namePengirim;
    }

    public String getJenisTransaksi() {
        return jenisTransaksi;
    }

    public void setNamePengirim(String namePengirim) {
        this.namePengirim = namePengirim;
    }
}
