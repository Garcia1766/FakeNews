package com.java.chenguo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TopMenuChoiceOthers {
    private static ArrayList<String> choice = new ArrayList<>();

    static {
        choice.add("头条");
        choice.add("游戏");
        choice.add("时尚");
        choice.add("电影");
        choice.add("数码");
        choice.add("旅游");
        choice.add("养生");
        choice.add("育儿");
        choice.add("历史");
    }

    public static ArrayList<String> getChoice() {
        return choice;
    }

    public static void setChoice(ArrayList<String> choice2) { choice = choice2; }

}