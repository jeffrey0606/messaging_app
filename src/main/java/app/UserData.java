package app;

public class UserData {
    public static String name;
    public static String profile;
    public static int id;

    public void setName(String userName) {
        name = userName;
    }

    public void setProfile(String profileImage) {
        profile = profileImage;
    }

    public void setId(int user_id) {
        id = user_id;
    }

    public String getName() {
        return name;
    }

    public String getProfile() {
        return  profile;
    }

    public int getId() {
        return id;
    }
}
