import javafx.stage.*;
import javafx.application.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Orientation;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.geometry.Pos;
import javafx.scene.*;
import java.sql.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.lang.Enum;
//javac --module-path "C:\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml Home.java
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.Action;
import javax.xml.catalog.Catalog;
import javax.xml.catalog.CatalogException;

//java --module-path "C:\javafx-sdk-18\lib" --add-modules javafx.controls,javafx.fxml Home
//set classpath=D:\mysql-connector-java-8.0.28.jar;.;

public class Home extends Application {
    private Label err;
    private Label username_label,password_label;
    private Label test;
    private TextField username ;
    private PasswordField password ;
    private Button submit_button;
    private ComboBox place_list;
    private Scene scene;
    private String app_type;
    private TextField uid;
    private TextField eid;
    private VBox Form;
    private PasswordField pwd1;
    private VBox second_window_vb;
    public String getType() {
        return app_type;
    }

    public boolean Register(String uid, String eid, String city, String pwd) {
        boolean flag = true;
        
        try {
            Font f2 = new Font("Calibri", 20);
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEBOPAM", "root","debopam03");        
            Statement stmt1 = con.createStatement();
            
            Statement stmt2 = con.createStatement();
            
            ResultSet rs1 = stmt1.executeQuery("SELECT * FROM USERS WHERE USERNAME = '" + uid + "';");
            ResultSet rs2 = stmt2.executeQuery("SELECT * FROM USERS WHERE EMAIL = '" + eid + "';");
            
            boolean flag1 = rs1.next();
            boolean flag2 = rs2.next();
            

            System.out.println(flag1);
            System.out.println(flag2);
            Pattern pattern = Pattern.compile("^([A-Za-z0-9]+)@([A-Za-z0-9]+).com$");
            Matcher matcher = pattern.matcher(eid);
            if (uid.equals("") || eid.equals("") || city.equals("") || pwd.equals("")) {
                System.out.println(city);
                System.out.println(uid);
                System.out.println(eid);
                System.out.println(pwd);
                flag = false;
            }
            else if (!matcher.matches()) {
                Label err = new Label("Invalid Email ID");
                second_window_vb.getChildren().addAll(new Label(""), err);
                pwd1.setText("");
                this.eid.setText("");
            }
            else if ((!flag1 && !flag2)) {
                Statement stmt3 = con.createStatement();
                stmt3.executeUpdate("INSERT INTO USERS VALUES ('" + uid + "', '" + eid + "', '" + city + "', '" + pwd + "');");
                Label success = new Label("Registration Successful. You can log in now.");
                success.setFont(new Font("Calibri", 30));
                this.uid.setText("");
                this.pwd1.setText("");
                this.eid.setText("");
                place_list.setValue("--select--");
                second_window_vb.getChildren().addAll(new Label(""), success);
        
            }
            else if (!flag1) {
                Label err = new Label("Email ID already registered");
                second_window_vb.getChildren().addAll(new Label(""), err);
                pwd1.setText("");

            }
            else if (!flag2) {
                Label err = new Label("Username already registered");
                second_window_vb.getChildren().addAll(new Label(""), err);
                pwd1.setText("");
            }
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    public void setType(String k) {

        app_type = k.trim();
    }

    private String uname, pwd;


    public boolean Login(String uid, String pwd) {
        boolean flag = false;
        try {
            
            Class.forName("com.mysql.jdbc.Driver");  
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEBOPAM", "root","debopam03");        
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME = '" + uid + "' AND PASSWORD = '" + pwd + "';");
            flag = rs.next();
            if (!flag) {
                rs = stmt.executeQuery("SELECT * FROM USERS WHERE USERNAME = '" + uid + "';");
                if (!rs.next()) {
                    err.setText("Invalid User Name and Password.");
                    password.setText("");
                    username.setText("");
                }
                else {
                    err.setText("Invalid Password.");
                    password.setText("");
                }

            }
            /*else {
                uname = uid;
                this.pwd = pwd; 
            }*/
            con.close();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }
    
    public void start(Stage home) {
        try {
            
            Font f = new Font("Calibri", 30);
            Tab t2 = new Tab("Upcoming Appointments");
        TabPane pane = new TabPane();
        pane.setPrefSize(1920/1.25, 1080/1.25);
        Scene sc = new Scene(pane, 1920 / 1.25, 1080 / 1.25);
        
        username = new TextField();
        username_label = new Label("Username : ") ;
        
        
        password = new PasswordField();
        password_label = new Label("Password : ") ;
        

        HBox hb1 = new HBox(30);
        HBox hb2 = new HBox(30) ;
        submit_button = new Button("Submit");
        Button newregb = new Button("New User? Register Here");
        newregb.setAlignment(Pos.CENTER);
        hb1.setAlignment(Pos.CENTER);
        
        Font f3 = new Font("Calibri", 15.0);
        hb2.setAlignment(Pos.CENTER);

        Form=new VBox(20);
        Form.setMinSize(1920 / 1.25, 1080 / 1.25);
        Form.setAlignment(Pos.TOP_CENTER);

        Font f2 = new Font("Calibri", 20.0);
        //TabPane tabpane=new TabPane();
        //Tab tab1=new Tab("Login Page");

        hb1.getChildren().add(username_label);
        hb1.getChildren().add(username);
        hb2.getChildren().add(password_label) ;
        hb2.getChildren().add(password);
        

        Form.getChildren().add(hb1);
        Form.getChildren().add(hb2);
        Form.getChildren().add(submit_button);
        Form.getChildren().add(newregb);
        ScrollPane sp3 = new ScrollPane(Form);
        
        //tab1.setContent(sp3);
        //tabpane.getTabs().add(tab1);

        uid = new TextField();
        eid = new TextField();
        pwd1 = new PasswordField();
        Label uid_label = new Label("User Name: ");
        Label eid_label = new Label("Email ID: ");
        Label pwd1_label = new Label("Password: ");
        
        //uid_label.setPrefSize(arg0, arg1);
        VBox i3 = new VBox();

        HBox hb3 = new HBox(uid_label, uid);
        HBox hb4 = new HBox(eid_label, eid);
        HBox hb5 = new HBox(pwd1_label, pwd1);
        hb3.setAlignment(Pos.CENTER);
        hb4.setAlignment(Pos.CENTER);
        hb5.setAlignment(Pos.CENTER);
        second_window_vb = new VBox(hb3, new Label(""), hb4, new Label(""), hb5);
        PasswordField pwd1 = new PasswordField();
        Label place_label=new Label("Enter Your Location: ");
        place_list=new ComboBox();
        place_list.getItems().addAll("--select--","Vellore","Banglore","Chennai");
        place_list.setValue("--select--");
        HBox second_window_hb=new HBox(10);
        second_window_hb.getChildren().addAll(place_label, place_list);
        
        Button reg = new Button("SUBMIT");
        reg.setAlignment(Pos.CENTER);
        EventHandler<ActionEvent> regCheck = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    String place = place_list.getValue().toString();
                    String passw = Home.this.pwd1.getText();
                    Register(uid.getText(), eid.getText(), place, passw);
                    
            }
            catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        }; 
        reg.setOnAction(regCheck);
        Button logpage = new Button("Return to Login Page");
        logpage.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e)   {
                try {
                    home.setScene(scene);
                }
                catch (Exception e2) {
                    e2.printStackTrace();
                }
            }
        });
        second_window_vb.setAlignment(Pos.TOP_CENTER);
        second_window_vb.getChildren().addAll(new Label(""), second_window_hb, new Label(""), reg, new Label(""), logpage);
        
        
        VBox w3 = new VBox();
        //Tab tab2 = new Tab("Register");
        second_window_hb.setAlignment(Pos.CENTER);
        second_window_vb.setPrefSize(1920/1.25, 1080/1.25);
        //tab2.setContent(new ScrollPane(second_window_vb));
        //tabpane.getTabs().add(tab2);
        Scene regsc = new Scene(new ScrollPane(second_window_vb));
        
        scene = new Scene(sp3,1920/1.25,1080/1.25);
        home.setScene(scene);
        home.setTitle("LOGIN PAGE");
        home.show();
        err = new Label();
        Form.getChildren().addAll(new Label(""), err);
        EventHandler<ActionEvent> homepage = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    uname = username.getText();
                    if (Login(username.getText(), password.getText())) {
                        Class.forName("com.mysql.jdbc.Driver");  
                        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEBOPAM", "root","debopam03");
                    
        Statement stmt = con.createStatement();
        System.out.println(uname);
        ResultSet rs2 = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE (CUST_UID = '" + uname + "' AND APP_DATE > CURDATE());");
        Label success = new Label();
        Border br = new Border(
                new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                        new BorderWidths(2)));
        w3.getChildren().add(success);
        if (rs2.next()) {
            success.setText("UPCOMING APPOINTMENTS:");
            success.setAlignment(Pos.CENTER);
            success.setFont(f);

                            List<HBox> hbl = new ArrayList<>();
                            System.out.println("SELECT * FROM DOCTORS WHERE UID = " + rs2.getInt(2) + ";");
                            Statement stmt1 = con.createStatement();
                            ResultSet rs9 = stmt1.executeQuery("SELECT * FROM DOCTORS WHERE UID = " + rs2.getInt(2) + ";");
                            rs9.next();
                            System.out.println(rs9.getString(2));
            Label r1 = new Label(rs9.getString(2)), r2 = new Label(rs2.getString(3));
            r1.setPrefSize(300, 10);
            r2.setPrefSize(300, 10);
            r1.setFont(f3);
            r2.setFont(f3);
                            r1.setBorder(br);
            r2.setBorder(br);
            hbl.add(new HBox(r1, r2));
            
                            while (rs2.next()) {
                                rs9 = stmt1.executeQuery("SELECT * FROM DOCTORS WHERE UID = " + rs2.getInt(2) + ";");
                                rs9.next();
                r1 = new Label(rs9.getString(2));
                r2 = new Label(rs2.getString(3));
                r1.setPrefSize(300, 10);
                r2.setPrefSize(300, 10);
                r1.setFont(f3);
                r2.setFont(f3);
                                r1.setBorder(br);
                
                                r2.setBorder(br);
                hbl.add(new HBox(r1, r2));
            }
            Iterator<HBox> ith = hbl.iterator();
            while (ith.hasNext()) {
                w3.getChildren().add(ith.next());
            }
        }
                        else {
                            success.setText("NO UPCOMING APPOINTMENTS");
                            success.setFont(f);
                        }
                        ResultSet rs3 = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE (CUST_UID = '" + uname + "' AND APP_DATE < CURDATE());");
                        if (rs3.next()) {
                            success = new Label("APPOINTMENTS HISTORY:");
                            success.setAlignment(Pos.CENTER);
                            success.setFont(f);
                
                            List<HBox> hbl = new ArrayList<>();
                            Label r1 = new Label(rs3.getString(2)), r2 = new Label(rs3.getString(3));
                            r1.setPrefSize(300, 10);
                            r2.setPrefSize(300, 10);
                            r1.setFont(f3);
                            r2.setFont(f3);
                            r1.setBorder(br);
                            r2.setBorder(br);
                            hbl.add(new HBox(r1, r2));
                
                            while (rs3.next()) {
                                r1 = new Label(rs3.getString(2));
                                r2 = new Label(rs3.getString(3));
                                r1.setPrefSize(300, 10);
                                r2.setPrefSize(300, 10);
                                r1.setFont(f3);
                                r2.setFont(f3);
                                r1.setBorder(br);
                                
                                r2.setBorder(br);
                                hbl.add(new HBox(r1, r2));
                            }
                            Iterator<HBox> ith = hbl.iterator();
                            i3.getChildren().add(success);
                            while (ith.hasNext()) {
                                i3.getChildren().add(ith.next());
                            }
                        }
                        else {
                            success = new Label("NO PREVIOUS APPOINTMENTS");
                            success.setFont(f);
                            
                            i3.getChildren().add(success);
                        }
                        home.setScene(sc);
                        home.setTitle("Home");
                
                }
                }
            catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };
        EventHandler<ActionEvent> newreg = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
                try {
                    home.setScene(regsc);
                }
            catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        };
        submit_button.setOnAction(homepage);
        /*EventHandler<ActionEvent> homepage = new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {
            }
        }*/

        
            
        
        Button l[] = new Button[8];
        l[0] = new Button();
        l[1] = new Button();
        l[2] = new Button(); //Neurologist
        l[3] = new Button(); //Otolaryngologist
        l[4] = new Button(); //Pediatrician

        l[5] = new Button(); //Cardiologist
        l[6] = new Button(); //Dermatologist
        l[7] = new Button(); //Gastroenterologist
        //Image img = new Image();
        Image img[] = new Image[8];
        ImageView view[] = new ImageView[8];
        
        Label la[] = new Label[8];
        
        la[0] = new Label("Endocrinologist".toUpperCase());
        la[1] = new Label("Family Physician".toUpperCase());
        la[2] = new Label("Neurologist".toUpperCase());
        la[3] = new Label("Otolaryngologist".toUpperCase());
        la[4] = new Label("Pediatrician".toUpperCase());
        la[5] = new Label("Cardiologist".toUpperCase());
        la[6] = new Label("Dermatologist".toUpperCase());
        la[7] = new Label("Gastroenterologist".toUpperCase());
        
        
        

        for (int i = 0; i < 8; i++) {
            la[i].setFont(f3);
            la[i].setAlignment(Pos.CENTER);
        }
        
        img[0] = new Image("endo.jpg");
        img[1] = new Image("family_physician.jpg");
        img[2] = new Image("nervous_system.jpg");
        img[3] = new Image("ENT.jpg");
        img[4] = new Image("pediatrician.jpg");
        img[5] = new Image("cardio.jpg");
        img[6] = new Image("skin.jpg");
        img[7] = new Image("gastro.jpg");
        //ScrollBar sb = new ScrollBar();
        for (int i = 0; i < 8; i++) {
            view[i] = new ImageView(img[i]);
            view[i].setFitHeight(200);
            view[i].setFitWidth(200);
        }
        
        for (int i = 0; i < 8; i++) {
            l[i].setGraphic(view[i]);
            l[i].setAlignment(Pos.CENTER);
        }
        VBox vb1 = new VBox();
        VBox vb2 = new VBox();
        for (int i = 0; i < 4; i++) {
            vb1.getChildren().addAll(l[i], la[i]);
        }
        for (int i = 4; i < 8; i++) {
            vb2.getChildren().addAll(l[i], la[i]);
        }
        

        vb1.setAlignment(Pos.CENTER);
        vb2.setAlignment(Pos.CENTER);
        
        newregb.setOnAction(newreg);

        List<EventHandler<ActionEvent>> event = new ArrayList<>(); 
        for (int i = 0; i < 8; i++) {
            final int iii = i;
            event.add(new EventHandler<ActionEvent>() {
                public void handle(ActionEvent e)
                {
                    try{
                    setType(la[iii].getText());
                    Label l = new Label(getType());
                    
                    Font f = new Font("Calibri", 30);
                    
                    Label l1 = new Label(
                        "AVAILABLE DOCTORS");
                    l1.setAlignment(Pos.CENTER);
                    l1.setFont(f);
                    VBox vb = new VBox(l1);
                    
                    Class.forName("com.mysql.jdbc.Driver");  
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEBOPAM", "root","debopam03");
                    
                        Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery("SELECT * from DOCTORS WHERE TYPE = '" + getType() + "';");
                    
                    List<Label> name = new ArrayList<>();
                    
                    List<Label> city = new ArrayList<>();
                    
                    List<Label> rating = new ArrayList<>();
                    
                    
                        
                    Border br = new Border(
                        new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                                new BorderWidths(2)));
                        while (rs.next()) {
                            name.add(new Label(rs.getString(2)));
                            name.get(name.size() - 1).setFont(f2);
                            name.get(name.size() - 1).setBorder(br);
                            name.get(name.size() - 1).setPrefSize(300, 10);

                            city.add(new Label("" + rs.getString(3)));
                            city.get(city.size() - 1).setFont(f2);
                            city.get(city.size() - 1).setBorder(br);
                            city.get(name.size() - 1).setPrefSize(300, 10);

                            rating.add(new Label("" + Integer.toString(rs.getInt(5))));
                            rating.get(rating.size() - 1).setFont(f2);
                            rating.get(rating.size() - 1).setBorder(br);
                            rating.get(name.size() - 1).setPrefSize(300, 10);
                        }
                        Label namecol = new Label("NAME"), citycol = new Label("CITY"), ratingcol = new Label("RATING");
                        namecol.setFont(f2);
                        citycol.setFont(f2);
                        ratingcol.setFont(f2);
                        namecol.setBorder(br);
                        citycol.setBorder(br);
                        ratingcol.setBorder(br);
                        namecol.setPrefSize(300, 10);
                        citycol.setPrefSize(300, 10);
                        ratingcol.setPrefSize(300, 10);
                        

                        HBox hb1 = new HBox(namecol, citycol, ratingcol);
                        hb1.setAlignment(Pos.CENTER);
                        List<HBox> list = new ArrayList<>();
                        Iterator it1 = name.iterator();
                        int count = 0;

                        while (it1.hasNext()) {
                            name.get(count).setAlignment(Pos.CENTER);
                            city.get(count).setAlignment(Pos.CENTER);
                            rating.get(count).setAlignment(Pos.CENTER);

                            HBox temp = new HBox(name.get(count), city.get(count), rating.get(count));
                            
                            temp.setAlignment(Pos.CENTER);

                            list.add(temp);
                            it1.next();
                            count++;
                        }

                        count = 0;
                        it1 = list.iterator();
                        vb.getChildren().add(hb1);
                        while (it1.hasNext()) {
                            vb.getChildren().add(list.get(count));
                            it1.next();
                            count++;
                        }
                        List<String> name_str = new ArrayList<>();
                        it1 = name.iterator();
                        count = 0;
                        while( it1.hasNext() ) {
                            name_str.add(name.get(count).getText());
                            it1.next();
                            count++;
                        }
                        ComboBox combo_box =
                                new ComboBox(FXCollections.observableList(name_str));
                        combo_box.setValue(name_str.get(0));
                        Button submit = new Button("APPLY");

                        EventHandler<ActionEvent> arg0 = new EventHandler<ActionEvent>() {
                            public void handle(ActionEvent e) {
                                try {Class.forName("com.mysql.jdbc.Driver");  
                                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/DEBOPAM", "root","debopam03");
                    
                                Statement stmt = con.createStatement();
                                ResultSet rs2 = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE (CUST_UID = '" + uname + "' AND DOC_UID = '" + combo_box.getValue() + "' AND APP_DATE > CURDATE());");
                                Label success;
                                if(!rs2.next())  {

                                        System.out.print(combo_box.getValue());
                                        ResultSet rs7 = stmt.executeQuery(
                                                "SELECT * FROM DOCTORS WHERE NAME = '" + combo_box.getValue() + "';");
                                        rs7.next();
                                                int f = rs7.getInt(1);
                                stmt.executeUpdate("INSERT INTO APPOINTMENT VALUES('" + uname + "', " + f + ", DATE_ADD(CURDATE(), INTERVAL 3 DAY));");
                                
                                        success = new Label("APPOINTMENT SUCCESSFUL!");
                                }
                                else{
                                    success = new Label("APPOINTMENT ALREADY EXISTS! TRY AGAIN");
                                }
                                    success.setFont(f);
                                    Border br1 = new Border(
                                    new BorderStroke(Color.GREEN, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                                new BorderWidths(2)));
                                success.setBorder(br1);
                                success.setAlignment(Pos.CENTER);
                                vb.getChildren().addAll(new Label(" "), success);
                                submit.setDisable(true);
                            }
                            catch (Exception e2) {
                                e2.printStackTrace();
                            }
                                
                            }
                        };

                        submit.setOnAction(arg0);
                        submit.setAlignment(Pos.CENTER);
                        vb.getChildren().addAll(new Label(" "), combo_box, new Label(" "), submit);

 
                    con.close();  
                    Button back = new Button("RETURN TO HOME PAGE");
                    EventHandler<ActionEvent> arg1 = new EventHandler<ActionEvent>() {
                        public void handle(ActionEvent e)   {
                                home.setScene(sc);
                                home.getScene().getWindow().setWidth(home.getScene().getWidth() + 0.001);
                        }
                    };
                        back.setOnAction(arg1);
                        back.setAlignment(Pos.CENTER);
                        vb.getChildren().addAll(new Label(" "),
                                back);
                        vb.setAlignment(Pos.TOP_CENTER);
                        
                        vb.setPrefSize(1920 / 1.25, 1080 / 1.25);
                        
                        ScrollPane sPane = new ScrollPane(vb);
                        sPane.setPrefSize(1920/1.25, 1080/1.25);
                    Scene sc2 = new Scene(sPane, 1920/1.25, 1080/1.25);
                    
                    home.setScene(sc2);
                    }
                    catch (Exception e1) {
                        e1.printStackTrace();
                    }
                }
            });
        }
        Iterator<EventHandler<ActionEvent>> it = event.iterator();
        int ii = 0;
        while(it.hasNext()) {
            l[ii].setOnAction(it.next());
            ii++;
        }
        
        
        HBox hb = new HBox(vb1, vb2);
        hb.setAlignment(Pos.CENTER);
        Label l_head = new Label("WELCOME TO THE PORTAL");
        Button btt = new Button("Log Out");
        btt.setOnAction(new EventHandler<ActionEvent>() {
                
                public void handle(ActionEvent e) {
                    try {
                        
                    home.setScene(scene);
                    uid.setText("");
                    pwd1.setText("");
                    username.setText("");
                    password.setText("");
                    eid.setText("");
                    place_list.setValue("--select--");
                    }
                    catch(Exception e2) {

                    }

                }

            }
        
        );
        HBox hb10 = new HBox(new Label("\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t"), btt);
        l_head.setFont(f);
        btt.setAlignment(Pos.CENTER_RIGHT);

        hb10.setAlignment(Pos.CENTER);
        Label pick = new Label("PICK YOUR DESIRED SERVICE");
        pick.setFont(f2);
        l_head.setAlignment(Pos.CENTER);
        pick.setAlignment(Pos.CENTER);
        VBox vb3 = new VBox(hb10, l_head, pick, hb);
        vb3.setAlignment(Pos.TOP_CENTER);
        
        vb3.setPrefSize(1920/1.25, 1080/1.25);
        ScrollPane sp = new ScrollPane();
        sp.setContent(vb3);
        sp.setVbarPolicy(ScrollBarPolicy.AS_NEEDED);
        
        Tab t1 = new Tab("Make an Appointment");
        t1.setContent(sp);
        ScrollPane sp2 = new ScrollPane();
        Label h5 = new Label("LIST OF APPOINTMENTS");
        h5.setFont(f);
        h5.setAlignment(Pos.CENTER);

        VBox vb5 = new VBox(h5);
        sp2.setContent(vb5);          
        /*Statement stmt = con.createStatement();
        System.out.println(uname);
        ResultSet rs2 = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE (CUST_UID = '" + uname + "' AND APP_DATE > CURDATE());");
        Label success = new Label();
        Border br = new Border(
            new BorderStroke(Color.GRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY,
                    new BorderWidths(2)));
        VBox w3 = new VBox();
        w3.getChildren().add(success);
        if (rs2.next()) {
        success.setText("UPCOMING APPOINTMENTS:");
        success.setAlignment(Pos.CENTER);
        success.setFont(f);
        
        List<HBox> hbl = new ArrayList<>();
        Label r1 = new Label(rs2.getString(2)), r2 = new Label(rs2.getString(3));
        r1.setPrefSize(300, 10);
        r2.setPrefSize(300, 10);
        r1.setFont(f3);
        r2.setFont(f3);
        r1.setBorder(br);
        hbl.add(new HBox(r1, r2));
        
        while (rs2.next()) {
            r1 = new Label(rs2.getString(2));
            r2 = new Label(rs2.getString(3));
            r1.setPrefSize(300, 10);
            r2.setPrefSize(300, 10);
            r1.setFont(f3);
            r2.setFont(f3);
            r1.setBorder(br);
            hbl.add(new HBox(r1, r2));
        }
        Iterator<HBox> ith = hbl.iterator();
        while (ith.hasNext()) {
            w3.getChildren().add(ith.next());
        }
        }
        else {
        success.setText("NO UPCOMING APPOINTMENTS");
        success.setFont(f);
        }
        */
        
        /*ResultSet rs3 = stmt.executeQuery("SELECT * FROM APPOINTMENT WHERE (CUST_UID = '" + uname + "' AND APP_DATE < CURDATE());");
        if (rs3.next()) {
            success = new Label("APPOINTMENTS HISTORY:");
            success.setAlignment(Pos.CENTER);
            success.setFont(f);

            List<HBox> hbl = new ArrayList<>();
            Label r1 = new Label(rs3.getString(2)), r2 = new Label(rs3.getString(3));
            r1.setPrefSize(300, 10);
            r2.setPrefSize(300, 10);
            r1.setFont(f3);
            r2.setFont(f3);
            r1.setBorder(br);
            hbl.add(new HBox(r1, r2));

            while (rs3.next()) {
                r1 = new Label(rs3.getString(2));
                r2 = new Label(rs3.getString(3));
                r1.setPrefSize(300, 10);
                r2.setPrefSize(300, 10);
                r1.setFont(f3);
                r2.setFont(f3);
                r1.setBorder(br);
                hbl.add(new HBox(r1, r2));
            }
            Iterator<HBox> ith = hbl.iterator();
            while (ith.hasNext()) {
                i3.getChildren().add(ith.next());
            }
        }
        else {
            success = new Label("NO PREVIOUS APPOINTMENTS");
            success.setFont(f);
        }*/
        
        
        Tab t3 = new Tab("Previous Appointments");
        t2.setContent(new ScrollPane(w3));
        t3.setContent(new ScrollPane(i3));
        pane.getTabs().addAll(t1, t2, t3);

        
        
        
        home.setResizable(true);
        home.show();
    }
    catch (Exception e) {
        e.printStackTrace();
    }
    }

    public static void main(String args[])  {
        launch();
    }
}