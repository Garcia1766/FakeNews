package com.example.chenguo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopMenuChoice {
    private static ArrayList<String> choice = new ArrayList<>();

    static {
        choice.add("今日");
        choice.add("娱乐");
        choice.add("社会");
        choice.add("文化");
        choice.add("财经");
        choice.add("军事");
        choice.add("科技");
        choice.add("体育");
        choice.add("教育");
        choice.add("汽车");
        choice.add("健康");
    }

    public static ArrayList<String> getChoice() {
        return choice;
    }

    public static void setChoice(ArrayList<String> choice2) { choice = choice2; }


}