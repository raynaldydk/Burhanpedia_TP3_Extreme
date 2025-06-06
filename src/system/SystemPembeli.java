package system;

import main.Burhanpedia;
import modelsProduct.Cart;
import modelsProduct.CartProduct;
import modelsProduct.Product;
import modelsPromotion.Promo;
import modelsPromotion.Voucher;
import modelsTransaction.TransactionStatus;
import modelsTransaction.Transaksi;
import modelsTransaction.TransaksiProduct;
import modelsUser.Pembeli;
import modelsUser.Penjual;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
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

        // get list penjual
        ArrayList<Penjual> daftarPenjual = mainRepository.getDaftarPenjual();

        // Filter penjuals that have products
        ArrayList<Penjual> penjualDenganProduk = new ArrayList<>();
        for (Penjual penjual : daftarPenjual) {
            if (!penjual.getRepo().getProductList().isEmpty()) {
                penjualDenganProduk.add(penjual);
            }
        }

        System.out.println("========================================");
        for(Penjual penjual : penjualDenganProduk){
            // Print nama toko
            System.out.println(penjual.getRepo().getNamaToko());

            // Print produk
            for(Product product : penjual.getRepo().getProductList()){
                System.out.printf("%-10s %14.2f %5d\n",
                        product.getProductName(),
                        (double) product.getProductPrice(),
                        product.getProductStock()
                );
            }

            // Print dashes line
            if(penjual != penjualDenganProduk.get(penjualDenganProduk.size() - 1)){
                System.out.println("----------------------------------------");
            }
        }
        System.out.println("========================================");
    }

    public void handleTambahToKeranjang(){
        // Input nama toko
        System.out.print("Masukkan toko penjual yang ingin dibeli: ");
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
            System.out.printf("%-10s %2.2f %4d (%.2f)\n", namaProduk, hargaProduk, qtyProduk, totalHarga);
        }
        System.out.println("----------------------------------------");
        System.out.printf("Subtotal:   %.2f\n", subtotal);
        System.out.println("========================================");

        // Konfirmasi produk yang di checkout
        System.out.print("Apakah anda yakin dengan produknya? (Y/N): ");
        String konfirmasiProduk = input.nextLine();

        if (konfirmasiProduk.equalsIgnoreCase("n")) {
            System.out.println("Checkout dibatalkan!");
            return;
        } else if (!konfirmasiProduk.equalsIgnoreCase("y")) {
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

        // Get total harga setelah diskon
        applyDiskon(diskonId, (long) subtotal);

        // Pilih opsi pengiriman
        System.out.println("Pilih opsi pengiriman: ");
        System.out.println("1. Instant  (20.000)");
        System.out.println("2. Next Day  (15.000)");
        System.out.println("3. Reguler (10.000)");
        System.out.print("Pilihan pengiriman: ");
        String pilihanOngkir = input.nextLine();

        // Get jenis transaksi & biaya ongkir
        String jenisTransaksi;
        long biayaOngkir;

        switch (pilihanOngkir) {
            case "1" -> {
                jenisTransaksi = "Instant";
                biayaOngkir = 20000;
            }
            case "2" -> {
                jenisTransaksi = "Next Day";
                biayaOngkir = 15000;
            }
            case "3" -> {
                jenisTransaksi = "Reguler";
                biayaOngkir = 10000;
            }
            default -> {
                System.out.println("Input yang anda masukkan salah!");
                return;
            }
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
            Date date = mainRepository.getDate();
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
        ArrayList<Transaksi> transaksiList = getTransaksiListByPembeli();

        if(transaksiList.isEmpty()){
            System.out.println("===================================");
            System.out.println("Tidak ada barang yang bisa dilacak!");
            System.out.println("===================================");
            return;
        }

        System.out.println("===================================");

        for(Transaksi transaksi : transaksiList){
            // Get data
            String transaksiId = transaksi.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            String tanggal = formatter.format(transaksi.getHistoryStatus().get(0).getTimestamp());
            String namaToko = transaksi.getNamePenjual();
            String status = transaksi.getCurrentStatus();

            // Print output
            System.out.printf("%-16s %s\n", "ID Transaksi", transaksiId);
            System.out.printf("%-16s %s\n", "Tanggal", tanggal);
            System.out.printf("%-16s %s\n", "Toko", namaToko);
            System.out.printf("%-16s %s\n", "Status", status);

            if(transaksi.getNamePengirim() != null){
                if(!transaksi.getNamePengirim().isEmpty()){
                    String pengirim = transaksi.getNamePengirim();
                    System.out.printf("%-16s %s\n", "Pengirim", pengirim);
                }
            }

            System.out.println("===================================");
        }
    }

    public void handleLaporanPengeluaran(){
        ArrayList<Transaksi> transaksiList = getTransaksiListByPembeli();

        if(transaksiList.isEmpty()){
            System.out.println("=================================");
            System.out.println("Laporan pengeluaran masih kosong!");
            System.out.println("=================================");
            return;
        }

        // Grand total
        double grandTotal = 0.0;

        for(Transaksi transaksi : transaksiList){
            // Get data transaksi
            String transaksiId = transaksi.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            String tanggal = formatter.format(transaksi.getHistoryStatus().get(0).getTimestamp());
            Penjual penjual = mainRepository.getPenjualByProductId(transaksi.getProdukDibeli().get(0).getProductId());

            // Header
            System.out.println("===== LAPORAN PENGELUARAN =====");
            System.out.printf("%-16s %s\n", "ID Transaksi", transaksiId);
            System.out.printf("%-16s %s\n", "Tanggal", tanggal);
            System.out.println("-------------------------------");

            // Inisialisasi subtotal
            double subtotal = 0;

            // Items
            for (TransaksiProduct productDibeli : transaksi.getProdukDibeli()) {
                Product product = penjual.getRepo().getProductById(productDibeli.getProductId());
                String namaProduk = product.getProductName();
                double hargaProduk = product.getProductPrice();
                int jumlahProduk = productDibeli.getProductAmount();
                double totalHargaProduk = hargaProduk * jumlahProduk;

                System.out.printf("%-10s %11.2f %4d (%.2f)\n",
                        namaProduk,
                        hargaProduk,
                        jumlahProduk,
                        totalHargaProduk
                );

                subtotal += totalHargaProduk;
            }

            // Subtotal, diskon, pajak, ongkir
            double diskon = getTotalAfterDiskon(transaksi.getIdDiskon(), subtotal);
            double totalSetelahDiskon = subtotal - diskon;
            double pajak = 0.03 * totalSetelahDiskon;
            double biayaOngkir = transaksi.getBiayaOngkir();

            System.out.println("-------------------------------");
            System.out.printf("%-10s %11.2f\n", "Subtotal", subtotal);
            System.out.printf("%-10s %11.2f\n", "Diskon", diskon);
            System.out.printf("%-10s %11.2f\n", "Pajak (3%)", pajak);
            System.out.printf("%-10s %11.2f\n", "Pengiriman",  biayaOngkir);
            System.out.println("-------------------------------");

            // Total
            double total = totalSetelahDiskon + pajak + biayaOngkir;
            System.out.printf("%-10s %11.2f\n", "Total", total);
            System.out.println("===============================");

            // Tambah ke grand total
            grandTotal += total;
        }

        System.out.printf("Total Keseluruhan: %.2f\n", grandTotal);
    }

    public void handleRiwayatTransaksi(){
        ArrayList<Transaksi> transaksiList = getTransaksiListByPembeli();

        if(transaksiList.isEmpty()){
            System.out.println("=================================");
            System.out.println("Riwayat transaksi masih kosong!");
            System.out.println("=================================");
            return;
        }

        System.out.println("===================== RIWAYAT TRANSAKSI =====================");
        System.out.println("ID Transaksi     Tanggal        Nominal      Keterangan");
        System.out.println("-------------------------------------------------------------");

        for(Transaksi transaksi : transaksiList){
            // Get data transaksi
            String transaksiId = transaksi.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("d MMMM yyyy", new Locale("id", "ID"));
            String tanggal = formatter.format(transaksi.getHistoryStatus().get(0).getTimestamp());
            double nominal = mainRepository.calculateTotalTransaksiWithoutDiskonCheck(transaksiId);

            // Print
            if(transaksi.getCurrentStatus().equalsIgnoreCase("Dikembalikan")){
                System.out.printf("%s  %s  + %.2f  %s\n", transaksiId, tanggal, nominal, "Dikembalikan");
            }
            else{
                System.out.printf("%s  %s  - %.2f  %s\n", transaksiId, tanggal, nominal, "Pembelian produk");
            }
        }

        System.out.println("=============================================================");
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

    public void applyDiskon(String diskonID, long subTotal) {

        if(mainRepository.getVoucherRepo().getById(diskonID) != null){
            Voucher voucher = mainRepository.getVoucherRepo().getById(diskonID);
            int diskon = voucher.calculateDisc(diskonID);
            long total = subTotal - (subTotal * diskon / 100);
            System.out.printf("Voucher diterapkan! Total harga setelah diskon: %d\n", total);
        }

        else if(mainRepository.getPromoRepo().getById(diskonID) != null){
            Promo promo = mainRepository.getPromoRepo().getById(diskonID);
            int diskon = promo.calculateDisc();
            long total = subTotal - (subTotal * diskon / 100);
            System.out.printf("Promo diterapkan! Total harga setelah diskon: %d\n", total);
        }

    }

    public double getTotalAfterDiskon(String diskonID, double subTotal) {
        if(mainRepository.getVoucherRepo().getById(diskonID) != null){
            Voucher voucher = mainRepository.getVoucherRepo().getById(diskonID);
            double diskon = voucher.calculateDisc(diskonID);
            return diskon / 100 * subTotal;
        }

        else if(mainRepository.getPromoRepo().getById(diskonID) != null){
            Promo promo = mainRepository.getPromoRepo().getById(diskonID);
            double diskon = promo.calculateDisc();
            return diskon / 100 * subTotal;
        }
        return subTotal;
    }

    public ArrayList<Transaksi> getTransaksiListByPembeli(){
        ArrayList<Transaksi> transaksiList = new ArrayList<>();

        for(Transaksi transaksi : mainRepository.getTransaksiRepo().getList()){
            if(transaksi.getNamePembeli().equalsIgnoreCase(activePembeli.getUsername())){
                transaksiList.add(transaksi);
            }
        }
        return transaksiList;
    }



}
