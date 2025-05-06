package system;

import main.Burhanpedia;
import modelsTransaction.Transaksi;
import modelsUser.Pengirim;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
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
        // Input id transaksi yang ingin diambil
        System.out.print("Masukkan ID Transaksi yang ingin diambil: ");
        String idTransaksi = input.nextLine();

        // Get transaksi yang ingin diambil
        Transaksi transaksiDiambil = null;
        ArrayList<Transaksi> jobList = getJobList();
        for(Transaksi transaksi : jobList){
            if(transaksi.getId().equals(idTransaksi)){
                transaksiDiambil = transaksi;
            }
        }

        // Cek apakah id transaksi valid
        if(transaksiDiambil == null){
            System.out.println("Tidak ada transaksi dengan id tersebut!");
            return;
        }

        // Cek apakah job bisa diambil
        if(!transaksiDiambil.getCurrentStatus().equalsIgnoreCase("Menunggu Pengirim")){
            System.out.println("Tidak dapat mengambil pesanan ini.");
            return;
        }

        // Cek batas tanggal pengiriman
        Date tanggalSekarang = mainRepository.getDate();
        Date tanggalDikirim = transaksiDiambil.getHistoryStatus().get(1).getTimestamp();
        String jenisTransaksi = transaksiDiambil.getJenisTransaksi();
        LocalDate tanggalSekarangLD = tanggalSekarang.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate tanggalDikirimLD = tanggalDikirim.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        boolean dapatDiambil = false;

        // Instant
        if(jenisTransaksi.equalsIgnoreCase("Instant")){
            dapatDiambil = tanggalSekarangLD.isEqual(tanggalDikirimLD);
        }

        // Next Day
        else if(jenisTransaksi.equalsIgnoreCase("Next Day")){
            long selisih = ChronoUnit.DAYS.between(tanggalDikirimLD, tanggalSekarangLD);
            dapatDiambil = selisih >= 0 && selisih <= 1;
        }

        // Reguler
        else{
            long selisih = ChronoUnit.DAYS.between(tanggalDikirimLD, tanggalSekarangLD);
            dapatDiambil = selisih >= 0 && selisih <= 2;
        }

        // Jika job bisa diambil
        if(dapatDiambil){
            transaksiDiambil.setNamePengirim(activePengirim.getUsername());
            System.out.printf("Pesanan berhasil diambil oleh %s.\n", activePengirim.getUsername());
        }
        else{
            System.out.println("Tidak dapat mengambil pesanan ini.");
        }
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
