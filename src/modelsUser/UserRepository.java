package modelsUser;

import java.util.ArrayList;
import java.util.UUID;

public class UserRepository {
    private ArrayList<User> userList;

    public UserRepository() {
        userList = new ArrayList<>();
    }

    public ArrayList<String> getUserRoles(String username) {
        ArrayList<String> userRoles = new ArrayList<>();

        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                userRoles.add(user.getRole());
            }
        }

        return userRoles;
    }

    public User getUserById(UUID userId){
        for (User user : userList) {
            if (user.getId().equals(userId)) {
                return user;
            }
        }
        return null;
    }

    public User getUserByName(String username) {
        for (User user : userList) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                return user;
            }
        }
        return null;
    }

    public ArrayList<User> getAll() {
        return userList;
    }

    public boolean login(String username, String password) {
        for (User user : userList) {
            if(user.getUsername().equalsIgnoreCase(username) && user.getPassword().equals(password)) {
                return true;
            }
        }
        return false;
    }
}
