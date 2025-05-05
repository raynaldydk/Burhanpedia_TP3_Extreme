package system;

import main.Burhanpedia;
import modelsUser.Pengirim;

import java.util.Scanner;

public class SystemPengirim implements SystemMenu {
    private Pengirim activePengirim;
    private Scanner input;
    private Burhanpedia mainRepository;

    public SystemPengirim(Pengirim activePengirim, Burhanpedia mainRepository) {
        this.activePengirim = activePengirim;
        this.input = new Scanner(System.in);
        this.mainRepository = mainRepository;
    }

    @Override
    public String showMenu() {
        return """
                
                ===== MENU PENGIRIM =====
                1. Find Job
                2. Take Job
                3. Confirm Job
                4. Lihat Riwayat Transaksi
                5. Kembali ke Menu Utama
                """;
    }

    @Override
    public void handleMenu() {
        String inputMenu;

        do{
            System.out.println(showMenu());
            System.out.print("Perintah: ");
            inputMenu = input.nextLine();
            switch(inputMenu){
                case "1":
                    handleFindJob();
                    break;
                case "2":
                    handleTakeJob();
                    break;
                case "3":
                    handleConfirmJob();
                    break;
                case "4":
                    handleRiwayatTransaksi();
                    break;
                case "5":
                    break;
                default:
                    System.out.println("Invalid input!");
                    break;
            }
        }while(!inputMenu.equals("5"));

    }

    public void handleFindJob(){

    }

    public void handleTakeJob(){

    }

    public void handleConfirmJob(){

    }

    public void handleRiwayatTransaksi(){

    }

}
