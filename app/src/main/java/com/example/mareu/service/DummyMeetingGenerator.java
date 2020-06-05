package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.User;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import static android.graphics.Color.rgb;

public class DummyMeetingGenerator {

    private static int randomColor;

    public static int getRandomColor() {
        return randomColor;
    }

    public static List<Meeting> DUMMY_MEETING = Arrays.asList(
            new Meeting(generateColor(), "Salle A", generateStartTime(), generateEndTime(), "Sujet 1", User.listParticipants),
            new Meeting(generateColor(), "Salle B", generateStartTime(), generateEndTime(), "Sujet 2", User.listParticipants),
            new Meeting(generateColor(), "Salle C", generateStartTime(), generateEndTime(), "Sujet 3", User.listParticipants),
            new Meeting(generateColor(), "Salle D", generateStartTime(), generateEndTime(), "Sujet 4", User.listParticipants)
    );

    public static List<String> ROOM_LIST = Arrays.asList(
            "Salle A", "Salle B", "Salle C", "Salle D"
    );

    public static List<Meeting> generateMeeting() {
        return new ArrayList<>(DUMMY_MEETING);
    }

    public static int generateColor() {
        randomColor = rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));
        return randomColor;
    }

    private static Date generateStartTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 8);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }

    private static Date generateEndTime() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 9);
        cal.set(Calendar.MINUTE, 0);
        return cal.getTime();
    }
}
