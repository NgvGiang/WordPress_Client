package vn.edu.usth.wordpressclient.model;

public class CommentCardModel {
    private Long id;
    private Long post;
    private Long parent;
    private Long author;
    private String authorName;
    private String date;
    private String content;
    private String status;
    private String authorAvatar;

    public CommentCardModel() {

    }

    public CommentCardModel(Long id, Long parent, Long post, Long author, String authorName, String date, String content, String status, String authorAvatar) {
        this.id = id;
        this.parent = parent;
        this.post = post;
        this.author = author;
        this.authorName = authorName;
        this.date = date;
        this.content = content;
        this.status = status;
        this.authorAvatar = authorAvatar;
    }

    public Long getId() {
        return id;
    }

    public Long getPost() {
        return post;
    }

    public Long getParent() {
        return parent;
    }

    public Long getAuthor() {
        return author;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public String getStatus() {
        return status;
    }

    public String getAuthorAvatar() {
        return authorAvatar;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
