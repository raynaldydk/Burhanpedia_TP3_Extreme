package system;

import main.Burhanpedia;
import modelsProduct.Product;
import modelsTransaction.Transaksi;
import modelsUser.Penjual;

import java.util.ArrayList;
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
            System.out.print("""
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
        // Input nama produk
        System.out.print("\nMasukkan nama produk: ");
        String namaProduk = input.nextLine();

        // Cek apakah product sudah pernah di input
        if(produkTersedia(namaProduk)){
            System.out.println("Produk sudah ada pada repository!");
            return;
        }

        // Input stok produk
        System.out.print("Masukkan stok produk: ");
        int stokProduk = input.nextInt();
        input.nextLine();

        // Cek apakah input lebih kecil dari sama dengan nol
        if(stokProduk <= 0){
            System.out.println("Input stok tidak boleh nol atau negatif!");
            return;
        }

        // Input harga produk
        System.out.print("Masukkan harga produk: ");
        long hargaProduk = input.nextLong();
        input.nextLine();

        // Cek apakah input harga valid
        if(hargaProduk <= 0){
            System.out.println("Input harga tidak boleh nol atau negatif!");
            return;
        }

        // Input produk baru ke productRepo
        Product produkBaru = new Product(namaProduk, stokProduk, hargaProduk);
        activePenjual.getRepo().getProductList().add(produkBaru);
        System.out.println("Berhasil menambahkan produk baru!");

    }

    public void handleTambahStok(){
        // Input nama produk
        System.out.print("Masukkan nama produk: ");
        String namaProduk = input.nextLine();

        // Cek apakah produk tersebut ada pada productRepo
        if(!produkTersedia(namaProduk)){
            System.out.printf("Tidak ada produk %s pada repository!\n", namaProduk.toLowerCase());
            return;
        }

        // Input jumlah stok yang ingin ditambah
        System.out.print("Masukkan jumlah stok yang ingin ditambah: ");
        int stokProduk = input.nextInt();
        input.nextLine();

        // Cek apakah input valid
        if(stokProduk <= 0){
            System.out.println("Input stok tidak boleh negatif atau nol!");
            return;
        }

        // Simpan stok baru ke repository product
        Product produk = activePenjual.getRepo().getProductByName(namaProduk);
        produk.setProductStock(produk.getProductStock() + stokProduk);

        // Success message
        System.out.printf("Stok %s berhasil ditambah! Stok saat ini: %d\n",
                produk.getProductName(),
                produk.getProductStock()
        );
    }

    public boolean produkTersedia(String namaProduk){
        for(Product product : activePenjual.getRepo().getProductList()){
            if (product.getProductName().equalsIgnoreCase(namaProduk)){
                return true;
            }
        }
        return false;
    }

    public void handleUbahHarga(){
        // Input nama produk
        System.out.print("Masukkan nama produk: ");
        String namaProduk = input.nextLine();

        // Cek apakah produk tersedia pada repository
        if(!produkTersedia(namaProduk)){
            System.out.printf("Tidak ada produk %s pada repository!\n", namaProduk.toLowerCase());
            return;
        }

        // Search produk
        Product produk = activePenjual.getRepo().getProductByName(namaProduk);

        // Input harga produk yang baru
        System.out.print("Masukkan harga produk yang baru: ");
        long hargaProdukBaru = input.nextLong();
        input.nextLine();

        // Cek apakah input valid
        if(hargaProdukBaru <= 0){
            System.out.println("Input harga tidak boleh negatif atau nol!");
            return;
        }

        // Simpan harga baru ke product repo
        produk.setProductPrice(hargaProdukBaru);

        // Success msg
        System.out.printf("Harga %s diperbarui: %d\n", produk.getProductName(), hargaProdukBaru);
    }

    public void handleKirimBarang(){
        // Cek apakah ada barang yang perlu dikirim
        if(getTransaksiPerluDikirimList() == null){
            System.out.println("========================================");
            System.out.println("Tidak ada barang yang bisa dikirim!");
            System.out.println("========================================");
            return;
        }

        // TODO
    }

    public void handleLaporanPendapatan(){
        // Cek apakah ada transaksi yang dilakukan penjual
        if(getTransaksiListByPenjual() == null){
            System.out.println("================================");
            System.out.println("Laporan pendapatan masih kosong!");
            System.out.println("================================");
            return;
        }

        // TODO
    }

    public void handleCekSaldo(){
        System.out.println("==============================");
        System.out.printf("Saldo saat ini: %.2f\n", (double) activePenjual.getBalance());
        System.out.println("==============================");
    }

    public void handleRiwayatTransaksi(){
        // Cek apakah ada transaksi yang dilakukan penjual
        if(getTransaksiListByPenjual() == null){
            System.out.println("================================");
            System.out.println("Riwayat transaksi masih kosong! ");
            System.out.println("================================");
            return;
        }
        // TODO
    }

    public ArrayList<Transaksi> getTransaksiListByPenjual(){
        ArrayList<Transaksi> transaksiList = new ArrayList<>();

        for(Transaksi transaksi : mainRepository.getTransaksiRepo().getList()){
            if(transaksi.getNamePenjual().equalsIgnoreCase(activePenjual.getRepo().getNamaToko())){
                transaksiList.add(transaksi);
            }
        }

        return transaksiList;
    }

    public ArrayList<Transaksi> getTransaksiPerluDikirimList(){
        ArrayList<Transaksi> transaksiList = new ArrayList<>();

        for(Transaksi transaksi : mainRepository.getTransaksiRepo().getList()){
            if(transaksi.getNamePenjual().equalsIgnoreCase(activePenjual.getRepo().getNamaToko())){
                if(transaksi.getCurrentStatus().equalsIgnoreCase("Sedang Dikemas")){
                    transaksiList.add(transaksi);
                }
            }
        }

        return transaksiList;
    }
}
