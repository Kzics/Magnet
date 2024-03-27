package com.magnet.managers;

import com.magnet.Magnet;

import java.util.HashSet;
import java.util.Set;

public class MagnetsManager {


    private final Set<Magnet> magnetTypes;
    public MagnetsManager(){
        this.magnetTypes = new HashSet<>();
    }

    public Set<Magnet> getMagnet() {
        return magnetTypes;
    }

    public void addMagnet(Magnet magnet){
        this.magnetTypes.add(magnet);
    }
    public Magnet getMagnet(String id){
        for(Magnet magnet : this.magnetTypes){
            if(magnet.getId().equals(id)){
                return magnet;
            }
        }
        return null;
    }

}
