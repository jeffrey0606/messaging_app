package app;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

public class Dbmanager extends App{

    PreparedStatement stmt;
    ResultSet resultSet;

    public boolean createChatSpace(String purpose, String name, String image, int adminId, ArrayList<Integer> membersID) throws SQLException {
        System.out.println(name);
        stmt = conn.prepareStatement("INSERT INTO chat_space(admin_id, name, purpose, image) VALUES(?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, adminId);
        stmt.setString(2, name);
        stmt.setString(3, purpose);
        stmt.setString(4, image);

        stmt.executeUpdate();

        resultSet = stmt.getGeneratedKeys();

        if(!membersID.isEmpty()) {
            int chatSpaceId = 0;

            while (resultSet.next()) {
                chatSpaceId = resultSet.getInt(1);
            }

            stmt.close();

            if(chatSpaceId != 0) {
                for(int i = 0; i < membersID.size(); i++) {
                    stmt = conn.prepareStatement("INSERT INTO chat_space_members(members_id, chat_space_id) VALUES(?, ?)");
                    stmt.setInt(1, membersID.get(i));
                    stmt.setInt(2, chatSpaceId);

                    if(stmt.executeUpdate() > 0) {
                        stmt.close();
                        continue;
                    } else {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } else {
            if(resultSet.next()) {
                return true;
            } else {
                return false;
            }
        }
    }

    public HashMap<String, Object> getUserInfos(int userId) throws SQLException {
        stmt = conn.prepareStatement("SELECT user_name, profile_image FROM users WHERE id=?");
        stmt.setInt(1, userId);
        resultSet = stmt.executeQuery();

        HashMap<String, Object> temp = new HashMap<String, Object>();

        while (resultSet.next()) {
            temp.put("user_name", resultSet.getString("user_name"));
            temp.put("profile_image", resultSet.getString("profile_image"));
            temp.put("id", userId);
        }

        stmt.close();
        return  temp;
    }

    public void addProfileImage(int id, String profileImageName) throws SQLException {
        stmt = conn.prepareStatement("UPDATE users SET profile_image=? WHERE id=?");
        stmt.setString(1, profileImageName);
        stmt.setInt(2, id);

        int rowAffected = stmt.executeUpdate();

        if(rowAffected > 0) {
            UserData userData = new UserData();
            userData.setProfile(profileImageName);
        }
    }

    public  boolean userAlreadyExits(String userName) {
        try {
            stmt = conn.prepareStatement("SELECT id FROM users WHERE user_name=?");
            stmt.setString(1, userName);

            resultSet = stmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                System.out.println(resultSet.getInt("id"));
                i++;
            }


            if(i > 0 ) {
                return true;
            } else {
                return false;
            }

        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return true;
    }

    public HashMap<String, Object> login(String userName, String password) {
        HashMap<String, Object> results = new HashMap<String, Object>();

        try {
            stmt = conn.prepareStatement("SELECT id FROM users WHERE user_name=? AND password=?");
            stmt.setString(1, userName);
            stmt.setString(2, password);

            resultSet = stmt.executeQuery();
            int i = 0;
            while (resultSet.next()) {
                results.put("user_id", resultSet.getInt("id"));
                i++;
            }

            stmt.close();
            if(i > 0 ) {
                results.put("status", true);
                return results;
            } else {
                results.put("status", false);
                return results;
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        results.put("status", false);
        return results;
    }

    public HashMap<String, Object> register(String userName, String password) {

        HashMap<String, Object> results = new HashMap<String, Object>();

        try {
            stmt = conn.prepareStatement("INSERT INTO users(user_name, password) VALUES(?, ?)", Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, userName);
            stmt.setString(2, password);
            stmt.executeUpdate();

            resultSet = stmt.getGeneratedKeys();

            if (resultSet.next()) {
                int id = resultSet.getInt(1);

                results.put("user_id", id);
                results.put("status", true);

                stmt.close();

                return results;
            } else {
                results.put("status", false);

                stmt.close();

                return results;
                // throw an exception from here
            }
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        results.put("status", false);
        return results;
    }
}
