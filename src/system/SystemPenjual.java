package system;

import main.Burhanpedia;
import modelsUser.Penjual;

import java.util.Scanner;

public class SystemPenjual implements SystemMenu{
    private Penjual activePenjual;
    private Scanner input;
    private Burhanpedia mainRepository;

    public SystemPenjual(Penjual activePenjual, Burhanpedia mainRepository) {
        this.activePenjual = activePenjual;
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

    public void handleCekProduk(){

    }

    public void handleTambahProduk(){

    }

    public void handleTambahStok(){

    }

    public void handleUbahHarga(){

    }

    public void handleKirimBarang(){

    }

    public void handleLaporanPendapatan(){

    }

    public void handleCekSaldo(){

    }

    public void handleRiwayatTransaksi(){

    }
}
