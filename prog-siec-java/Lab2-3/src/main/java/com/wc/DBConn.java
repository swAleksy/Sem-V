package com.wc;

import java.sql.*;

public class DBConn {
    private Connection connection;
    private Statement statement;

    public void connect(){
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:wc.db");
            statement = connection.createStatement();

            statement.setQueryTimeout(30);
            System.out.println("Connected to db");
        } catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void createTables() {
        try {
            String createTableSQL = "CREATE TABLE IF NOT EXISTS links (id INTEGER PRIMARY KEY AUTOINCREMENT, link TEXT,  seen BOOLEAN DEFAULT 0)";
            statement.executeUpdate(createTableSQL);
            System.out.println("Table 'wc' created or already exists.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void insertRow(String link, int seen) {  
        try {
            String checkSQL = "SELECT seen FROM links WHERE link = ?";
            PreparedStatement checkStatement = connection.prepareStatement(checkSQL);
            checkStatement.setString(1, link);
            ResultSet rs = checkStatement.executeQuery();

            if (rs.next()) {
                int currentSeen = rs.getInt("seen");
                String updateSQL = "UPDATE links SET seen = ? WHERE link = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setInt(1, currentSeen + 1);
                updateStatement.setString(2, link);
                updateStatement.executeUpdate();
                System.out.println("Incremented seen for link: " + link);
            } else {
                String insertSQL = "INSERT INTO links (link, seen) VALUES (?, ?)";
                PreparedStatement insertStatement = connection.prepareStatement(insertSQL);
                insertStatement.setString(1, link);
                insertStatement.setString(2, String.valueOf(seen));
                insertStatement.executeUpdate();
                System.out.println("Inserted new link: " + link);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getUnseenLink() {
        String link = null;
        try {
            String selectSQL = "SELECT link FROM links WHERE seen = 0 ORDER BY id LIMIT 1";
            PreparedStatement selectStatement = connection.prepareStatement(selectSQL);
            ResultSet rs = selectStatement.executeQuery();

            if (rs.next()) {
                link = rs.getString("link");

                String updateSQL = "UPDATE links SET seen = 1 WHERE link = ?";
                PreparedStatement updateStatement = connection.prepareStatement(updateSQL);
                updateStatement.setString(1, link);
                updateStatement.executeUpdate();
                System.out.println("Marked link as seen: " + link);
            } else {
                System.out.println("No unseen links found.");
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return link;
    }

    public void dropTables() {
        try {
            String dropTableSQL = "DROP TABLE IF EXISTS links";
            statement.executeUpdate(dropTableSQL);
            System.out.println("Table 'links' dropped.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (statement != null) statement.close();
            if (connection != null) connection.close();
            System.out.println("Disconnected from the database.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void displayRows() {
        try {
            String selectSQL = "SELECT * FROM links";
            ResultSet rs = statement.executeQuery(selectSQL);
            while (rs.next()) {
                System.out.println("id: " + rs.getInt("id"));
                System.out.println("link: " + rs.getString("link"));
                System.out.println("seen: " + rs.getBoolean("seen"));
                System.out.println("occurrence: " + rs.getInt("occurrence"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
