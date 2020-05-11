--Task 6
CREATE TABLE LOGGING 
(
  ID NUMBER NOT NULL, 
  INSERTION_DATE DATE NOT NULL, 
  TABLE_NAME VARCHAR(150) NOT NULL, 
  INSERTED_DATA VARCHAR(3000) NOT NULL, 
  CONSTRAINT LOGGING_PK PRIMARY KEY 
  (
    ID 
  )
  ENABLE 
);

CREATE OR REPLACE TRIGGER before_news_insertion
BEFORE INSERT
ON news
FOR EACH ROW
BEGIN
    insert_log('news', 'id ' || :new.id || '; ' || 'creation_date ' || :new.creation_date || '; ' || 
                'full_text ' || :new.full_text || '; ' || 'modification_date ' || :new.modification_date || '; ' ||
                'short_text ' || :new.short_text || '; ' || 'title ' || :new.title);
END;
    
CREATE OR REPLACE TRIGGER before_authors_insertion
BEFORE INSERT
ON author
FOR EACH ROW
BEGIN
    insert_log('author', 'id ' || :new.id || '; ' || 'name ' || :new.name || '; ' || 'surname ' || :new.surname);
END;

CREATE OR REPLACE PROCEDURE insert_log(table_n IN VARCHAR, inserted_d IN VARCHAR)
IS
BEGIN
    INSERT INTO logging(insertion_date, table_name, inserted_data)
    VALUES (SYSDATE, table_n, inserted_d);
END;