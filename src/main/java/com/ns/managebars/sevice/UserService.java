package com.ns.managebars.sevice;

import com.ns.managebars.model.User;
import com.ns.managebars.repo.UserRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }


    public List<User> queryUsers() {
        return userRepo.getAllUsers();
    }

    public void updateUserName(User waiter, String newName) {
        userRepo.updateUserName(waiter,newName);
    }

    public void saveNewUser(User user) {
        userRepo.save(user);
    }

    public void removeUser(User user) {
        userRepo.removeUser(user.getId());
    }
}
