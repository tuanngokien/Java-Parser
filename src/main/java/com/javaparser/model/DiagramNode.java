package com.javaparser.model;

import java.util.ArrayList;
import javafx.scene.control.Label;

public class DiagramNode {

    private final Label dNode_label;
    private ArrayList<DiagramNode> childNode_list;
    private boolean sorted = false;

    public DiagramNode() {
        this.dNode_label = null;
        childNode_list = new ArrayList<>();
    }

    public DiagramNode(Label node) {
        this.dNode_label = node;
        childNode_list = new ArrayList<>();
    }

    public void addChildNode(DiagramNode childNode) {
        childNode_list.add(childNode);
    }

    public void setLabelPosition(double x, double y) {
        dNode_label.setTranslateX(x);
        dNode_label.setTranslateY(y);
    }

    public ArrayList<DiagramNode> getChildNodes() {
        return childNode_list;
    }

    public Label getLabelNode() {
        return dNode_label;
    }

    public boolean isSorted() {
        return sorted;
    }

    public void setSorted(boolean sorted) {
        this.sorted = sorted;
    }

    public void deleteChildNodes() {
        childNode_list = new ArrayList<>();
    }

}
