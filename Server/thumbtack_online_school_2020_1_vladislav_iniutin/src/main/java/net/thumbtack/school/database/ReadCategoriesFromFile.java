package net.thumbtack.school.database;

import net.thumbtack.school.model.Lot;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ReadCategoriesFromFile {
    public static Map<String, Lot> read() {
        Map<String, Lot> list = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader("categories.txt"))) {
            String str;
            while (true) {
                str = br.readLine();
                if (str != null) {
                    list.put(str, null);
                } else break;
            }
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return list;
    }
}
