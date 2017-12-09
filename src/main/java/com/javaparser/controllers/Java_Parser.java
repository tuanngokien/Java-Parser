package com.javaparser.controllers;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.Node;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.BodyDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.EnumConstantDeclaration;
import com.github.javaparser.ast.body.EnumDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.Statement;
import com.github.javaparser.ast.type.ClassOrInterfaceType;
import com.javaparser.model.Container;
import com.javaparser.model.Function;
import com.javaparser.model.RelaType;
import com.javaparser.model.Variable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Java_Parser {

    private final HashMap<String, Container> class_list = new HashMap();
    private final HashMap<String, Container> interface_list = new HashMap();
    private final HashMap<String, Container> enum_list = new HashMap();
    private final ArrayList<String> relationships = new ArrayList();

    private int class_count = 0;
    private int interface_count = 0;
    private int enum_count = 0;
    private int var_count = 0;
    private int constr_count = 0;
    private int method_count = 0;

    private int assoc_count = 0;
    private int inherit_count = 0;
    private int imple_count = 0;

    HashMap<String, Integer> var_type_count = new HashMap();

    public Java_Parser() {
    }

    public void SourceCode_parser(File file) {
        try {
            if (file.isFile()) {
                FileInputStream inputStream = new FileInputStream(file.getAbsolutePath());

                CompilationUnit cu = JavaParser.parse(inputStream);
                String package_name = (!cu.getPackageDeclaration().equals(Optional.empty())) ? ((PackageDeclaration) cu.getPackageDeclaration().get()).getName().toString() : "";
                List<Node> childrenNodes = cu.getChildNodes();
                for (Node childNode : childrenNodes) {
                    if (childNode instanceof ClassOrInterfaceDeclaration) {
                        ClassOrInterfaceDeclaration node = (ClassOrInterfaceDeclaration) childNode;
                        parseClassOrInterface(package_name, node);
                    } else if ((childNode instanceof EnumDeclaration)) {
                        EnumDeclaration node = (EnumDeclaration) childNode;
                        parseEnum(package_name, node);
                    }
                }
            }
        } catch (FileNotFoundException ex) {
            String package_name;
            System.out.println(ex);
            ex.printStackTrace();
        }
    }

    public void parseClassOrInterface(String package_name, ClassOrInterfaceDeclaration node) {
        String name = node.getNameAsString();
        ArrayList<String> access_mod = get_access_mod(node.getModifiers());

        checkRelationships(node);

        ArrayList<Variable> var_list = new ArrayList();
        ArrayList<Function> constr_list = new ArrayList();
        ArrayList<Function> method_list = new ArrayList();
        NodeList<BodyDeclaration<?>> elements = node.getMembers();
        for (BodyDeclaration element : elements) {
            if (element instanceof FieldDeclaration) {
                FieldDeclaration var = (FieldDeclaration) element;
                ArrayList<Variable> var_data = parse_variable(var);
                var_list.addAll(var_data);
                var_count += var_data.size();
            } else if (element instanceof ConstructorDeclaration) {
                ConstructorDeclaration constr = (ConstructorDeclaration) element;
                Function constr_data = parse_constructor(constr);
                constr_list.add(constr_data);
                constr_count += 1;
            } else if ((element instanceof MethodDeclaration)) {
                MethodDeclaration method = (MethodDeclaration) element;
                Function method_data = parse_method(method);
                method_list.add(method_data);
                method_count += 1;
            } else if ((element instanceof ClassOrInterfaceDeclaration)) {
                ClassOrInterfaceDeclaration inner_node = (ClassOrInterfaceDeclaration) element;
                parseClassOrInterface(package_name, inner_node);
            } else if ((element instanceof EnumDeclaration)) {
                EnumDeclaration inner_node = (EnumDeclaration) element;
                parseEnum(package_name, inner_node);
            }
        }
        if (node.isInterface()) {
            String type = "INTERFACE";
            interface_list.put(name, new Container(package_name, type, name, access_mod, var_list, constr_list, method_list));
            interface_count += 1;
        } else {
            String type = "CLASS";
            class_list.put(name, new Container(package_name, type, name, access_mod, var_list, constr_list, method_list));
            class_count += 1;
        }
    }

    public void parseEnum(String package_name, EnumDeclaration node) {
        String name = node.getNameAsString();
        String type = "ENUM";
        checkRelationships(node);
        List<Node> enum_child_nodes = node.getChildNodes();
        ArrayList<Variable> var_list = new ArrayList();
        ArrayList<Function> constr_list = new ArrayList();
        ArrayList<Function> method_list = new ArrayList();
        for (Node element : enum_child_nodes) {
            if (element instanceof EnumConstantDeclaration) {
                EnumConstantDeclaration constant_enum = (EnumConstantDeclaration) element;
                Variable const_enum_data = parseConstantEnum(constant_enum);
                var_list.add(const_enum_data);
            } else if (element instanceof FieldDeclaration) {
                FieldDeclaration var = (FieldDeclaration) element;
                ArrayList<Variable> var_data = parse_variable(var);
                var_list.addAll(var_data);
            } else if (element instanceof ConstructorDeclaration) {
                ConstructorDeclaration constr = (ConstructorDeclaration) element;
                Function constr_data = parse_constructor(constr);
                constr_list.add(constr_data);
            } else if (element instanceof MethodDeclaration) {
                MethodDeclaration method = (MethodDeclaration) element;
                Function method_data = parse_method(method);
                method_list.add(method_data);
            } else if (element instanceof ClassOrInterfaceDeclaration) {
                ClassOrInterfaceDeclaration inner_node = (ClassOrInterfaceDeclaration) element;
                parseClassOrInterface(package_name, inner_node);
            } else if (element instanceof EnumDeclaration) {
                EnumDeclaration inner_node = (EnumDeclaration) element;
                parseEnum(package_name, inner_node);
            }
        }
        enum_count += 1;
        enum_list.put(name, new Container(package_name, type, name, new ArrayList(), var_list, constr_list, method_list));
    }

    public ArrayList<String> get_access_mod(EnumSet<Modifier> modifiers) {
        ArrayList<String> access_mod = new ArrayList();
        if (modifiers.contains(Modifier.PUBLIC)) {
            access_mod.add("public");
        } else if (modifiers.contains(Modifier.PRIVATE)) {
            access_mod.add("private");
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            access_mod.add("proteted");
        } else {
            access_mod.add("default");
        }
        if (modifiers.contains(Modifier.ABSTRACT)) {
            access_mod.add("abstract");
        }
        if (modifiers.contains(Modifier.STATIC)) {
            access_mod.add("static");
        }
        if (modifiers.contains(Modifier.FINAL)) {
            access_mod.add("final");
        }
        return access_mod;
    }

    public ArrayList<Variable> parse_variable(FieldDeclaration var_exp) {
        String var_type = var_exp.getElementType().toString();
        String default_value = null;
        ArrayList<String> var_access_mod = get_access_mod(var_exp.getModifiers());
        ArrayList<Variable> var_data = new ArrayList();
        for (VariableDeclarator var : var_exp.getVariables()) {
            String var_name = var.getNameAsString();
            int pos = var_name.indexOf('=');
            if (pos > -1) {
                default_value = var_name.substring(pos + 2);
                var_name = var_name.substring(0, pos - 1);
            }
            var_data.add(new Variable(var_type, var_name, var_access_mod, default_value));
        }
        if (var_type_count.get(var_type) == null) {
            var_type_count.put(var_type, var_data.size());
        } else {
            var_type_count.put(var_type, var_type_count.get(var_type) + var_data.size());
        }
        return var_data;
    }

    public Function parse_constructor(ConstructorDeclaration constr) {
        String constr_name = constr.getName().toString();
        String return_type = null;
        ArrayList<String> constr_access_mod = get_access_mod(constr.getModifiers());
        ArrayList<Variable> pam_list = new ArrayList();
        List<Parameter> parameters = constr.getParameters();
        if (!parameters.isEmpty()) {
            for (Parameter pam : parameters) {
                String pam_type = pam.getType().toString();
                String pam_name = pam.getName().toString();
                pam_list.add(new Variable(pam_type, pam_name, new ArrayList(), null));
            }
        }
        String body = constr.getBody().toString();
        return new Function(constr_name, return_type, constr_access_mod, pam_list, body);
    }

    public Function parse_method(MethodDeclaration method) {
        String mtd_name = method.getName().toString();
        String return_type = method.getType().toString();
        ArrayList<String> mtd_access_mod = get_access_mod(method.getModifiers());
        ArrayList<Variable> pam_list = new ArrayList();
        List<Parameter> parameters = method.getParameters();
        if (!parameters.isEmpty()) {
            for (Parameter pam : parameters) {
                String pam_type = pam.getType().toString();
                String pam_name = pam.getName().toString();
                pam_list.add(new Variable(pam_type, pam_name, new ArrayList(), null));
            }
        }
        String body = (method.getBody().equals(Optional.empty())) ? "" : ((BlockStmt) method.getBody().get()).toString();
        return new Function(mtd_name, return_type, mtd_access_mod, pam_list, body);
    }

    public Variable parseConstantEnum(EnumConstantDeclaration const_enum) {
        return new Variable(null, const_enum.toString(), new ArrayList(), null);
    }

    public void checkRelationships(ClassOrInterfaceDeclaration node) {
        String class_name = node.getNameAsString();
        String parent_name;
        if (!node.getExtendedTypes().isEmpty()) {
            parent_name = ((ClassOrInterfaceType) node.getExtendedTypes().get(0)).toString();
            relationships.add("[" + node.getNameAsString() + "]" + RelaType.INHERITANCE.getSymbol() + "[" + parent_name + "]");
            inherit_count += 1;
        }

        if (!node.getImplementedTypes().isEmpty()) {
            for (ClassOrInterfaceType impl_node : node.getImplementedTypes()) {
                relationships.add("[" + node.getNameAsString() + "]" + RelaType.REALIZATION.getSymbol() + "[" + impl_node.getNameAsString() + "]");
                imple_count += 1;
            }
        }
        NodeList<BodyDeclaration<?>> elements = node.getMembers();
        for (BodyDeclaration element : elements) {
            if ((element instanceof FieldDeclaration)) {
                FieldDeclaration var = (FieldDeclaration) element;
                List<Node> var_types = var.getElementType().getChildNodes();
                checkAssociation(var_types, class_name);
            } else if ((element instanceof MethodDeclaration)) {
                MethodDeclaration method = (MethodDeclaration) element;
                String return_type = method.getType().toString();
                if (!return_type.equals("void")) {
                    checkAssociation(return_type, class_name);
                }
                if (!method.getBody().equals(Optional.empty())) {
                    for (Node body_node : ((BlockStmt) method.getBody().get()).getChildNodes()) {
                        parseStmt(body_node, class_name);
                    }
                }
            }
        }
    }

    public void parseStmt(Node body_node, String class_name) {
        ExpressionStmt ex_stmt;
        if ((body_node instanceof ExpressionStmt)) {
            ex_stmt = (ExpressionStmt) body_node;
            if ((ex_stmt.getExpression() instanceof VariableDeclarationExpr)) {
                VariableDeclarationExpr var_decl = (VariableDeclarationExpr) ex_stmt.getExpression();
                checkAssociation(var_decl.getElementType().getChildNodes(), class_name);
            }
        } else if ((body_node instanceof Statement)) {
            for (Node inner_node : body_node.getChildNodes()) {
                parseStmt(inner_node, class_name);
            }
        }
    }

    public void checkRelationships(EnumDeclaration node) {
        String class_name = node.getNameAsString();
        NodeList<BodyDeclaration<?>> elements = node.getMembers();
        for (BodyDeclaration element : elements) {
            if ((element instanceof FieldDeclaration)) {
                FieldDeclaration var = (FieldDeclaration) element;
                List<Node> var_types = var.getElementType().getChildNodes();
                checkAssociation(var_types, class_name);
            } else if ((element instanceof MethodDeclaration)) {
                MethodDeclaration method = (MethodDeclaration) element;
                String return_type = method.getType().toString();

                if (!return_type.equals("void")) {
                    checkAssociation(return_type, class_name);
                }

                if (!method.getBody().equals(Optional.empty())) {
                    for (Node body_node : ((BlockStmt) method.getBody().get()).getChildNodes()) {
                        parseStmt(body_node, class_name);
                    }
                }
            }
        }
    }

    public void checkAssociation(List<Node> var_types, String class_name) {
        if (var_types.size() == 1) {
            checkAssociation(((Node) var_types.get(0)).toString(), class_name);
        } else {
            for (int i = 1; i < var_types.size(); i++) {
                checkAssociation(((Node) var_types.get(i)).toString(), class_name);
            }
        }
    }

    public void checkAssociation(String type, String class_name) {
        if (!type.equals(class_name)) {
            String rela = "[" + class_name + "]" + RelaType.ASSOCIATION.getSymbol() + "[" + type + "]";
            if (!relationships.contains(rela) && class_name != type) {
                relationships.add(rela);
                assoc_count += 1;
            }
        }
    }

    public ArrayList<String> getRelationships() {
        return relationships;
    }

    public HashMap<String, Container> getClassList() {
        return class_list;
    }

    public HashMap<String, Container> getInterfaceList() {
        return interface_list;
    }

    public HashMap<String, Container> getEnumList() {
        return enum_list;
    }

    public int getClass_count() {
        return class_count;
    }

    public int getInterface_count() {
        return interface_count;
    }

    public int getEnum_count() {
        return enum_count;
    }

    public int getVar_count() {
        return var_count;
    }

    public int getConstr_count() {
        return constr_count;
    }

    public int getMethod_count() {
        return method_count;
    }

    public int getAssoc_count() {
        return assoc_count;
    }

    public int getInherit_count() {
        return inherit_count;
    }

    public int getImple_count() {
        return imple_count;
    }

    public HashMap<String, Integer> getVar_type_count() {
        return var_type_count;
    }
}
