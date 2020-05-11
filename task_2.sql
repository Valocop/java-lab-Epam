--Task 2
WITH step_one AS (
    SELECT ouu.author_id as a_id, AVG(LENGTH(news.full_text)) as avg_lenght,
    (
        SELECT MAX(LENGTH(news.full_text))
            FROM news_author inn
            JOIN news ON news.id = inn.news_id
            WHERE inn.author_id = ouu.author_id
    ) as max_lenght
    FROM news_author ouu
        JOIN news ON ouu.news_id = news.id 
    GROUP BY ouu.author_id
)

SELECT step_one.a_id, author.name 
FROM step_one
    JOIN author ON author.id = step_one.a_id
WHERE avg_lenght > 500 AND max_lenght > 3000
