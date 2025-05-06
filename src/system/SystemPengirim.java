package system;

import main.Burhanpedia;
import modelsTransaction.TransactionStatus;
import modelsTransaction.Transaksi;
import modelsUser.Pengirim;
import modelsUser.Penjual;

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
            System.out.println("Tidak ada pesanan untuk ID tersebut.");
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
            // Tambahkan status baru
            mainRepository.getTransaksiRepo().prosesTransaksi(idTransaksi);

            // Simpan nama pengirim
            transaksiDiambil.setNamePengirim(activePengirim.getUsername());
            System.out.printf("Pesanan berhasil diambil oleh %s.\n", activePengirim.getUsername());
        }
        else{
            System.out.println("Pesanan sudah melewati tanggal pengiriman!");
        }
    }

    public void handleConfirmJob(){
        // Input transaksi ID yang ingin di confirm
        System.out.print("Masukkan ID transaksi yang ingin dikonfirmasi: ");
        String idTransaksi = input.nextLine();

        // Cek apakah ID transaksi valid
        if(mainRepository.getTransaksiRepo().getTransaksiById(idTransaksi) == null){
            System.out.println("Tidak ada pesanan untuk ID tersebut.");
            return;
        }

        // Get job list yang di take pengirim
        ArrayList<Transaksi> jobList = getJobListByPengirim();

        // Get transaksi yang dimaksud
        Transaksi transaksiPengirim = null;
        for(Transaksi transaksi : jobList){
            if(transaksi.getId().equalsIgnoreCase(idTransaksi) && transaksi.getCurrentStatus().equalsIgnoreCase("Sedang Dikirim")){
                transaksiPengirim = transaksi;
            }
        }

        // Cek apakah transaksi merupakan job pengirim
        if(transaksiPengirim == null){
            System.out.println("Tidak ada pesanan untuk ID tersebut.");
            return;
        }

        // Proses jika job valid
        String jenisTransaksi = transaksiPengirim.getJenisTransaksi();
        Date tanggalSekarangDate = mainRepository.getDate();
        Date tanggalDikirimDate = transaksiPengirim.getHistoryStatus().get(0).getTimestamp();

        LocalDate tanggalSekarang = tanggalSekarangDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        LocalDate tanggalDikirim = tanggalDikirimDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();

        boolean dapatDiselesaikan = false;

        if (jenisTransaksi.equalsIgnoreCase("Instant")) {
            // Hanya bisa diambil di hari yang sama dengan tanggal dikirim
            dapatDiselesaikan = tanggalSekarang.isEqual(tanggalDikirim);
        } else if (jenisTransaksi.equalsIgnoreCase("Next Day")) {
            long selisih = ChronoUnit.DAYS.between(tanggalDikirim, tanggalSekarang);
            dapatDiselesaikan = selisih >= 0 && selisih <= 1;
        } else { // Reguler
            long selisih = ChronoUnit.DAYS.between(tanggalDikirim, tanggalSekarang);
            dapatDiselesaikan = selisih >= 0 && selisih <= 2;
        }

        if(dapatDiselesaikan){
            // Transaksi diselesaikan
            mainRepository.getTransaksiRepo().prosesTransaksi(idTransaksi);
            System.out.printf("Pesanan berhasil diselesaikan oleh %s.\n", activePengirim.getUsername());

            // Tambah balance pengirim
            activePengirim.setBalance(activePengirim.getBalance() + transaksiPengirim.getBiayaOngkir());

            // Tambah balance penjual
            Penjual penjual = null;

            for(Penjual penjualRepo : mainRepository.getDaftarPenjual()){
                if(penjualRepo.getRepo().getNamaToko().equalsIgnoreCase(transaksiPengirim.getNamePenjual())){
                    penjual = penjualRepo;
                }
            }

            if(penjual != null){
                long totalTransaksi = mainRepository.calculateSubtotalCart(transaksiPengirim.getId());
                totalTransaksi -= transaksiPengirim.getBiayaOngkir();

                penjual.setBalance(penjual.getBalance() + totalTransaksi);
            }
        }
        else{
            System.out.println("Pesanan sudah melewati tanggal pengiriman!");
        }

    }

    public void handleRiwayatTransaksi(){
        // Get job list pengirim
        ArrayList<Transaksi> jobList = getJobListByPengirim();

        // Cek apakah ada job yang sudah diambil
        if (jobList.isEmpty()) {
            System.out.println("Belum ada job yang diambil!");
            return;
        }

        System.out.println("==============================");
        for (Transaksi transaksi : jobList) {
            // get data
            String transaksiId = transaksi.getId();
            SimpleDateFormat formatter = new SimpleDateFormat("EEEE, d MMMM yyyy", new Locale("id", "ID"));
            String tanggal = formatter.format(transaksi.getHistoryStatus().get(2).getTimestamp());
            long pendapatan = transaksi.getBiayaOngkir();
            String namaToko = transaksi.getNamePenjual();

            // print data
            System.out.printf("%-16s %s\n", "ID Transaksi", transaksiId);
            System.out.printf("%-16s %s\n", "Tanggal", tanggal);
            System.out.printf("%-16s %s\n", "Toko", namaToko);
            System.out.printf("%-16s %d\n", "Pendapatan", pendapatan);
            System.out.println("==============================");
        }

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

    public ArrayList<Transaksi> getJobListByPengirim(){
        ArrayList<Transaksi> jobList = new ArrayList<>();

        for(Transaksi transaksi : mainRepository.getTransaksiRepo().getList()){
            if(transaksi.getNamePengirim().equalsIgnoreCase(activePengirim.getUsername())){
                jobList.add(transaksi);
            }
        }
        return jobList;
    }

}
