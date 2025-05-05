package system;

import main.Burhanpedia;
import modelsProduct.Product;
import modelsUser.Pembeli;
import modelsUser.Penjual;
import modelsUser.User;

import java.util.ArrayList;
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
        // Input saldo yang mau ditambahkan
        System.out.print("Masukkan saldo yang ingin ditambah: ");
        long saldoTopUp = input.nextLong();
        input.nextLine();

        // Cek apakah jumlah top up saldo valid
        if(saldoTopUp <= 0){
            System.out.println("Nominal top up saldo tidak boleh negatif atau nol!");
            return;
        }

        // Tambahkan saldo ke dompet pembeli
        activePembeli.setBalance(saldoTopUp + activePembeli.getBalance());

        // Success msg
        System.out.printf("Saldo berhasil ditambah! Saldo saat ini: %.2f\n", (double) activePembeli.getBalance());
    }

    public void handleCekDaftarBarang(){
        // Cek apakah ada penjual yang memiliki dagangan
        if(getJumlahPenjualDenganProduk() == 0){
            System.out.println("==============================");
            System.out.println("Toko belum memiliki produk!");
            System.out.println("==============================");
            return;
        }

        ArrayList<Penjual> daftarPenjual = getDaftarPenjual();
        System.out.println("========================================");
        for(Penjual penjual : daftarPenjual){
            // Print nama toko
            System.out.println(penjual.getRepo().getNamaToko());

            // Print produk
            for(Product product : penjual.getRepo().getProductList()){
                System.out.printf("%-10s %14.2f %d\n",
                        product.getProductName(),
                        (double) product.getProductPrice(),
                        product.getProductStock()
                );
            }

            // Print dashes line
            if(penjual != daftarPenjual.get(daftarPenjual.size() - 1)){
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================");
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

    public ArrayList<Penjual> getDaftarPenjual(){
        ArrayList<Penjual> daftarPenjual = new ArrayList<>();

        for (User user : mainRepository.getUserRepo().getAll()){
            if(user.getRole().equals("Penjual")){
                daftarPenjual.add((Penjual) user);
            }
        }

        return daftarPenjual;
    }

    public int getJumlahPenjualDenganProduk(){
        int total = 0;

        for(Penjual penjual : getDaftarPenjual()){
            if(!penjual.getRepo().getProductList().isEmpty()){
                total++;
            }
        }

        return total;
    }
}
