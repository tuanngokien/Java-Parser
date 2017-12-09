package com.javaparser.controllers;

import com.javaparser.model.*;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.Accordion;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import javafx.scene.transform.Scale;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javax.imageio.ImageIO;

public class Controller implements Initializable {

    private static List<File> file_list = new ArrayList<>();

    private static HashMap<String, Container> class_list = new HashMap<>();
    private static HashMap<String, Container> interface_list = new HashMap<>();
    private static HashMap<String, Container> enum_list = new HashMap<>();
    private static ArrayList<String> relationships = new ArrayList<>();
    private static ClassDiagram class_diagram = new ClassDiagram();

    private int class_count = 0;
    private int interface_count = 0;
    private int enum_count = 0;
    private int var_count = 0;
    private int constr_count = 0;
    private int method_count = 0;

    private int assoc_count = 0;
    private int inherit_count = 0;
    private int imple_count = 0;

    HashMap<String, Integer> var_type_count = new HashMap<>();
    Label current_choosing_label = null;

    @FXML
    private Label obj_type;

    @FXML
    private Label pkg_name;

    @FXML
    private Label obj_name;

    @FXML
    private Label ac_mod;

    @FXML
    private ListView obj_var;

    @FXML
    private Accordion obj_method;

    @FXML
    private AnchorPane label_area;

    @FXML
    private AnchorPane line_area;

    @FXML
    private RadioButton draggable;

    @FXML
    private MenuItem open_folder;

    @FXML
    private MenuItem open_file;

    @FXML
    private StackPane directory_list;

    @FXML
    private TableView<Container> table;

    @FXML
    private TableColumn<Container, String> col_name;

    @FXML
    private TableColumn<Container, String> col_type;

    @FXML
    private TextField search_field;

    @FXML
    private TreeView browser;

    @FXML
    private StackPane main_draw;

    @FXML
    private ScrollPane main_pane;

    @FXML
    private RadioButton default_zoom;

    @FXML
    private CheckBox associa_mode;

    @FXML
    private CheckBox inherit_mode;

    @FXML
    private CheckBox realiz_mode;

    @FXML
    private RadioButton auto_sort;

    @FXML
    private RadioMenuItem mItem_associa;

    @FXML
    private RadioMenuItem mItem_inherit;

    @FXML
    private RadioMenuItem mItem_realiz;

    @FXML
    private RadioMenuItem mItem_showToolbar;

    @FXML
    private AnchorPane toolbar;

    public Controller() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setupViewOptions();
    }

    public void exitApp() {
        Platform.exit();
    }

    public void autoSortDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Tip");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to sort diagram automatically ?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            sortDiagram();
        }
    }

    public void openFolder() {
        final DirectoryChooser path_chooser = new DirectoryChooser();
        path_chooser.setTitle("Choose folder");
        File folder = path_chooser.showDialog(open_folder.getParentPopup().getScene().getWindow());
        if (folder != null) {
            resetScene();
            file_list = new ArrayList<>();
            setUpDirectoryBar(folder);
            parseFiles();
            initCenterPane();
            initList();
            initBrowser();
            draggable.setSelected(true);
            autoSortDialog();
        }
    }

    public void openFile() {
        final FileChooser file_chooser = new FileChooser();
        file_chooser.setTitle("Choose java file");
        file_chooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Java Source", "*.java"));
        List<File> files = file_chooser.showOpenMultipleDialog(open_file.getParentPopup().getScene().getWindow());
        if (files != null) {
            resetScene();
            file_list = files;
            parseFiles();
            initCenterPane();
            initList();
            initBrowser();
            draggable.setSelected(true);
            setUpDirectoryBar(files);
            autoSortDialog();
        }
    }

    public void exportDiagramToImage() {
        FileChooser image_save = new FileChooser();
        image_save.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PNG", "*.png"));
        File image_file = image_save.showSaveDialog(open_file.getParentPopup().getScene().getWindow());
        Alert image_saved = new Alert(Alert.AlertType.INFORMATION);
        image_saved.setTitle("Image Save");
        image_saved.setHeaderText(null);
        if (image_file != null) {
            WritableImage image = main_draw.snapshot(new SnapshotParameters(), null);
            BufferedImage rendered_image = SwingFXUtils.fromFXImage(image, null);
            try {
                ImageIO.write(rendered_image, "png", image_file);
                image_saved.setContentText("Image exported successfully !");
                image_saved.showAndWait();
            } catch (IOException ex) {
                image_saved.setContentText("Image failed to export !");
                image_saved.showAndWait();
            }

        }
    }

    public void setUpDirectoryBar(File folder_path) {
        TreeItem<String> file_list = createBarItem(folder_path);
        TreeView<String> treeView = new TreeView<>(file_list);
        directory_list.getChildren().clear();
        directory_list.getChildren().add(treeView);
    }

    public void setUpDirectoryBar(List<File> files) {
        TreeItem<String> item_list = new TreeItem<>();
        for (File file : files) {
            ImageView i_file = new ImageView(new Image(getClass().getResourceAsStream("/icon/java_file.png")));
            String file_path = file.getAbsolutePath();
            TreeItem<String> item = new TreeItem<>(file_path.substring(file_path.lastIndexOf("\\") + 1), i_file);
            item_list.getChildren().add(item);
        }
        item_list.setExpanded(true);
        TreeView<String> treeView = new TreeView<>(item_list);
        treeView.setShowRoot(false);
        directory_list.getChildren().clear();
        directory_list.getChildren().add(treeView);
    }

    public TreeItem<String> createBarItem(File file) {
        TreeItem<String> item_list = null;
        if (file.isFile()) {
            if (file.getAbsolutePath().endsWith(".java")) {
                ImageView i_file = new ImageView(new Image(getClass().getResourceAsStream("/icon/java_file.png")));
                String file_path = file.getAbsolutePath();
                item_list = new TreeItem<>(file_path.substring(file_path.lastIndexOf("\\") + 1), i_file);
                item_list.setExpanded(true);
                file_list.add(file);
            }
        } else if (file.isDirectory()) {
            String folder_path = file.getAbsolutePath();
            ImageView i_folder = new ImageView(new Image(getClass().getResourceAsStream("/icon/open_folder.png")));
            item_list = new TreeItem<>(folder_path.substring(folder_path.lastIndexOf("\\") + 1), i_folder);
            for (File inner_file : file.listFiles()) {
                TreeItem<String> item = createBarItem(inner_file);
                if (item != null) {
                    if (item.isExpanded()) {
                        item_list.setExpanded(true);
                    }
                    item_list.getChildren().add(item);
                }
            }
        }
        return item_list;
    }

    public void resetScene() {
        class_list = new HashMap<>();
        interface_list = new HashMap<>();
        enum_list = new HashMap<>();
        relationships = new ArrayList<>();
        class_diagram = new ClassDiagram();
        class_count = 0;
        interface_count = 0;
        enum_count = 0;
        var_count = 0;
        constr_count = 0;
        method_count = 0;
        assoc_count = 0;
        inherit_count = 0;
        imple_count = 0;
        var_type_count = new HashMap<>();
        line_area.getChildren().clear();
        label_area.getChildren().clear();
        obj_name.setText("Object");
        obj_type.setText("access");
        pkg_name.setText("Package");
        ac_mod.setText("");
        obj_var.getItems().clear();
        obj_method.getPanes().clear();
        draggable.setSelected(false);
    }

    public void initList() {
        List<Container> obj_list = new ArrayList<>();
        obj_list.addAll(class_list.values());
        obj_list.addAll(interface_list.values());
        obj_list.addAll(enum_list.values());
        ObservableList<Container> row_list = FXCollections.observableArrayList(obj_list);
        col_name.setCellValueFactory(new PropertyValueFactory<>("obj_name"));
        col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        FilteredList<Container> filter_data = new FilteredList<>(row_list, p -> true);
        search_field.textProperty().addListener((observable, oldValue, newValue) -> {
            filter_data.setPredicate(container -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                String text = newValue.toLowerCase();
                if (container.getObj_name().toLowerCase().contains(text)) {
                    return true;
                }
                return false;
            });
        });

        table.setRowFactory((param) -> {
            TableRow<Container> row = new TableRow<>();
            row.setOnMouseClicked((event) -> {
                Container object = row.getItem();
                if (object != null) {
                    String object_name = object.getObj_name();
                    for (Node node : label_area.getChildren()) {
                        if (node instanceof Label) {
                            Label label = (Label) node;
                            if (label.getText().equals(object_name)) {
                                label.getStyleClass().add("current_choosing_label");
                                if (current_choosing_label != null) {
                                    current_choosing_label.getStyleClass().remove("current_choosing_label");
                                }
                                current_choosing_label = label;
                                getDetail(object);
                                break;
                            }
                        }
                    }
                }

            });
            return row;
        });

        SortedList<Container> sorted_list = new SortedList<>(filter_data);
        sorted_list.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sorted_list);
    }

    public void initBrowser() {
        TreeItem<Text> item_list = new TreeItem<>(new Text("Diagram"));
        item_list.setExpanded(true);

        TreeItem<Text> content = new TreeItem<>(new Text("Content"), new ImageView(new Image(getClass().getResourceAsStream("/icon/content.png"))));
        content.setExpanded(true);

        TreeItem<Text> class_count_item = new TreeItem<>(new Text("Class (" + this.class_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/class.png"))));
        TreeItem<Text> interface_count_item = new TreeItem<>(new Text("Interface (" + this.interface_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/interface.png"))));
        TreeItem<Text> enum_count_item = new TreeItem<>(new Text("Enum (" + this.enum_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/enum.png"))));
        TreeItem<Text> variable_count_item = new TreeItem<>(new Text("Field (" + this.var_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/variable.png"))));
        for (String type : var_type_count.keySet()) {
            Text t_var_type = new Text(type + " (" + var_type_count.get(type) + ")");
            t_var_type.getStyleClass().add("italic_style");
            TreeItem<Text> var_type_item = new TreeItem<>(t_var_type);
            variable_count_item.getChildren().add(var_type_item);
        }

        TreeItem<Text> constructor_count_item = new TreeItem<>(new Text("Constructor (" + this.constr_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/constructor.png"))));
        TreeItem<Text> method_count_item = new TreeItem<>(new Text("Method (" + this.method_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/method.png"))));
        content.getChildren().addAll(class_count_item, interface_count_item, enum_count_item, variable_count_item, constructor_count_item, method_count_item);

        TreeItem<Text> relationship = new TreeItem<>(new Text("Relationship"), new ImageView(new Image(getClass().getResourceAsStream("/icon/relationship.png"))));
        relationship.setExpanded(true);

        TreeItem<Text> inhertance = new TreeItem<>(new Text("Inheritance (" + this.inherit_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/inheritance.png"))));
        TreeItem<Text> implement = new TreeItem<>(new Text("Implement (" + this.imple_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/implement.png"))));
        TreeItem<Text> association = new TreeItem<>(new Text("Association (" + this.assoc_count + ")"), new ImageView(new Image(getClass().getResourceAsStream("/icon/association.png"))));

        for (String rela : relationships) {
            String rela_op = rela.substring(rela.indexOf("]") + 1, rela.lastIndexOf("["));
            Text t_rela = new Text(rela);

            TreeItem<Text> rela_item = new TreeItem<>(t_rela);
            if (rela_op.equals(RelaType.INHERITANCE.getSymbol())) {
                inhertance.getChildren().add(rela_item);
                t_rela.getStyleClass().add("inheritance_style");
            } else if (rela_op.equals(RelaType.REALIZATION.getSymbol())) {
                implement.getChildren().add(rela_item);
                t_rela.getStyleClass().add("realization_style");
            } else {
                t_rela.getStyleClass().add("association_style");
                association.getChildren().add(rela_item);
            }
        }

        relationship.getChildren().addAll(inhertance, implement, association);

        item_list.getChildren().addAll(content, relationship);
        browser.setRoot(item_list);
    }

    public void setupViewOptions() {
        mItem_showToolbar.selectedProperty().addListener((observable, oldValue, newValue) -> {
            toolbar.setVisible(newValue);
        });

        mItem_associa.setOnAction((event) -> {
            associa_mode.fire();
        });
        associa_mode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mItem_associa.setSelected(newValue);
        });

        mItem_inherit.setOnAction((event) -> {
            inherit_mode.fire();
        });
        inherit_mode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mItem_inherit.setSelected(newValue);
        });

        mItem_realiz.setOnAction((event) -> {
            realiz_mode.fire();
        });
        realiz_mode.selectedProperty().addListener((observable, oldValue, newValue) -> {
            mItem_realiz.setSelected(newValue);
        });
    }

    public void initCenterPane() {
        main_pane.addEventFilter(ScrollEvent.ANY, (event) -> {
            paneZoom(event);
            event.consume();
        });
        addBoxLabel();
        associa_mode.setSelected(false);
        inherit_mode.setSelected(true);
        realiz_mode.setSelected(true);
        addRelaLines(Arrays.asList(RelaType.INHERITANCE.getSymbol(), RelaType.REALIZATION.getSymbol()));
        setupClassDiagram();
    }

    public void digramShowModeChange() {
        List<String> rela_filter = new ArrayList<>();
        if (inherit_mode.isSelected()) {
            rela_filter.add(RelaType.INHERITANCE.getSymbol());
        }
        if (realiz_mode.isSelected()) {
            rela_filter.add(RelaType.REALIZATION.getSymbol());
        }
        if (associa_mode.isSelected()) {
            rela_filter.add(RelaType.ASSOCIATION.getSymbol());
        }
        addRelaLines(rela_filter);
        setupClassDiagram();
        sortDiagram();
    }

    public void parseFiles() {
        try {
            for (File file : file_list) {
                if (file.getAbsolutePath().endsWith(".java")) {
                    Java_Parser parser = new Java_Parser();
                    parser.SourceCode_parser(file);
                    class_list.putAll(parser.getClassList());
                    interface_list.putAll(parser.getInterfaceList());
                    enum_list.putAll(parser.getEnumList());
                    relationships.addAll(parser.getRelationships());
                    class_count += parser.getClass_count();
                    interface_count += parser.getInterface_count();
                    enum_count += parser.getEnum_count();
                    var_count += parser.getVar_count();
                    constr_count += parser.getConstr_count();
                    method_count += parser.getMethod_count();
                    inherit_count += parser.getInherit_count();
                    imple_count += parser.getImple_count();
                    assoc_count += parser.getAssoc_count();
                    parser.getVar_type_count().forEach((key, value) -> var_type_count.merge(key, value, Integer::sum));
                }
            }
        } catch (NullPointerException ex) {
            System.out.println("Folder not found");
            ex.printStackTrace();
        }
    }

    public void addBoxLabel() {
        for (String name : class_list.keySet()) {
            Label label = new Label(name);
            label.getStyleClass().add("class_label");
            Container class_ = class_list.get(name);
            if(class_.isAbstract()){
                label.getStyleClass().add("abstract_type_label");
            }
            label.setOnMouseClicked((event) -> {
                label.getStyleClass().add("current_choosing_label");
                if (current_choosing_label != null) {
                    current_choosing_label.getStyleClass().remove("current_choosing_label");
                }
                current_choosing_label = label;
                getDetail(class_);
            });
            //enable draggable label
            labelEnableDrag(label);
            label_area.getChildren().add(label);
            class_diagram.addNode(label);
        }

        for (String name : interface_list.keySet()) {
            Label label = new Label(name);
            label.getStyleClass().add("interface_label");
            Container interface_ = interface_list.get(name);
            if(interface_.isAbstract()){
                label.getStyleClass().add("abstract_type_label");
            }
            label.setOnMouseClicked((event) -> {
                label.getStyleClass().add("current_choosing_label");
                if (current_choosing_label != null) {
                    current_choosing_label.getStyleClass().remove("current_choosing_label");
                }
                current_choosing_label = label;
                getDetail(interface_);
            });
            //enable draggable label
            labelEnableDrag(label);
            label_area.getChildren().add(label);
            class_diagram.addNode(label);
        }

        for (String name : enum_list.keySet()) {
            Label label = new Label(name);
            label.getStyleClass().add("enum_label");
            Container enum_ = enum_list.get(name);
            if(enum_.isAbstract()){
                label.getStyleClass().add("abstract_type_label");
            }
            label.setOnMouseClicked((event) -> {
                label.getStyleClass().add("current_choosing_label");
                if (current_choosing_label != null) {
                    current_choosing_label.getStyleClass().remove("current_choosing_label");
                }
                current_choosing_label = label;
                getDetail(enum_);
            });
            //enable draggable label
            labelEnableDrag(label);
            label_area.getChildren().add(label);
            class_diagram.addNode(label);
        }
    }

    public void getDetail(Container ctn) {
        obj_type.setText(ctn.getType());
        pkg_name.setText(ctn.getPackage_name());
        String access_mod = String.join(" ", ctn.getAccess_mod());
        ac_mod.setText(access_mod);
        obj_name.setText(ctn.getObj_name());
        ArrayList<Variable> var_list = ctn.getVar_list();
        ObservableList<Variable> var_obsList = FXCollections.observableArrayList(var_list);
        obj_var.setItems(var_obsList);
        obj_var.setCellFactory((param) -> new ListCell<Variable>() {
            @Override
            protected void updateItem(Variable var, boolean empty) {
                super.updateItem(var, empty);
                if (empty || var == null) {
                    setText(null);
                    getStyleClass().remove("static_type_label");
                } else {
                    setText(var.toString());
                    if (var.isStatic() && !getStyleClass().contains("static_type_label")) {
                        getStyleClass().add("static_type_label");
                    }
                }
            }
        });
        ArrayList<Function> method_list = new ArrayList<>(ctn.getConstr_list());
        method_list.addAll(ctn.getFunc_list());
        obj_method.getPanes().clear();
        for (Function mtd : method_list) {
            AnchorPane paneContent = new AnchorPane();
            paneContent.getChildren().add(new Label(mtd.getContent()));
            TitledPane pane = new TitledPane(mtd.getHeader(), paneContent);
            if (mtd.isStatic()) {
                pane.getStyleClass().add("static_type_title");
            }
            obj_method.getPanes().add(pane);
        }
    }

    public void labelEnableDrag(Label label) {
        final DragContext drag_ctx = new DragContext();
        label.setOnMousePressed((event) -> {
            if (draggable.isSelected()) {
                drag_ctx.setPosX(event.getX());
                drag_ctx.setPosY(event.getY());
            }
        });
        label.setOnMouseDragged((event) -> {
            if (draggable.isSelected()) {
                double newPosX = label.getTranslateX() + event.getX() - drag_ctx.getPosX();
                double newPosY = label.getTranslateY() + event.getY() - drag_ctx.getPosY();
                label.setTranslateX(newPosX);
                label.setTranslateY(newPosY);
            }
        });
    }

    public void addRelaLines(List<String> filter_rela) {
        line_area.getChildren().clear();
        class_diagram.resetRelationship();
        for (String rela : relationships) {
            String src = rela.substring(rela.indexOf("[") + 1, rela.indexOf("]"));
            String target = rela.substring(rela.lastIndexOf("[") + 1, rela.lastIndexOf("]"));
            String rela_op = rela.substring(rela.indexOf("]") + 1, rela.lastIndexOf("["));
            if (filter_rela.contains(rela_op)) {
                Group arrow_line = getRelaLine(src, target, rela_op);
                if (arrow_line != null) {
                    line_area.getChildren().add(arrow_line);
                }
            }
        }
    }

    public Group getRelaLine(String source, String target, String rela_operator) {
        Group arrow_line = new Group();
        for (Node node : label_area.getChildren()) {
            if (node instanceof Label) {
                Label src_label = (Label) node;
                if (src_label.getText().equals(source)) {
                    for (Node node2 : label_area.getChildren()) {
                        if (node2 instanceof Label) {
                            Label tg_label = (Label) node2;
                            if (tg_label.getText().equals(target)) {
                                class_diagram.addChildNode(tg_label, src_label);
                                ArrowLine new_arrow_line = new ArrowLine(src_label, tg_label, rela_operator);
                                return new_arrow_line.getArrowLine();
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public void setupClassDiagram() {
        class_diagram.initializeDiagram(relationships);
    }

    public void sortDiagram() {
        class_diagram.sort();
        auto_sort.setSelected(true);
    }

    public void paneZoom(ScrollEvent event) {
        final double zoom_out_factor = 0.8;
        final double zoom_in_factor = 1.2;
        Scale new_scale = new Scale();
        double h_scroll_pos = main_pane.getHvalue() * main_pane.getWidth();
        double v_scroll_pos = main_pane.getVvalue() * main_pane.getHeight();
        double deltaY = event.getDeltaY();
        if (!main_draw.getTransforms().isEmpty()) {
            Scale last_scale = (Scale) main_draw.getTransforms().get(main_draw.getTransforms().size() - 1);
            if (last_scale.getX() > 1) {
                if (deltaY < 0) {
                    main_draw.getTransforms().remove(last_scale);
                } else {
                    new_scale.setPivotX(h_scroll_pos + event.getX());
                    new_scale.setPivotY(v_scroll_pos + event.getY());
                    new_scale.setX(zoom_in_factor);
                    new_scale.setY(zoom_in_factor);
                    main_draw.getTransforms().add(new_scale);
                }
            } else {
                if (deltaY > 0) {
                    main_draw.getTransforms().remove(last_scale);

                } else {
                    main_pane.setVvalue(0);
                    main_pane.setHvalue(0);
                    new_scale.setPivotY(main_pane.getWidth() / 2);
                    new_scale.setX(zoom_out_factor);
                    new_scale.setY(zoom_out_factor);
                    main_draw.getTransforms().add(new_scale);
                }
            }
        } else {
            if (deltaY < 0) {
                new_scale.setPivotY(main_pane.getWidth() / 2);
                main_pane.setVvalue(0);
                main_pane.setHvalue(0);
                new_scale.setX(zoom_out_factor);
                new_scale.setY(zoom_out_factor);
                main_draw.getTransforms().add(new_scale);
            } else {
                new_scale.setPivotX(h_scroll_pos + event.getX());
                new_scale.setPivotY(v_scroll_pos + event.getY());
                new_scale.setX(zoom_in_factor);
                new_scale.setY(zoom_in_factor);
                main_draw.getTransforms().add(new_scale);
            }
        }
        if (!main_draw.getTransforms().isEmpty()) {
            main_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            main_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            default_zoom.setSelected(false);

        } else {
            main_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            main_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
            default_zoom.setSelected(true);
        }
        event.consume();
    }

    public void resetZoom() {
        main_draw.getTransforms().clear();
        main_pane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        main_pane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        main_pane.setVvalue(0);
        main_pane.setHvalue(0);
        default_zoom.setSelected(true);
    }

    public void showAbout() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About Me");
        alert.setHeaderText(null);
        alert.setContentText("Author: Ngo Kien Tuan\nStudent ID: 16021211\nClass: K61_C-CLC\nUniversity: UET");
        alert.showAndWait();
    }

}
