# News-manager REST-api

**Sonarqube**
*  http://epbyminw8141.minsk.epam.com:9000/dashboard?id=com.epam.lab%3Anews

**Jenkins (developer/Qwe123)**
*	http://epbyminw8141.minsk.epam.com:8085/

**Deploy link**
*	http://epbyminw8141.minsk.epam.com:8080/news-manager/

**REST API**

***Author***
*	GET: /authors/id - Gets author by id;
*	POST: /authors - Persists author. Author should be sent using request-body in appropriate JSON-format;
*	PUT: /authors - Merge author. Author should be sent using request-body in appropriate JSON-format;
*	DELETE: /authors/id - Delete author by id.

***Tag***
*	GET: /tags/id - Gets tag by id;
*	POST: /tags - Persists tag. Tag should be sent using request-body in appropriate JSON-format;
*	PUT: /tags - Merge tag. Tag should be sent using request-body in appropriate JSON-format;
*	DELETE: /tags/id - Delete tag by id.

***News***
*	GET: /count - Gets count of news;
*	GET: /news/id - Gets news by id;
*	GET: /news?authors_name=...,...&tags_name=...,...&sort=... - Gets news according to search and sort conditions. See more about search and sort conditions in News controller Javadocs;
*	POST: /news - Persists news. News should be sent using request-body in appropriate JSON-format;
*	PUT: /news - Merge news. News should be sent using request-body in appropriate JSON-format;
*	DELETE: /news/id - Delete news by id.

