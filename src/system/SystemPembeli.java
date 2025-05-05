package system;

import main.Burhanpedia;
import modelsProduct.Cart;
import modelsProduct.CartProduct;
import modelsProduct.Product;
import modelsTransaction.TransactionStatus;
import modelsTransaction.Transaksi;
import modelsTransaction.TransaksiProduct;
import modelsUser.Pembeli;
import modelsUser.Penjual;
import modelsUser.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

        ArrayList<Penjual> daftarPenjual = mainRepository.getDaftarPenjual();
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
            Penjual penjualInCart = mainRepository.getPenjualByProductId(cartProduct.getProductId());

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
        // Cek apakah keranjang masih kosong
        if(activePembeli.getCart().getCartContent().isEmpty()){
            System.out.println("==============================");
            System.out.println("Keranjang masih kosong!");
            System.out.println("==============================");
            return;
        }

        // Inisialisasi subtotal
        double subtotal = 0;

        // Tampilkan barang di keranjang
        System.out.println("========================================");
        for (CartProduct cartProduct : activePembeli.getCart().getCartContent()) {
            // Get penjual yang ada di keranjang
            Penjual penjual = mainRepository.getPenjualByProductId(cartProduct.getProductId());

            // Get data produk
            Product product = penjual.getRepo().getProductById(cartProduct.getProductId());
            String namaProduk = product.getProductName();
            double hargaProduk = product.getProductPrice();
            int qtyProduk = cartProduct.getProductAmount();
            double totalHarga = product.getProductPrice() * qtyProduk;

            // Tambahkan ke subtotal
            subtotal += totalHarga;

            // Print keranjang
            System.out.printf("%-12s %13.2f %4d (%.2f)\n", namaProduk, hargaProduk, qtyProduk, totalHarga);
        }
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:   %.2f\n", subtotal);
        System.out.println("========================================");

        // Konfirmasi produk yang di checkout
        System.out.print("Apakah anda yakin dengan produknya? (Y/N): ");
        String konfirmasiProduk = input.nextLine();

        if (konfirmasiProduk.equals("n")) {
            System.out.println("Checkout dibatalkan!");
            return;
        } else if (!konfirmasiProduk.equals("y")) {
            System.out.println("Input yang anda masukkan salah!");
            return;
        }

        // Input voucher / promo user
        System.out.println("Masukkan kode voucher.");
        System.out.println("Jika tidak ada, ketik 'skip'");
        System.out.println("========================================");
        System.out.print("Kode: ");
        String diskonId = input.nextLine();

        // Cek apakah stok penjual cukup
        if(!cekStokPenjual()) return;

        // Pilih opsi pengiriman
        System.out.println("Pilih opsi pengiriman: ");
        System.out.println("1. Instant  (20.000)");
        System.out.println("2. Reguler  (15.000)");
        System.out.println("3. Next Day (10.000)");
        System.out.print("Pilihan pengiriman: ");
        String pilihanOngkir = input.nextLine();

        // Get jenis transaksi & biaya ongkir
        String jenisTransaksi;
        long biayaOngkir;

        if(pilihanOngkir.equals("1")){
            jenisTransaksi = "Instant";
            biayaOngkir = 20000;
        }
        else if(pilihanOngkir.equals("2")){
            jenisTransaksi = "Reguler";
            biayaOngkir = 15000;
        }
        else if(pilihanOngkir.equals("3")){
            jenisTransaksi = "Next Day";
            biayaOngkir = 10000;
        }
        else{
            System.out.println("Input yang anda masukkan salah!");
            return;
        }

        // Buat instance transaksi baru
        String transaksiId = generateTransaksiId();
        String namaPembeli = activePembeli.getUsername();
        Penjual penjual = mainRepository.getPenjualByProductId(activePembeli.getCart().getCartContent().get(0).getProductId());
        String namaToko = penjual.getRepo().getNamaToko();
        Transaksi transaksiBaru = new Transaksi(transaksiId, namaPembeli, namaToko, diskonId, jenisTransaksi, biayaOngkir);

        // Tambahkan produk dibeli & status transaksi
        for (CartProduct cartProduct : activePembeli.getCart().getCartContent()) {
            Product produk = penjual.getRepo().getProductById(cartProduct.getProductId());
            Date tanggalTransaksi = mainRepository.getDate();

            //Tambah produk dibeli
            TransaksiProduct transaksiProduct = new TransaksiProduct(produk.getProductId(), cartProduct.getProductAmount());
            transaksiBaru.getProdukDibeli().add(transaksiProduct);

            // Tambah status transaksi
            TransactionStatus statusTransaksi = new TransactionStatus(tanggalTransaksi, "Sedang Dikemas");
            transaksiBaru.getHistoryStatus().add(statusTransaksi);
        }

        // Simpan transaksi baru ke repo
        mainRepository.getTransaksiRepo().addTransaksi(transaksiBaru);

        // Kalkulasi harga final
        long finalPrice = mainRepository.calculateTotalTransaksi(transaksiId);

        if(activePembeli.getBalance() >= finalPrice && finalPrice != -1){
            // Kurangi saldo pembeli
            activePembeli.setBalance(activePembeli.getBalance() - finalPrice);

            // Masukkan history status transaksi
            Date date = new Date();
            transaksiBaru.getHistoryStatus().add(new TransactionStatus(date, "Sedang Dikemas"));

            // Kurangi pemakaian voucher
            if(!diskonId.equalsIgnoreCase("skip")){

                // Cek apakah promo atau voucher
                if(mainRepository.getVoucherRepo().getById(transaksiBaru.getIdDiskon()) != null){
                    mainRepository.getVoucherRepo().getById(transaksiBaru.getIdDiskon()).pakaiVoucher();
                }
            }

            // KURANGI STOK PRODUK PENJUAL
            for (CartProduct cartProduct : activePembeli.getCart().getCartContent()) {
                Product product = penjual.getRepo().getProductById(cartProduct.getProductId());
                if (product != null) {
                    product.setProductStock(product.getProductStock() - cartProduct.getProductAmount());
                }
            }

            // Hapus keranjang
            activePembeli.setKeranjang(new Cart());

            // Success msg
            System.out.printf("Pembelian sukses! Saldo saat ini: %.2f\n", (double) activePembeli.getBalance());
        }
        else{
            // Delete transaksi
            mainRepository.getTransaksiRepo().deleteTransaksibyID(transaksiId);
            System.out.println("Pembelian gagal. Saldo tidak cukup!");
        }
    }

    public void handleLacakBarang(){

    }

    public void handleLaporanPengeluaran(){

    }

    public void handleRiwayatTransaksi(){

    }



    public int getJumlahPenjualDenganProduk(){
        int total = 0;

        for(Penjual penjual : mainRepository.getDaftarPenjual()){
            if(!penjual.getRepo().getProductList().isEmpty()){
                total++;
            }
        }
        return total;
    }

    public Penjual getPenjualByNamaToko(String namaToko){
        for(Penjual penjual: mainRepository.getDaftarPenjual()){
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

    public boolean cekStokPenjual(){
        boolean stokCukup = true;

        for (CartProduct cartProduct : activePembeli.getCart().getCartContent()){
            Penjual penjual = mainRepository.getPenjualByProductId(cartProduct.getProductId());
            Product product = penjual.getRepo().getProductById(cartProduct.getProductId());

            if(product.getProductStock() < cartProduct.getProductAmount()){
                System.out.printf("Stok produk %s tidak mencukupi!\n", product.getProductName().toLowerCase());
                stokCukup = false;
            }
        }
        return stokCukup;
    }

    public String generateTransaksiId(){
        // Ambil tanggal sekarang
        Date tanggalTransaksi = mainRepository.getDate();

        // Format tanggal
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = formatter.format(tanggalTransaksi);

        // Generate nomorTransaksi
        String nomorTransaksi = String.format("%04d", mainRepository.getTransaksiRepo().getList().size() + 1);

        // Generate ID Transaksi
        return "TRX" + formattedDate + nomorTransaksi;
    }



}
