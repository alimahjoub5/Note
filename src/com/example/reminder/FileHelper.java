package com.example.reminder;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {

    public static void saveListViewToFile(Context context, ListView listView, String fileName) {
        ArrayAdapter<String> adapter = (ArrayAdapter<String>) listView.getAdapter();

        if (adapter == null) {
            return; // Si l'adaptateur est null, il n'y a rien à enregistrer
        }

        File file = new File(context.getFilesDir(), fileName);

    try {
            FileOutputStream outputStream = new FileOutputStream(file);

            int itemCount = adapter.getCount();
            for (int i = 0; i < itemCount; i++) {
                String item = adapter.getItem(i);
                if (item != null) {
                    outputStream.write(item.getBytes());
                    outputStream.write("\n".getBytes()); 
                    // Nouvelle ligne pour chaque élément
                }
            }

            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static List<String> readFileToList(Context context, String fileName) {
        List<String> dataList = new ArrayList<String>();
        File file = new File(context.getFilesDir(), fileName);

        try {
            FileInputStream inputStream = new FileInputStream(file);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line;
            while ((line = bufferedReader.readLine()) != null) {
                dataList.add(line);
            }

            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return dataList;
    }
}
