--Task 8
select table_name, round((num_rows*avg_row_len)/(1024*1024)) MB 
from all_tables 
where owner like 'VALOCOP'
and num_rows > 0
order by MB desc