package vn.edu.usth.wordpressclient.utils;

public class DomainManager {
    private static DomainManager instance;
    private String selectedDomain;

    private DomainManager() {
        // Private constructor to prevent instantiation
    }

    public static synchronized DomainManager getInstance() {
        if (instance == null) {
            instance = new DomainManager();
        }
        return instance;
    }

    public void setSelectedDomain(String domain) {
        this.selectedDomain = domain;
    }

    public String getSelectedDomain() {
        return selectedDomain;
    }

}
