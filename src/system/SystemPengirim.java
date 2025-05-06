package system;

import main.Burhanpedia;
import modelsTransaction.Transaksi;
import modelsUser.Pengirim;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;
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
                    System.out.println("Input yang anda masukkan salah!");
                    break;
            }
        }while(!inputMenu.equals("5"));

    }

    public void handleFindJob(){
        // Get job list pengirim
        ArrayList<Transaksi> jobList = getJobList();

        // Cek apakah ada job yang bisa diambil
        if(jobList.isEmpty()){
            System.out.println("===============================");
            System.out.println("Tidak ada job yang bisa diambil");
            System.out.println("===============================");
            return;
        }

        System.out.println("===============================");
        for(Transaksi transaksi : jobList){
            // Get data
            String transaksiId = transaksi.getId();
            String namaPembeli = transaksi.getNamePembeli();
            String namaPenjual = transaksi.getNamePenjual();

            // Print data
            System.out.println("Pesanan tersedia:");
            System.out.println("ID: " + transaksiId);
            System.out.println("Pembeli: " + namaPembeli);
            System.out.println("Penjual: " + namaPenjual);

            // Dashed line pemisah
            if(transaksi != jobList.get(jobList.size()-1)){
                System.out.println("-------------------------------");
            }
        }
        System.out.println("===============================");
    }

    public void handleTakeJob(){
        // TODO
    }

    public void handleConfirmJob(){
        // TODO
    }

    public void handleRiwayatTransaksi(){
        // TODO
    }

    public ArrayList<Transaksi> getJobList(){
        ArrayList<Transaksi> jobList = new ArrayList<>();

        for(Transaksi transaksi : mainRepository.getTransaksiRepo().getList()){
            // Get transaksi current status
            String statusTransaksi = transaksi.getCurrentStatus();

            // Tambahkan ke joblist jika status = sedang dikemas atau menunggu pengirim
            if(statusTransaksi.equalsIgnoreCase("Sedang Dikemas") || statusTransaksi.equalsIgnoreCase("Menunggu Pengirim")){
                jobList.add(transaksi);
            }
        }

        return jobList;
    }

}
