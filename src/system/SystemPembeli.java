package system;

import main.Burhanpedia;
import modelsUser.Pembeli;

import java.util.Scanner;

public class SystemPembeli implements SystemMenu {
    private Pembeli activePembeli;
    private Scanner input;
    private Burhanpedia mainRepository;

    public SystemPembeli(Pembeli activePembeli, Burhanpedia mainRepository) {
        this.activePembeli = activePembeli;
        this.input = new Scanner(System.in);
        this.mainRepository = mainRepository;
    }

    @Override
    public String showMenu() {
        return "";
    }

    @Override
    public void handleMenu() {

    }

    public void handleCekSaldo(){

    }

    public void handleTopUpSaldo(){

    }

    public void handleCekDaftarBarang(){

    }

    public void handleTambahToKeranjang(){

    }

    public void handleCheckout(){

    }

    public void handleLacakBarang(){

    }

    public void handleLaporanPengeluaran(){

    }

    public void handleRiwayatTransaksi(){

    }
}
