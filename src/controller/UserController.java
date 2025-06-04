package controller;

import model.User;
import model.UserDAO;

public class UserController {
    public User login(String nik, String password) {
        return UserDAO.login(nik, password);
    }

    public boolean register(User user) {
    return UserDAO.register(user);
    }   
}



