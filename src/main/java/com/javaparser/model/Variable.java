package com.javaparser.model;

import java.util.ArrayList;

public class Variable {

    private String type;
    private String name;
    private ArrayList<String> access_mod;
    private String default_value;

    public Variable() {
        type = null;
        name = null;
        access_mod = new ArrayList<>();
        default_value = null;
    }

    public Variable(String type, String name, ArrayList<String> access_mod, String default_value) {
        this.type = type;
        this.name = name;
        this.access_mod = access_mod;
        this.default_value = default_value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<String> getAccess_mod() {
        return access_mod;
    }

    public void setAccess_mod(ArrayList<String> access_mod) {
        this.access_mod = access_mod;
    }

    public String getDefault_value() {
        return default_value;
    }

    public void setDefault_value(String default_value) {
        this.default_value = default_value;
    }

    @Override
    public String toString() {
        String string = "";
        if (access_mod.size() > 0) {
            if (access_mod.contains("public")) {
                string += "+";
            } else if (access_mod.contains("private")) {
                string += "-";
            } else if (access_mod.contains("protected")) {
                string += "#";
            } else {
                string += "~";
            }
            if (access_mod.contains("final")) {
                name = name.toUpperCase();
            }
        }
        if (type != null) {
            string += name + " : " + type;
        } else {
            string += name;
        }

        if (default_value != null) {
            string += " = " + default_value;
        }
        return string;
    }

    public boolean isStatic() {
        return access_mod.contains("static");
    }

}
