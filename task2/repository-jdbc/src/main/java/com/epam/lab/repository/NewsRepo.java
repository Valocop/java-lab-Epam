package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;

public interface NewsRepo extends Repository<News, Long> {
    boolean createAuthorToNews(News news, Author author);
    boolean deleteAuthorOfNews(News news);
    boolean deleteNewsOfAuthor(Author author);
    boolean createTagToNews(News news, Tag tag);
    boolean deleteTagOfNews(News news, Tag tag);
    boolean deleteTagsOfNews(News news);
}
