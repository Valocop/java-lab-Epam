package com.epam.lab.model;

import java.util.Date;
import java.util.Objects;

public class News {
    private long id;
    private String title;
    private String shortText;
    private String fullText;
    private Date creationDate;
    private Date modificationDate;

    public News(long id, String title, String shortText, String fullText, Date creationDate, Date modificationDate) {
        this.id = id;
        this.title = title;
        this.shortText = shortText;
        this.fullText = fullText;
        this.creationDate = creationDate;
        this.modificationDate = modificationDate;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getShortText() {
        return shortText;
    }

    public String getFullText() {
        return fullText;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getModificationDate() {
        return modificationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        News news = (News) o;
        return id == news.id &&
                title.equals(news.title) &&
                shortText.equals(news.shortText) &&
                fullText.equals(news.fullText) &&
                creationDate.equals(news.creationDate) &&
                Objects.equals(modificationDate, news.modificationDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, shortText, fullText, creationDate, modificationDate);
    }

    @Override
    public String toString() {
        return "News{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", shortText='" + shortText + '\'' +
                ", fullText='" + fullText + '\'' +
                ", creationDate=" + creationDate +
                ", modificationDate=" + modificationDate +
                '}';
    }
}
