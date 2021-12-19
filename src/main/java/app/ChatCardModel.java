package app;

public class ChatCardModel {
    private final String text;

    public ChatCardModel(String text) {

        this.text = text;

        System.out.println(this.text);
    }

    public void setText(String text) {

    }

    public String getText() {
        return text;
    }
}
