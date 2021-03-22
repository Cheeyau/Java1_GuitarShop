package nl.exam.model;

import java.io.Serializable;

public class WelcomeMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    private long Id;
    private String title;
    private String content;

    public WelcomeMessage(long id, String title, String content) {
        Id = id;
        this.title = title;
        this.content = content;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
