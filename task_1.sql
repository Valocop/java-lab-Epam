CREATE OR REPLACE VIEW author_info AS 
    WITH step_one AS (
    SELECT
        news_author.author_id   AS a_id,
        news_tag.tag_id         AS t_id,
        COUNT(news_tag.tag_id) AS cnt,
        ROW_NUMBER() OVER(ORDER BY COUNT(news_tag.tag_id) DESC) AS rn
    FROM
        news_author
        INNER JOIN news_tag ON news_author.news_id = news_tag.news_id
    GROUP BY
        news_author.author_id,
        news_tag.tag_id
    ), step_two AS (
    SELECT DISTINCT ou.a_id,
    (
        SELECT  inn.t_id
        FROM step_one inn
        WHERE rn = (SELECT MIN(rn) FROM step_one WHERE a_id = ou.a_id)
    )as tag_id
    FROM step_one ou
    GROUP BY ou.a_id, ou.t_id
    )
    SELECT author.id as author_id, author.name as author_name, step_two.tag_id as tag_id, 
        tag.name as popular_tag, COUNT(news_author.news_id) as news_count
    FROM author
        LEFT OUTER JOIN step_two ON author.id = step_two.a_id
        LEFT OUTER JOIN tag ON tag.id = step_two.tag_id
    LEFT OUTER JOIN news_author ON author.id = news_author.author_id 
    GROUP BY author.id, author.name, step_two.tag_id, tag.name
    ORDER BY author.id;
