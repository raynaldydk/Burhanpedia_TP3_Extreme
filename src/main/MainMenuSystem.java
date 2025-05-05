package main;

import modelsUser.Pembeli;
import modelsUser.Pengirim;
import modelsUser.Penjual;
import modelsUser.User;
import system.*;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenuSystem implements SystemMenu {
    private SystemPembeli systemPembeli;
    private SystemPenjual systemPenjual;
    private SystemPengirim systemPengirim;
    private SystemAdmin systemAdmin;
    private Scanner input;
    private Burhanpedia mainRepository;

    public MainMenuSystem() {
        this.input = new Scanner(System.in);
        this.mainRepository = new Burhanpedia();
    }

    public static void main(String[] args) {
        MainMenuSystem mainMenuSystem = new MainMenuSystem();
        mainMenuSystem.showHeader();
        mainMenuSystem.handleMenu();
    }

    @Override
    public String showMenu() {
        return """
                
                Pilih menu
                1. Login
                2. Register
                3. Hari Selanjutnya
                4. Keluar
                """;

    }

    @Override
    public void handleMenu() {
        String pilihan;
        do{
            System.out.println(this.showMenu());
            System.out.print("\bPerintah : ");
            pilihan = input.nextLine();
            switch (pilihan){
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegister();
                    break;
                case "3":
                    handleNextDay();
                    break;
                case "4":
                    handleExit();
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
                    break;
            }
        }while(!pilihan.equals("4"));
    }

    public void handleLogin(){
        System.out.println("\n===== LOGIN =====");
        System.out.print("Masukkan username: ");
        String username = input.nextLine();

        // Cek apakah username ada pada userRepo
        if(mainRepository.getUserRepo().getUserByName(username) == null) {
            System.out.printf("Tidak ada user dengan nama %s\n", username);
            return;
        }

        System.out.print("Masukkan password: ");
        String password = input.nextLine();

        // Cek login
        if(!mainRepository.getUserRepo().login(username, password)){
            System.out.println("Password salah!");
            return;
        }

        String pilihan;
        do{
            System.out.println("\nPilih opsi login:");
            System.out.println("1. Penjual");
            System.out.println("2. Pembeli");
            System.out.println("3. Pengirim");
            System.out.println("4. Cek Saldo Antar Role");
            System.out.println("5. Batal Login");
            System.out.print("\nPerintah: ");
            pilihan = input.nextLine();

            switch (pilihan){
                case "1":
                    handleLoginPenjual(username);
                    break;
                case "2":
                    handleLoginPembeli(username);
                    break;
                case "3":
                    handleLoginPengirim(username);
                    break;
                case "4":
                    handleCekSaldoAntarAkun(username);
                    break;
                case "5":
                    System.out.println("Login dibatalkan, kembali ke menu utama...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
                    break;
            }
        }while(!pilihan.equals("5"));

    }

    public void handleRegister(){
        System.out.println("\n===== REGISTRASI =====");
        System.out.print("Masukkan username: ");
        String username = input.nextLine();
        String password;

        // Cek apakah sudah ada akun dengan username tersebut
        if(mainRepository.getUserRepo().getUserByName(username) != null){
            // Cek apakah user sudah memiliki semua role
            ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);
            if(userRoles.size() == 3){
                System.out.printf("Username %s tidak dapat menambahkan role lagi karena " +
                        "sudah memiliki semua role, registrasi dibatalkan.\n", username.toLowerCase());
                return;
            }

            System.out.println("Username sudah ada! Silahkan konfirmasi password untuk menambahkan role lain.");

            System.out.print("Masukkan password: ");
            password = input.nextLine();

            // Cek login
            if(!mainRepository.getUserRepo().login(username, password)){
                System.out.println("Password salah!");
                return;
            }
        }

        // Akun baru
        else{
            System.out.print("Masukkan password: ");
            password = input.nextLine();
        }

        // Pilih role
        String pilihan;
        do{
            System.out.println("\nPilih role:");
            System.out.println("1. Penjual");
            System.out.println("2. Pembeli");
            System.out.println("3. Pengirim");
            System.out.println("4. Batalkan register");
            System.out.print("\nPerintah: ");

            pilihan = input.nextLine();
            switch (pilihan){
                case "1":
                    handleRegisterPenjual(username, password);
                    return;
                case "2":
                    handleRegisterPembeli(username, password);
                    return;
                case "3":
                    handleRegisterPengirim(username, password);
                    return;
                case "4":
                    System.out.println("Registrasi dibatalkan, kembali ke menu utama...");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
                    break;
            }
        }while(!pilihan.equals("4"));
    }

    public void handleNextDay(){

    }

    public void handleCekSaldoAntarAkun(String username){
        StringBuilder sb = new StringBuilder();
        boolean found = false;

        sb.append("\nRole      | Saldo\n");
        sb.append("=========================\n");

        for (User user : mainRepository.getUserRepo().getAll()) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                found = true;
                String role = user.getRole();
                double saldo = user.getBalance();
                sb.append(String.format("%-10s| %.2f\n", role, saldo));
            }
        }

        sb.append("=========================\n");

        if (!found) {
            System.out.printf("Username %s tidak ditemukan di sistem.\n", username);
        }

        System.out.println(sb);
    }

    public void showHeader(){
        System.out.println("\n=============================================================");
        System.out.println("  ____             _                 _____         _ _       ");
        System.out.println(" |  _ \\           | |               |  __ \\       | (_)      ");
        System.out.println(" | |_) |_   _ _ __| |__   __ _ _ __ | |__) |__  __| |_  __ _ ");
        System.out.println(" |  _ <| | | | '__| '_ \\ / _` | '_ \\|  ___/ _ \\/ _` | |/ _` |");
        System.out.println(" | |_) | |_| | |  | | | | (_| | | | | |  |  __/ (_| | | (_| |");
        System.out.println(" |____/ \\__,_|_|  |_| |_|\\__,_|_| |_|_|   \\___|\\__,_|_|\\__,_|");
        System.out.println("=============================================================");
        System.out.println("============== Selamat datang di Burhanpedia! ===============");
        System.out.println("=============================================================");
    }

    public void handleExit(){
        System.out.println("\n===========================================");
        System.out.println("Terima kasih telah menggunakan Burhanpedia!");
        System.out.println("===========================================\n");
    }

    public void handleRegisterPenjual(String username, String password){
        // Cek apakah user sudah memiliki role penjual
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);
        if(userRoles.contains("Penjual")){
            System.out.println("Role sudah ada. Silahkan pilih role lain!");
            return;
        }

        // Input nama toko
        System.out.print("Masukkan nama toko: ");
        String namaToko = this.input.nextLine();

        // Buat instance baru penjual
        Penjual penjual = new Penjual(username, password, "Penjual", namaToko);

        // Simpan akun ke dalam userRepo
        mainRepository.getUserRepo().getAll().add(penjual);

        System.out.println("Registrasi akun penjual berhasil!");
    }

    public void handleRegisterPembeli(String username, String password){
        // Cek apakah user sudah memiliki role pembeli
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);
        if(userRoles.contains("Pembeli")){
            System.out.println("Role sudah ada. Silahkan pilih role lain!");
            return;
        }

        // Buat instance baru pembeli
        Pembeli pembeli = new Pembeli(username, password, "Pembeli");

        // Simpan akun ke dalam userRepo
        mainRepository.getUserRepo().getAll().add(pembeli);

        System.out.println("Registrasi akun pembeli berhasil!");
    }

    public void handleRegisterPengirim(String username, String password){
        // Cek apakah user sudah memiliki role pembeli
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);
        if(userRoles.contains("Pengirim")){
            System.out.println("Role sudah ada. Silahkan pilih role lain!");
            return;
        }

        // Buat instance baru pengirim
        Pengirim pengirim = new Pengirim(username, password, "Pengirim");

        // Simpan akun ke dalam userRepo
        mainRepository.getUserRepo().getAll().add(pengirim);

        System.out.println("Registrasi akun pengirim berhasil!");
    }

    public void handleLoginPenjual(String username){
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);

        if(userRoles.contains("Penjual")){
            for(User user : mainRepository.getUserRepo().getAll()){
                if(user.getUsername().equalsIgnoreCase(username) && user.getRole().equals("Penjual")){
                    System.out.printf("Login berhasil! Selamat datang %s!\n", username);
                    Penjual penjual = (Penjual) user;
                    systemPenjual = new SystemPenjual(penjual, mainRepository);
                }
            }
        }

        else{
            System.out.printf("Username %s tidak memiliki role penjual!\n", username.toLowerCase());
        }
    }

    public void handleLoginPembeli(String username){
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);

        if(userRoles.contains("Pembeli")){
            for(User user : mainRepository.getUserRepo().getAll()){
                if(user.getUsername().equalsIgnoreCase(username) && user.getRole().equals("Pembeli")){
                    System.out.printf("Login berhasil! Selamat datang %s!\n", username);
                    Pembeli pembeli = (Pembeli) user;
                    systemPembeli = new SystemPembeli(pembeli, mainRepository);
                }
            }
        }

        else{
            System.out.printf("Username %s tidak memiliki role pembeli!\n", username.toLowerCase());
        }
    }

    public void handleLoginPengirim(String username){
        ArrayList<String> userRoles = mainRepository.getUserRepo().getUserRoles(username);

        if(userRoles.contains("Pengirim")){
            for(User user : mainRepository.getUserRepo().getAll()){
                if(user.getUsername().equalsIgnoreCase(username) && user.getRole().equals("Pengirim")){
                    System.out.printf("Login berhasil! Selamat datang %s!\n", username);
                    Pengirim pengirim = (Pengirim) user;
                    systemPengirim = new SystemPengirim(pengirim, mainRepository);
                }
            }
        }

        else{
            System.out.printf("Username %s tidak memiliki role pengirim!\n", username.toLowerCase());
        }
    }
}