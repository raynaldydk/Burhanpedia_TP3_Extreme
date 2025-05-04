package system;

import main.Burhanpedia;
import modelsAdmin.Admin;

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
        return "";
    }

    @Override
    public void handleMenu() {

    }

    public void handleGenerateVoucher(){

    }

    public void handleGeneratePromo(){

    }

    public void handleLihatVoucher(){

    }

    public void handleLihatPromo(){

    }
}
