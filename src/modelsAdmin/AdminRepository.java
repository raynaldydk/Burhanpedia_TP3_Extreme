package modelsAdmin;

import java.util.ArrayList;

public class AdminRepository {
    private ArrayList<Admin> adminList;

    public AdminRepository() {
        adminList = new ArrayList<>();
        adminList.add(new Admin("admin", "admin"));
        adminList.add(new Admin("root", "toor"));
        adminList.add(new Admin("dekdepe", "aku_CinTaJaVa"));
    }
}
