package system;

import main.Burhanpedia;
import modelsAdmin.Admin;
import modelsPromotion.Voucher;

import java.text.SimpleDateFormat;
import java.util.Scanner;

public class SystemAdmin implements SystemMenu {
    private Admin activeAdmin;
    private Scanner input;
    private Burhanpedia mainRepository;

    public SystemAdmin(Admin activeAdmin, Burhanpedia mainRepository) {
        this.activeAdmin = activeAdmin;
        this.input = new Scanner(System.in);
        this.mainRepository = mainRepository;
    }

    @Override
    public String showMenu() {
        return """
                
                ===== MENU ADMIN =====
                1. Generate Voucher
                2. Generate Promo
                3. Lihat Voucher
                4. Lihat Promo
                5. Kembali ke Menu Utama
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
                    handleGenerateVoucher();
                    break;
                case "2":
                    handleGeneratePromo();
                    break;
                case "3":
                    handleLihatVoucher();
                    break;
                case "4":
                    handleLihatPromo();
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Input yang anda masukkan salah!");
                    break;
            }
        }while(!pilihan.equals("5"));
    }

    public void handleGenerateVoucher(){
        // Input tanggal berlaku
        String tanggalBerlaku;
        System.out.print("Voucher berlaku hingga: ");
        tanggalBerlaku = input.nextLine();

        // Generate voucher
        mainRepository.getVoucherRepo().generate(tanggalBerlaku);
    }

    public void handleGeneratePromo(){

    }

    public void handleLihatVoucher(){
        // Cek apakah ada voucher di repo
        if(mainRepository.getVoucherRepo().getAll().isEmpty()){
            System.out.println("""
                    ==============================
                    Belum ada voucher yang dibuat!
                    ==============================
                    """);
            return;
        }

        // Apabila ada voucher
        System.out.println("============================================================");
        for(Voucher voucher : mainRepository.getVoucherRepo().getAll()){

            // Format tanggal
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String tanggalBerlaku = dateFormat.format(voucher.getBerlakuHingga());

            // Print voucher list
            System.out.printf("%s [Dapat digunakan %d kali] [Sampai dengan %s]\n",
                    voucher.getId(),
                    voucher.getSisaPemakaian(),
                    tanggalBerlaku
            );
        }
        System.out.println("============================================================");
    }

    public void handleLihatPromo(){

    }
}
