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
    }
};

export const tagsAPI = {
    getTags() {
        return instance.get('tags')
            .then(response => {
                return response.data;
            });
    }
};

export const authorAPI = {
    getAuthors() {
        return instance.get('authors')
            .then(response => {
                return response.data;
            });
    }
};