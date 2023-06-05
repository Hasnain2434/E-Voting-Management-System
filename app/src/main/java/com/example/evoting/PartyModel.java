package com.example.evoting;

import java.net.URL;

public class PartyModel {
    String name;
    int votes;
    String url;

    public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public PartyModel(String name, int votes, String url) {
        this.name = name;
        this.votes = votes;
        this.url = url;
    }
}
