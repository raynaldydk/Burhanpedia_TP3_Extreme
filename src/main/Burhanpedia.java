package main;

import modelsAdmin.AdminRepository;
import modelsPromotion.PromoRepository;
import modelsPromotion.VoucherRepository;
import modelsTransaction.TransaksiRepository;
import modelsUser.UserRepository;

import java.util.Date;

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

    public long calculateTotalTransaksi(String idTransaksi) {
        // TODO
        return 0;
    }
}
