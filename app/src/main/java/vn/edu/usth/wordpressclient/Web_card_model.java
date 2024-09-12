package vn.edu.usth.wordpressclient;

public class Web_card_model {
    String web_title;
    String web_domain;
    int web_icon;

    public Web_card_model(int web_icon, String web_domain, String web_title) {
        this.web_icon = web_icon;
        this.web_domain = web_domain;
        this.web_title = web_title;
    }

    public String getWeb_title() {
        return web_title;
    }

    public String getWeb_domain() {
        return web_domain;
    }

    public int getWeb_icon() {
        return web_icon;
    }
}
