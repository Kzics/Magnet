package com.magnet.loader;

import com.magnet.Magnet;
import com.magnet.Main;

import java.io.File;

public class MagnetsLoader {


    public static void loadMagnets(){
        File file = new File(Main.getInstance().getDataFolder(), "magnets");
        if(!file.exists()){
            file.mkdirs();
        }
        if(file.listFiles() == null){
            return;
        }
        if(file.listFiles().length == 0){
            return;
        }
        for(File magnetFile : file.listFiles()){
            Main.getInstance().getMagnetsManager().addMagnet(new Magnet(magnetFile));
            System.out.println("Loaded magnet: " + magnetFile.getName());
        }
    }
}
