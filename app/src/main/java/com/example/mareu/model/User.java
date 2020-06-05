package com.example.mareu.model;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {

    private String firstName;
    private String lastName;
    private String emailAddress;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public User(String firstName, String lastName, String emailAddress) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
    }

    public static List<String> listParticipants = generateUsers();




    public static List<String> generateUsers() {
        List<User> dummyUser = new ArrayList<>();
        dummyUser.add(new User("Célia", "Chaabane", "g4cklez@live.fr"));
        dummyUser.add(new User("Mohamed", "Chaabane", "momoji@gmail.com"));
        dummyUser.add(new User("Yousra", "Chaabane", "yousra.chbn@gmail.com"));
        dummyUser.add(new User("Nesrine", "Chaabane", "nesrine.chbn@gmail.com"));
        dummyUser.add(new User("Anfel", "Chaabane", "anfel.chbn@gmail.com"));
        List<String> usersList = new ArrayList<>();
        for (User user : dummyUser) {
            usersList.add(user.emailAddress);
        }
        return usersList;
    }
}
