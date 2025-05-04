package main;

import system.*;

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

    }

    @Override
    public String showMenu() {
        return "";
    }

    @Override
    public void handleMenu() {

    }

    public void handleLogin(){

    }

    public void handleRegister(){

    }

    public void handleNextDay(){

    }

    public void handleCekSaldoAntarAkun(String username){

    }
}