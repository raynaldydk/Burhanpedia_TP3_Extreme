package system;

import main.Burhanpedia;
import modelsProduct.CartProduct;
import modelsProduct.Product;
import modelsUser.Pembeli;
import modelsUser.Penjual;
import modelsUser.User;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

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
        // Input nama toko
        System.out.print("Masukkan toko penjual yang ingin dibeli");
        String namaToko = input.nextLine();

        // Cek apakah ada toko dengan nama tersebut
        if(getPenjualByNamaToko(namaToko) == null){
            System.out.printf("Tidak ada toko dengan nama %s!\n", namaToko);
            return;
        }

        // Get instance penjual
        Penjual penjual = getPenjualByNamaToko(namaToko);

        // Input nama barang yang mau dibeli
        System.out.print("Masukkan nama barang yang ingin dibeli: ");
        String namaBarang = input.nextLine();

        // Cek apakah ada produk dengan nama tersebut
        if(getProdukByPenjual(penjual, namaBarang) == null){
            System.out.printf("Tidak ada produk dengan nama %s pada repo!\n", namaBarang);
            return;
        }

        // Get instance Product
        Product product = getProdukByPenjual(penjual, namaBarang);

        // Input jumlah barang yang ingin dibeli
        System.out.print("Masukkan jumlah barang yang ingin dibeli: ");
        int jumlahBarang = input.nextInt();
        input.nextLine();

        // Validasi input qty barang
        if(jumlahBarang < 0){
            System.out.println("Qty barang tidak boleh negatif atau nol!");
            return;
        }

        // Cek apakah cart sudah memiliki barang dari toko lain
        if(!activePembeli.getCart().getCartContent().isEmpty()){
            CartProduct cartProduct = activePembeli.getCart().getCartContent().get(0);
            Penjual penjualInCart = getPenjualByProductId(cartProduct.getProductId());

            if(penjualInCart != penjual){
                System.out.println("Anda sudah memiliki barang di keranjang yang berasal dari toko berbeda.");
                System.out.print("Kosongkan keranjang dan masukkan barang yang baru? (Y/N): ");
                String kosongkanKeranjang = input.nextLine();

                if(kosongkanKeranjang.equalsIgnoreCase("y")){
                    activePembeli.getCart().getCartContent().clear();
                }

                else if(kosongkanKeranjang.equalsIgnoreCase("n")){
                    System.out.println("Penambahan barang dibatalkan!");
                    return;
                }

                else{
                    System.out.println("Input yang anda masukkan salah!");
                    return;
                }
            }
        }

        // Masukkan barang ke cart
        activePembeli.getCart().addToCart(product.getProductId(), jumlahBarang);

        // Success msg
        System.out.println("Barang berhasil ditambahkan!");
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

    public Penjual getPenjualByNamaToko(String namaToko){
        for(Penjual penjual: getDaftarPenjual()){
            if(penjual.getRepo().getNamaToko().equalsIgnoreCase(namaToko)){
                return penjual;
            }
        }
        return null;
    }

    public Product getProdukByPenjual(Penjual penjual, String namaProduk){
        for(Product product : penjual.getRepo().getProductList()){
            if(product.getProductName().equalsIgnoreCase(namaProduk)){
                return product;
            }
        }
        return null;
    }

    public Penjual getPenjualByProductId(UUID productId){
        for (Penjual penjual : getDaftarPenjual()){
            for (Product product : penjual.getRepo().getProductList()){
                if(product.getProductId().equals(productId)){
                    return penjual;
                }
            }
        }
        return null;
    }

}
