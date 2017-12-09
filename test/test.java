package com.java_analyst;

import java.util.ArrayList;
import java.util.HashMap;

interface itf_test{
    public void hello();
    public void wow();
}

interface itf_test2{
    public void helloo();
    public void woww();
}

interface itf_test3{
    public void hello1();
    public void wow1();
}

public abstract class test implements itf_test, itf_test2, itf_test3{
    private static String hello = "PCA", name ="Tuan";
    public final Double weight = 9.0;
    Integer age;
    private String job;
    private static ArrayList<Integer> cout = new ArrayList<>();
    private HashMap<String, Double> dic = new HashMap<>();

    public test() {
        name = "";
        age = 0;
        job = "";
    }
    
    public test(String name, Integer age, String job){
        this.name = name;
        this.age = age;
        this.job = job;
    }
    public String getName(Integer age){
        return name;
    }
    
    public String setInfo(String name, String job){
        if( 2 > 1 ){
            String tuan = "Tuan";
        }
        
        while(2>1){
            int a = 3;
        }
        this.name = name; 
        this.job = job;
        return job;
    }
    public static String getNamee(){
        String myname = "World";
        return (String)myname;
    }
}

class Tuan{
    
}