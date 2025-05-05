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
        return """
                
                ===== MENU PEMBELI =====
                1. Cek Saldo
                2. Top Up Saldo
                3. Cek Daftar Barang
                4. Tambah Barang ke Keranjang
                5. Checkout Keranjang
                6. Lacak Barang
                7. Lihat Laporan Pengeluaran
                8. Lihat Riwayat Transaksi
                9. Kembali ke Menu Utama
                """;
    }

    @Override
    public void handleMenu() {
        String pilihanMenuPembeli;

        do{
            System.out.println(showMenu());
            System.out.print("Perintah: ");
            pilihanMenuPembeli = input.nextLine();
            switch (pilihanMenuPembeli) {
                case "1":
                    handleCekSaldo();
                    break;
                case "2":
                    handleTopUpSaldo();
                    break;
                case "3":
                    handleCekDaftarBarang();
                    break;
                case "4":
                    handleTambahToKeranjang();
                    break;
                case "5":
                    handleCheckout();
                    break;
                case "6":
                    handleLacakBarang();
                    break;
                case "7":
                    handleLaporanPengeluaran();
                    break;
                case "8":
                    handleRiwayatTransaksi();
                    break;
                case "9":
                    break;
                default:
                    System.out.println("Input yang anda masukkan salah!");
                    break;
            }
        }while(!pilihanMenuPembeli.equals("9"));
    }

    public void handleCekSaldo(){
        System.out.println("==============================");
        System.out.printf("Saldo saat ini: %.2f\n", (double) activePembeli.getBalance());
        System.out.println("==============================");
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
