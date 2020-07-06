--Task 4
SELECT news.id, news.title,
    (LISTAGG ( tag.name, ', ') 
     WITHIN GROUP (ORDER BY news_tag.tag_id) ) as tags
FROM news_tag
    JOIN tag ON tag.id = news_tag.tag_id
    RIGHT JOIN news ON news.id = news_tag.news_id
GROUP BY news.id, news.title