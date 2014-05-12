package br.com.rtools.principal;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBExternal {

    private Statement statment;

    public Connection getConnection() {
        try {
            String url = "jdbc:postgresql://200.158.101.9:5432/Rtools";
            Properties props = new Properties();
            props.setProperty("user", "postgres");
            props.setProperty("password", "r#@tools");
            //props.setProperty("ssl", "true");
            Connection conn = DriverManager.getConnection(url, props);            
            return conn;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return null;
    }

    public Statement getStatment() throws SQLException {
        statment = getConnection().createStatement();
        return statment;
    }

    public void setStatment(Statement statment) {
        this.statment = statment;
    }
    public void closeStatment() throws SQLException {
        getConnection().close();
    }
}
