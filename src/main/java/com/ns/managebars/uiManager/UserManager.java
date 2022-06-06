package com.ns.managebars.uiManager;

import com.ns.managebars.model.Role;
import com.ns.managebars.model.User;
import com.ns.managebars.sevice.UserService;
import com.ns.managebars.uiManager.ObservablePattern.Observer;
import com.ns.managebars.uiManager.ObservablePattern.Subject;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserManager implements Subject {

    private final UserService userService;

    private List<User> users;

    private User actualUser;

    private List<Observer> observers = new ArrayList<>();

    public UserManager(UserService userService){
        this.userService = userService;
        setUsers();

    }


    private void setUsers(){
        users = userService.queryUsers();
    }

    public boolean authenticate(String passcode){

        for (User user : users) {
            if (user.getPasscode().equals(passcode)) {
                this.actualUser = user;
                System.out.println(user);

                if(actualUser.getRole().equals(Role.WAITER))
                    notifyObservers();

                return true;
            }
        }
        return false;
    }

    public User getActualUser() {
        return actualUser;
    }

    public List<User> getUsers() {
        return users;
    }

    public User getUser(int userId){
        for (User user : users) {
            if (user.getId() == userId)
                return user;
        }
        return null;
    }

    public void clearActualUser (){
        this.actualUser = null;
    }

    @Override
    public void registerObserver(Observer o, int index) {
        observers.add(index,o);

    }

    @Override
    public void removeObserver(Observer o) {
        observers.remove(o);

    }

    @Override
    public void notifyObservers() {
        for (Observer o : observers) {
            o.update();
        }

    }

    public void changeUserName(User waiter, String newName) {
       userService.updateUserName(waiter,newName);
       waiter.setName(newName);
    }

    public User createNewUser(String newWaiterName, String newWaiterPasscode) {
        User waiter = new User(newWaiterName,Role.WAITER,newWaiterPasscode);
        userService.saveNewUser(waiter);
        users.add(waiter);
        return waiter;
    }

    public void deleteUser(User selectedUser) {
        userService.removeUser(selectedUser);
        users.remove(selectedUser);
    }
}
