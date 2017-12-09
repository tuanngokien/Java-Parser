package com.javaparser.model;

import java.util.ArrayList;

public class Function {

    private String name;
    private String return_type;
    private ArrayList<String> access_mod;
    private ArrayList<Variable> pam_list;
    private String body;

    public Function(String name, String return_type, ArrayList<String> access_mod, ArrayList<Variable> pam_list, String body) {
        this.name = name;
        this.return_type = return_type;
        this.access_mod = access_mod;
        this.pam_list = pam_list;
        this.body = body;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReturn_type() {
        return return_type;
    }

    public void setReturn_type(String return_type) {
        this.return_type = return_type;
    }

    public ArrayList<String> getAccess_mod() {
        return access_mod;
    }

    public void setAccess_mod(ArrayList<String> access_mod) {
        this.access_mod = access_mod;
    }

    public ArrayList<Variable> getPam_list() {
        return pam_list;
    }

    public void setPam_list(ArrayList<Variable> pam_list) {
        this.pam_list = pam_list;
    }

    public String getContent() {
        return body;
    }

    public void setContent(String body) {
        this.body = body;
    }

    public String getHeader() {
        String string = "";
        if (access_mod.size() > 0) {
            if (access_mod.contains("public")) {
                string = "+";
            } else if (access_mod.contains("private")) {
                string = "- ";
            } else if (access_mod.contains("protect")) {
                string = "# ";
            }
            if (access_mod.contains("abstract")) {
                string += "abstract ";
            } else if (access_mod.contains("final")) {
                string += "final ";
            }
        }
        String pamList_repr = pam_list.toString().replace("[", "").replace("]", "");
        String return_type_toStr = (return_type != null) ? " : " + return_type : "";
        string += name + "(" + pamList_repr + ")" + return_type_toStr;
        return string;
    }

    @Override
    public String toString() {
        String string = "";
        string += name + pam_list + body;
        return string;
    }

    public boolean isStatic() {
        return access_mod.contains("static");
    }

}
