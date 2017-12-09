package com.javaparser.model;

import java.util.ArrayList;
import java.util.HashMap;
import javafx.scene.control.Label;

public class ClassDiagram {

    ArrayList<DiagramNode> parentNode_list;
    ArrayList<DiagramNode> dNode_list;

    public ClassDiagram() {
        parentNode_list = new ArrayList<>();
        dNode_list = new ArrayList<>();
    }
    
    public void initializeDiagram(ArrayList<String> relationships){
        HashMap<String, Integer> child_count = new HashMap<>();
        for (String rela : relationships) {
            String child_name = rela.substring(rela.indexOf("[") + 1, rela.indexOf("]"));
            String parent_name = rela.substring(rela.lastIndexOf("[") + 1, rela.lastIndexOf("]"));
            Integer value = child_count.get(parent_name);
            if (value == null) {
                int count = getChildCount(parent_name);
                child_count.put(parent_name, count);
            }
        }

        String[] countDESC_child = child_count.keySet().toArray(new String[0]);
        for (int i = 0; i < countDESC_child.length; i++) {
            for (int j = i + 1; j < countDESC_child.length; j++) {
                String name_a = countDESC_child[i];
                String name_b = countDESC_child[j];
                if (child_count.get(name_a) < child_count.get(name_b)) {
                    String temp = countDESC_child[i];
                    countDESC_child[i] = countDESC_child[j];
                    countDESC_child[j] = temp;
                }
            }
        }
        
        for (int i = 0; i < countDESC_child.length; i++) {
            setParent(countDESC_child[i]);
        }
    }

    public void addNode(Label node) {
        dNode_list.add(new DiagramNode(node));
    }

    public void addChildNode(Label parentNode, Label childNode) {
        for (DiagramNode node : dNode_list) {
            if (parentNode == node.getLabelNode()) {
                for (DiagramNode child : dNode_list) {
                    if (childNode == child.getLabelNode()) {
                        node.addChildNode(child);
                    }
                }
            }
        }
    }

    public boolean isChildOfParent(DiagramNode node) {
        for (DiagramNode parent : parentNode_list) {
            boolean isChild = isChildOfParent(parent, node);
            if (isChild == true) {
                return true;
            }
        }
        return false;
    }

    public boolean isChildOfParent(DiagramNode parentNode, DiagramNode node) {
        if (parentNode == node) {
            return true;
        }
        for (DiagramNode childNode : parentNode.getChildNodes()) {
            boolean isChild = isChildOfParent(childNode, node);
            if (isChild == true) {
                return true;
            }
        }
        return false;
    }

    public void setParent(String parent_name) {
        if (isTypeInList(parent_name)) {
            for (DiagramNode node : dNode_list) {
                if (node.getLabelNode().getText().equals(parent_name) && isTypeInList(parent_name)) {
                    if (!isChildOfParent(node)) {
                        parentNode_list.add(node);
                    }
                }
            }
        }
    }

    double[] next;

    public void sort() {
        next = new double[100];
        for (DiagramNode node : dNode_list) {
            node.setSorted(false);
        }
        for (DiagramNode node : parentNode_list) {
            sort(node, 0);
        }
        for (DiagramNode node : dNode_list) {
            if (!node.isSorted()) {
                sort(node, 0);
            }
        }
    }

    private void sort(DiagramNode root, int depth) {
        if (!root.isSorted()) {
            double margin_right = 40;
            double x;
            double y = depth * 200;
            for (DiagramNode node : root.getChildNodes()) {
                sort(node, depth + 1);
            }

            if (root.getChildNodes().isEmpty()) {
                x = next[depth];
            } else if (root.getChildNodes().size() == 1) {
                Label parent = root.getLabelNode();
                Label uniChild = root.getChildNodes().get(0).getLabelNode();
                x = (uniChild.getTranslateX() + uniChild.getWidth() / 2) - parent.getWidth() / 2;
            } else {
                Label firstChild = root.getChildNodes().get(0).getLabelNode();
                Label lastChild = root.getChildNodes().get(root.getChildNodes().size() - 1).getLabelNode();
                x = (firstChild.getTranslateX() + lastChild.getTranslateX()) / 2;
            }
            if (!root.getChildNodes().isEmpty()) {
                if (x < next[depth]) {
                    double offset = next[depth] - x;
                    moveTree(root, offset);
                    x = next[depth];
                }
            }
            root.setSorted(true);
            root.setLabelPosition(x, y);
            next[depth] = margin_right + root.getLabelNode().getTranslateX() + root.getLabelNode().getWidth();
        }
    }

    private void moveTree(DiagramNode root, double offset) {
        for (DiagramNode childNode : root.getChildNodes()) {
            moveTree(childNode, offset);
        }
        double new_x = root.getLabelNode().getTranslateX() + offset;
        root.getLabelNode().setTranslateX(new_x);
    }

    public void printNode() {
        for (DiagramNode node : dNode_list) {
            System.out.print(node.getLabelNode().getText());
            for (DiagramNode child_node : node.getChildNodes()) {
                System.out.print(child_node.getLabelNode().getText() + " ");
            }
            System.out.println("");
        }
    }

    public boolean isTypeInList(String name) {
        for (DiagramNode node : dNode_list) {
            if (node.getLabelNode().getText().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public int getChildCount(String name) {
        DiagramNode node = null;
        for (DiagramNode node_ : dNode_list) {
            if (node_.getLabelNode().getText().equals(name)) {
                node = node_;
            }
        }
        if (node == null) {
            return 0;
        }
        int count = 0;
        for (DiagramNode childNode : node.getChildNodes()) {
            count += getChildCount(childNode);
        }
        return count;
    }

    public int getChildCount(DiagramNode node) {
        int count = 1;
        for (DiagramNode childNode : node.getChildNodes()) {
            count += getChildCount(childNode);
        }
        return count;
    }

    public void resetRelationship() {
        parentNode_list = new ArrayList<>();
        for (DiagramNode node : dNode_list) {
            node.deleteChildNodes();
        }
    }
}
