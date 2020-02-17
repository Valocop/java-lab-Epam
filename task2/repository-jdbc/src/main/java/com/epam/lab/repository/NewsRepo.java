package com.epam.lab.repository;

import com.epam.lab.model.Author;
import com.epam.lab.model.News;
import com.epam.lab.model.Tag;

public interface NewsRepo extends Repository<News, Long> {
    boolean createBindingOfAuthorAndNews(News news, Author author);

    boolean deleteBindingsOfNewsAndAutors(News news);

    boolean deleteBindingsOfAuthorAndNews(Author author);

    boolean createBindingOfNewsAndTags(News news, Tag tag);

    boolean deleteBindingOfNewsAndTag(News news, Tag tag);

    boolean deleteBindingsOfNewsAndAllTags(News news);
}
