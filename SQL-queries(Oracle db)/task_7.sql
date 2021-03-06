--Task 7
WITH step_one as (
SELECT USED.TABLESPACE_NAME as table_space, USED.USED_BYTES AS used_mb,  FREE.FREE_BYTES AS free_mb
FROM
(SELECT TABLESPACE_NAME,TO_CHAR(SUM(NVL(BYTES,0))/(1024*1024), '99,999,990.99') AS USED_BYTES 
    FROM USER_SEGMENTS 
    GROUP BY TABLESPACE_NAME) USED
INNER JOIN
(SELECT TABLESPACE_NAME,TO_CHAR(SUM(NVL(BYTES,0))/(1024*1024), '99,999,990.99') AS FREE_BYTES 
    FROM  USER_FREE_SPACE 
    GROUP BY TABLESPACE_NAME) FREE
ON (USED.TABLESPACE_NAME = FREE.TABLESPACE_NAME)
)

SELECT table_space, (free_mb / used_mb)
FROM step_one