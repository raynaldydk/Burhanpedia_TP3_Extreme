package system;

import main.Burhanpedia;
import modelsProduct.Product;
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
        return """
                
                ===== MENU PENJUAL =====
                1. Cek Produk
                2. Tambah Produk
                3. Tambah Stok
                4. Ubah Harga Barang
                5. Kirim Barang
                6. Lihat Laporan Pendapatan
                7. Cek Saldo
                8. Lihat Riwayat Transaksi
                9. Kembali ke Menu Utama
                """;
    }

    @Override
    public void handleMenu() {
        String pilihan;

        do{
            System.out.println(showMenu());
            System.out.print("Perintah: ");
            pilihan = input.nextLine();

            switch (pilihan){
                case "1":
                    handleCekProduk();
                    break;
                case "2":
                    handleTambahProduk();
                    break;
                case "3":
                    handleTambahStok();
                    break;
                case "4":
                    handleUbahHarga();
                    break;
                case "5":
                    handleKirimBarang();
                    break;
                case "6":
                    handleLaporanPendapatan();
                    break;
                case "7":
                    handleCekSaldo();
                    break;
                case "8":
                    handleRiwayatTransaksi();
                    break;
                case "9":
                    break;
                default:
                    System.out.println("Input salah!\n");
                    break;
            }
        }while(!pilihan.equals("9"));
    }

    public void handleCekProduk(){
        if(activePenjual.getRepo().getProductList().isEmpty()){
            System.out.println("""
                    ==============================
                    Toko belum memiliki produk!
                    ==============================
                    """);
            return;
        }

        System.out.println("==================================");
        for(Product product : activePenjual.getRepo().getProductList()){
            System.out.printf("%-10s %10.2f %10d\n",
                    product.getProductName(),
                    (double) product.getProductPrice(),
                    product.getProductStock());
        }
        System.out.println("==================================");
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
