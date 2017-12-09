package com.javaparser.model;

import java.util.ArrayList;

public class Container {

    private String package_name;
    private String type; // class or interface 
    private String obj_name;
    private ArrayList<String> access_mod;
    private ArrayList<Variable> var_list;
    private ArrayList<Function> constr_list;
    private ArrayList<Function> func_list;

    public Container(String package_name, String type, String obj_name, ArrayList<String> access_mod, ArrayList<Variable> var_list, ArrayList<Function> constr_list, ArrayList<Function> func_list) {
        this.package_name = package_name;
        this.type = type;
        this.obj_name = obj_name;
        this.access_mod = access_mod;
        this.var_list = var_list;
        this.constr_list = constr_list;
        this.func_list = func_list;
    }

    public String getPackage_name() {
        return package_name;
    }

    public void setPackage_name(String package_name) {
        this.package_name = package_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getObj_name() {
        return obj_name;
    }

    public void setObj_name(String obj_name) {
        this.obj_name = obj_name;
    }

    public ArrayList<String> getAccess_mod() {
        return access_mod;
    }

    public void setAccess_mod(ArrayList<String> access_mod) {
        this.access_mod = access_mod;
    }

    public ArrayList<Variable> getVar_list() {
        return var_list;
    }

    public void setVar_list(ArrayList<Variable> var_list) {
        this.var_list = var_list;
    }

    public ArrayList<Function> getConstr_list() {
        return constr_list;
    }

    public void setConstr_list(ArrayList<Function> constr_list) {
        this.constr_list = constr_list;
    }

    public ArrayList<Function> getFunc_list() {
        return func_list;
    }

    public void setFunc_list(ArrayList<Function> func_list) {
        this.func_list = func_list;
    }
    
    public boolean isAbstract(){
        return access_mod.contains("abstract");
    }
    @Override
    public String toString() {
        return type + " " + obj_name;
    }

}
