package system;

import main.Burhanpedia;
import modelsAdmin.Admin;
import modelsPromotion.Promo;
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
        // Input tanggal berlaku
        String tanggalBerlaku;
        System.out.print("Promo berlaku hingga: ");
        tanggalBerlaku = input.nextLine();

        // Generate promo
        mainRepository.getPromoRepo().generate(tanggalBerlaku);
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

        // Show menu voucher
        String pilihanMenuVoucher;

        do{
            System.out.println("\n===== MENU LIHAT VOUCHER =======");
            System.out.println("1. Lihat Semua");
            System.out.println("2. Lihat Berdasarkan ID");
            System.out.println("3. Kembali");
            System.out.println(" ");
            System.out.print("Perintah: ");

            pilihanMenuVoucher = input.nextLine();

            switch (pilihanMenuVoucher){
                case "1":
                    showVoucher();
                    break;
                case "2":
                    showVoucherById();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Input yang anda masukkan salah!");
                    break;
            }
        }while(!pilihanMenuVoucher.equals("3"));

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
        // Cek apakah ada promo di repo
        if (mainRepository.getPromoRepo().getAll().isEmpty()){
            System.out.println("""
                    ==============================
                    Belum ada promo yang dibuat!
                    ==============================
                    """
            );
            return;
        }

        // Show menu promo
        String pilihanMenuPromo;

        do{
            System.out.println("\n===== MENU LIHAT PROMO =======");
            System.out.println("1. Lihat Semua");
            System.out.println("2. Lihat Berdasarkan ID");
            System.out.println("3. Kembali");
            System.out.print("\nPerintah: ");
            pilihanMenuPromo = input.nextLine();

            switch (pilihanMenuPromo){
                case "1":
                    showPromo();
                    break;
                case "2":
                    showPromoById();
                    break;
                case "3":
                    break;
                default:
                    System.out.println("Input yang anda masukkan salah!");
                    break;
            }
        }while(!pilihanMenuPromo.equals("3"));
    }

    public void showVoucher(){
        System.out.println("============================================================");
        for(Voucher voucher : mainRepository.getVoucherRepo().getAll()){
            // Format tanggal
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(voucher.getBerlakuHingga());

            // Print voucher
            System.out.printf("%s [Dapat digunakan %d kali] [Sampai dengan %s]\n",
                    voucher.getId(),
                    voucher.getSisaPemakaian(),
                    formattedDate
            );
        }
        System.out.println("============================================================");
    }

    public void showPromo(){
        System.out.println("============================================================");
        for(Promo promo : mainRepository.getPromoRepo().getAll()){
            // Parse tanggal menjadi string
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(promo.getBerlakuHingga());

            // Print promo
            System.out.printf("%s [Sampai dengan %s]\n", promo.getId(), formattedDate);
        }
        System.out.println("============================================================");
    }

    public void showPromoById(){
        // Masukkan id promo yang ingin di cari
        System.out.print("Masukkan id promo: ");
        String inputPromo = input.nextLine();

        // Cek apakah promo yang di input ada pada repository
        if(mainRepository.getPromoRepo().getById(inputPromo) != null){
            Promo promoTemp = mainRepository.getPromoRepo().getById(inputPromo);

            // Format the date from Date object to "dd/MM/yyyy" format
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(promoTemp.getBerlakuHingga());

            // print promo
            System.out.println("============================================================");
            System.out.printf("%s [Sampai dengan %s]\n", promoTemp.getId(), formattedDate);
            System.out.println("============================================================");
        }

        // Jika promo tidak ada pada repo
        else{
            System.out.println("============================================================");
            System.out.printf("Tidak ada promo dengan id %s\n", inputPromo);
            System.out.println("============================================================");
        }
    }

    public void showVoucherById(){
        // Masukkan id voucher yang ingin dicari
        System.out.print("Masukkan id voucher: ");
        String inputVoucher = input.nextLine();

        // Cek apakah voucher ada di repository
        if(mainRepository.getVoucherRepo().getById(inputVoucher) != null){
            Voucher voucherTemp = mainRepository.getVoucherRepo().getById(inputVoucher);

            // Format tanggal
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String formattedDate = dateFormat.format(voucherTemp.getBerlakuHingga());

            // Print voucher
            System.out.printf("%s [Dapat digunakan %d kali] [Sampai dengan %s]\n",
                    voucherTemp.getId(),
                    voucherTemp.getSisaPemakaian(),
                    formattedDate
            );
        }

        else{
            System.out.println("===========================================================");
            System.out.printf("Tidak ada voucher dengan id %s\n", inputVoucher);
            System.out.println("===========================================================");
        }
    }
}
