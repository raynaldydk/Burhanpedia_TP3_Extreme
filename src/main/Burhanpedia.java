package main;

import modelsAdmin.AdminRepository;
import modelsProduct.Product;
import modelsPromotion.Promo;
import modelsPromotion.PromoRepository;
import modelsPromotion.Voucher;
import modelsPromotion.VoucherRepository;
import modelsTransaction.Transaksi;
import modelsTransaction.TransaksiProduct;
import modelsTransaction.TransaksiRepository;
import modelsUser.Penjual;
import modelsUser.User;
import modelsUser.UserRepository;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Burhanpedia {
    private UserRepository userRepo;
    private AdminRepository adminRepo;
    private VoucherRepository voucherRepo;
    private PromoRepository promoRepo;
    private TransaksiRepository transaksiRepo;
    private Date date;

    public Burhanpedia() {
        userRepo = new UserRepository();
        adminRepo = new AdminRepository();
        voucherRepo = new VoucherRepository();
        promoRepo = new PromoRepository();
        transaksiRepo = new TransaksiRepository();
        date = new Date();
    }

    public UserRepository getUserRepo() {
        return userRepo;
    }

    public AdminRepository getAdminRepo() {
        return adminRepo;
    }

    public VoucherRepository getVoucherRepo() {
        return voucherRepo;
    }

    public PromoRepository getPromoRepo() {
        return promoRepo;
    }

    public TransaksiRepository getTransaksiRepo() {
        return transaksiRepo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long calculateTotalTransaksi(String transaksiID){
        double subTotal = calculateSubtotalCart(transaksiID);
        double grandTotal = subTotal;
        boolean validVoucher = true;

        // get instance transaksi
        Transaksi transaksi = this.getTransaksiRepo().getTransaksiById(transaksiID);

        // Jika memakai voucher
        if(this.getVoucherRepo().getById(transaksi.getIdDiskon()) != null){
            Voucher voucherTemp = this.getVoucherRepo().getById(transaksi.getIdDiskon());

            // Cek apakah voucher masih memiliki sisa pemakaian
            if(voucherTemp.getSisaPemakaian() <= 0){
                System.out.println("Sisa pemakaian voucher sudah habis!");
                return -1;
            }

            // Cek apakah voucher masih berlaku
            if(isAfterDateOnly(getDate(), voucherTemp.getBerlakuHingga())){
                System.out.println("Voucher sudah tidak berlaku!");
                return -1;
            }

            // Kalkulasi harga setelah diskon ketika voucher valid
            double diskon = voucherTemp.calculateDisc(voucherTemp.getId());
            grandTotal = subTotal - (subTotal * diskon / 100);
        }

        // Jika memakai promo
        else if (this.getPromoRepo().getById(transaksi.getIdDiskon()) != null){
            Promo promoTemp = this.getPromoRepo().getById(transaksi.getIdDiskon());

            // Cek apakah promo masih berlaku

            if(isAfterDateOnly(getDate(), promoTemp.getBerlakuHingga())){
                System.out.println("Promo sudah tidak berlaku!");
                return -1;
            }

            // Kalkulasi harga setelah diskon ketika promo valid
            int diskon = promoTemp.calculateDisc();
            grandTotal = subTotal - (subTotal * diskon / 100);
        }

        // Tambahkan pajak 3%
        double pajak = 0.03 * grandTotal;
        grandTotal += (long) pajak;

        // Tambahkan total dengan harga ongkir
        grandTotal += transaksi.getBiayaOngkir();

        return (long) grandTotal;
    }

    public long calculateSubtotalCart(String transaksiID){
        // Initialize subtotal
        long subtotal = 0;

        for(TransaksiProduct transaksiProduct : this.getTransaksiRepo().getTransaksiById(transaksiID).getProdukDibeli()){
            Penjual penjual = getPenjualByProductId(transaksiProduct.getProductId());
            Product produk = penjual.getRepo().getProductById(transaksiProduct.getProductId());
            long hargaProduk = produk.getProductPrice();
            int qtyProduk = transaksiProduct.getProductAmount();

            subtotal += hargaProduk * qtyProduk;
        }

        return subtotal;
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

    public ArrayList<Penjual> getDaftarPenjual(){
        ArrayList<Penjual> daftarPenjual = new ArrayList<>();

        for (User user : this.getUserRepo().getAll()){
            if(user.getRole().equals("Penjual")){
                daftarPenjual.add((Penjual) user);
            }
        }

        return daftarPenjual;
    }

    private boolean isAfterDateOnly(Date d1, Date d2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(d1);
        cal1.set(Calendar.HOUR_OF_DAY, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(d2);
        cal2.set(Calendar.HOUR_OF_DAY, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);

        return cal1.after(cal2);
    }


}
