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

    public boolean login(String username, String password) {
        for (Admin admin : adminList) {
            if(admin.getUsername().equals(username) && admin.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }

    public Admin getAdminByUsername(String username) {
        for (Admin admin : adminList) {
            if(admin.getUsername().equals(username)){
                return admin;
            }
        }
        return null;
    }
}
