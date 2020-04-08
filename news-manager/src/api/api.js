import axios from "axios";

const instance = axios.create({
    // withCredentials: true,
    baseURL: 'http://localhost:8080/news-manager/'
});

export const newsAPI = {
    getNews(currentPage = 1, pageSize = 10, authorsName = null, tagsName = null) {
        let authorSearch = Array.isArray(authorsName) && authorsName.length ? ('&authors_name=' + authorsName) : '';
        let tagSearch = Array.isArray(tagsName) && tagsName.length ? ('&tags_name=' + tagsName) : '';
        return instance.get(`news?count=${pageSize}&page=${currentPage}` + authorSearch + tagSearch)
            .then(response => {
                return response.data;
            });
    },
    getNewsById(id) {
        return instance.get('news/' + id)
            .then(response => {
                return response.data;
            });
    },
    postNews(news) {
        return instance.post('news', news);
    },
    putNews(news) {
        return instance.put('news', news);
    },
    deleteNews(id) {
        return instance.delete('news/' + id);
    }
};

export const tagsAPI = {
    getTags() {
        return instance.get('tags');
    },
    postTag(tag) {
        return instance.post('tags', tag);
    },
    put(tag) {
        return instance.put('tags', tag);
    }
};

export const authorAPI = {
    getAuthors() {
        return instance.get('authors')
            .then(response => {
                return response.data;
            });
    },
    putAuthor(author) {
        return instance.put('authors', author);
    },
    postAuthor(author) {
        return instance.post('authors', author);
    }
};