package com.javaparser.model;

import java.util.Arrays;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

public class ArrowLine {

    private final Polygon arrow;
    private Line custom_arrow;  // for association arrow
    private final Line line;
    private final Label src_label;
    private final Label tg_label;
    private final RelaType type;

    public ArrowLine(Label src, Label target, String rela_operator) {
        this.arrow = new Polygon();
        this.line = new Line();
        this.src_label = src;
        this.tg_label = target;
        createLine();
        arrowAddListener();
        if (rela_operator.equals(RelaType.INHERITANCE.getSymbol())) {
            type = RelaType.INHERITANCE;
            line.getStyleClass().add("l_inheritance");
            arrow.getStyleClass().add("a_inheritance");
        } else if (rela_operator.equals(RelaType.REALIZATION.getSymbol())) {
            type = RelaType.REALIZATION;
            line.getStyleClass().add("l_realization");
            arrow.getStyleClass().add("a_realization");
        } else if (rela_operator.equals(RelaType.ASSOCIATION.getSymbol())) {
            type = RelaType.ASSOCIATION;
            line.getStyleClass().add("l_inheritance");
            arrow.getStyleClass().add("a_association");
            custom_arrow = new Line();
            custom_arrow.getStyleClass().add("l_association_custom");
        } else {
            type = RelaType.UNKNOWN;
        }
    }

    private void createLine() {
        line.startXProperty().bind(src_label.translateXProperty().add(src_label.widthProperty().divide(2)));
        line.startYProperty().bind(src_label.translateYProperty().add(src_label.heightProperty().divide(2)));
        line.endXProperty().bind(tg_label.translateXProperty().add(tg_label.widthProperty().divide(2)));
        line.endYProperty().bind(tg_label.translateYProperty().add(tg_label.heightProperty().divide(2)));
        double[] peak = getArrowPeak();
        updateArrow(line.startXProperty().get(), line.startYProperty().get(), peak[0], peak[1]);
    }

    private void arrowAddListener() {
        line.startXProperty().addListener((observable, oldValue, newValue) -> {
            double[] peak = getArrowPeak();
            updateArrow((double) newValue, line.startYProperty().get(), peak[0], peak[1]);
        });
        line.startYProperty().addListener((observable, oldValue, newValue) -> {
            double[] peak = getArrowPeak();
            updateArrow(line.startXProperty().get(), (double) newValue, peak[0], peak[1]);
        });

        line.endXProperty().addListener((observable, oldValue, newValue) -> {
            double[] peak = getArrowPeak();
            updateArrow(line.startXProperty().get(), line.startYProperty().get(), peak[0], peak[1]);
        });

        line.endYProperty().addListener((observable, oldValue, newValue) -> {
            double[] peak = getArrowPeak();
            updateArrow(line.startXProperty().get(), line.startYProperty().get(), peak[0], peak[1]);
        });
    }

    public double[] getArrowPeak() {
        //find intersection point of the line and the retangle (target_label) for drawing arrow there
        double peak_x;
        double peak_y;
        double rec_width = tg_label.getWidth();
        double rec_height = tg_label.getHeight();
        double line_x1 = line.getStartX();
        double line_y1 = line.getStartY();
        double line_x2 = line.getEndX();
        double line_y2 = line.getEndY();
        double angle_factor = (line_y1 - line_y2) / (line_x1 - line_x2);
        if (angle_factor * rec_width / 2 < rec_height / 2 && angle_factor * rec_width / 2 > -rec_height / 2) { // point is in the left or right side
            if (line_x1 > line_x2) {
                peak_x = line_x2 + rec_width / 2;
                peak_y = line_y2 + (rec_width / 2) * angle_factor;
            } else {
                peak_x = line_x2 - rec_width / 2;
                peak_y = line_y2 - (rec_width / 2) * angle_factor;
            }
        } else {// point is in the top or bottom side
            if (line_y1 > line_y2) {
                peak_y = line_y2 + rec_height / 2;
                peak_x = line_x2 + (rec_height / 2) / angle_factor;
            } else {
                peak_y = line_y2 - rec_height / 2;
                peak_x = line_x2 - (rec_height / 2) / angle_factor;
            }
        }
        return new double[]{peak_x, peak_y};
    }

    public void updateArrow(double line_x1, double line_y1, double line_x2, double line_y2) {
        double dx = line_x1 - line_x2;
        double dy = line_y1 - line_y2;
        double arrow_length = 10.0;
        double arrow_angle = Math.toRadians(30);
        double angle = Math.atan2(dy, dx);  // angle between direct vector of input line and X-axis
        //coordinate of 3 peaks of triangle
        double peak_x1 = line_x2;
        double peak_y1 = line_y2;

        double peak_x2 = Math.cos(angle + arrow_angle) * arrow_length + line_x2;
        double peak_y2 = Math.sin(angle + arrow_angle) * arrow_length + line_y2;

        double peak_x3 = Math.cos(angle - arrow_angle) * arrow_length + line_x2;
        double peak_y3 = Math.sin(angle - arrow_angle) * arrow_length + line_y2;
        arrow.getPoints().setAll(new Double[]{
            peak_x1, peak_y1,
            peak_x2, peak_y2,
            peak_x3, peak_y3
        });
        if (type == RelaType.ASSOCIATION) {
            custom_arrow.setStartX(peak_x2);
            custom_arrow.setStartY(peak_y2);
            custom_arrow.setEndX(peak_x3);
            custom_arrow.setEndY(peak_y3);
        }
    }

    public Group getArrowLine() {
        Group arrow_line = new Group();
        if (type != RelaType.ASSOCIATION) {
            arrow_line.getChildren().addAll(line, arrow);
        } else {
            arrow_line.getChildren().addAll(arrow, custom_arrow, line);
        }
        return arrow_line;
    }

}
