--Task 5
WITH random as (
    SELECT author.id a_id
    FROM author
    ORDER BY dbms_random.value
), random_rn as (
    SELECT random.a_id as a_id, ROW_NUMBER() OVER (ORDER BY ROWNUM) AS rn
    FROM random
), random_rvs as (
    SELECT random.a_id as b_id
    FROM random
    ORDER BY ROWNUM DESC
), random_rvs_rn as (
    SELECT random_rvs.b_id as b_id, ROW_NUMBER() OVER (ORDER BY ROWNUM) AS rn
    FROM random_rvs
)

SELECT DISTINCT random_rn.a_id, random_rvs_rn.b_id
FROM random_rn
    JOIN random_rvs_rn ON random_rn.rn = random_rvs_rn.rn

