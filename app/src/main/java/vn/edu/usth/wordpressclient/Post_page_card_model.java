package vn.edu.usth.wordpressclient;

public class Post_page_card_model {
    String post_date;
    String post_title;
    String post_content;

    public Post_page_card_model(String post_date, String post_title, String post_content) {
        this.post_date = post_date;
        this.post_title = post_title;
        this.post_content = post_content;
    }
    public String getPost_date() {
        return post_date;
    }
    public String getPost_title() {
        return post_title;
    }
    public String getPost_content() {
        return post_content;
    }

}
