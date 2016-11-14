package com.amontes.thecounted;

import android.content.Context;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

class DataHelper {

    static ArrayList<Victim> getSavedData(Context context){

        ArrayList<Victim> savedArray = null;

        try {

            FileInputStream fis = context.openFileInput("Victims");
            ObjectInputStream ois = new ObjectInputStream(fis);
            savedArray = (ArrayList<Victim>) ois.readObject();
            ois.close();

        } catch (IOException | ClassNotFoundException e) {

            e.printStackTrace();

        }

        return savedArray;

    }

}
