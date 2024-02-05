module com.mycompany.to.do_list {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.to.do_list to javafx.fxml;
    exports com.mycompany.to.do_list;
}
