--Task 3
CREATE OR REPLACE FUNCTION READ_TAGS(id_news IN NUMBER, separator IN VARCHAR)
RETURN VARCHAR2
DETERMINISTIC
IS
tags NVARCHAR2(300);

BEGIN
    SELECT 
    (LISTAGG ( tag.name, separator) 
     WITHIN GROUP (ORDER BY tag_id) )
INTO tags
FROM news_tag
    JOIN tag ON tag.id = news_tag.tag_id
WHERE news_tag.news_id = id_news
GROUP BY news_tag.news_id;

RETURN(tags);
END;

SELECT news.id, READ_TAGS(1 , ',') as all_tags
    FROM news
WHERE id = 1   