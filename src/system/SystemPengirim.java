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
        return "";
    }

    @Override
    public void handleMenu() {

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
