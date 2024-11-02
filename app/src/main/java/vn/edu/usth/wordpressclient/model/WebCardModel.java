package vn.edu.usth.wordpressclient.model;

public class WebCardModel {
    String web_title;
    String web_domain;
    String web_icon_url;

    public WebCardModel(String web_icon_url, String web_domain, String web_title) {
        this.web_icon_url = web_icon_url;
        this.web_domain = web_domain;
        this.web_title = web_title;
    }

    public String getWeb_title() {
        return web_title;
    }

    public String getWeb_domain() {
        return web_domain;
    }

    public String getWeb_icon_url() {
        return web_icon_url;
    }
}
