package com.epam.lab.dto;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;

import java.util.List;

public class NewsDto {
    private News news;
    private Author author;
    private List<Tag> tags;

    public News getNews() {
        return news;
    }

    public void setNews(News news) {
        this.news = news;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}
