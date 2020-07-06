import axios from "axios";
import {tokenProvider} from "./oAuth";
import * as Querystring from "query-string";

const jwtDecode = require('jwt-decode');

// const instance = axios.create({
//     baseURL: 'http://localhost:8080/news-manager/api',
//     headers: tokenProvider.getToken() ? {Authorization: `Bearer ${tokenProvider.getToken()}`} : null
// });

let getRequestInstance = () => {
    let token = tokenProvider.getToken();
    return axios.create({
        baseURL: 'http://localhost:8080/news-manager/api',
        headers: token ? {Authorization: `Bearer ${token}`} : null
    });
};

export const userAPI = {
    getUserByUserName(username) {
        debugger;
        return getRequestInstance().get('users?username=' + username);
    },
    getUserDataByToken() {
        let token = tokenProvider.getToken();
        let decode = token ? jwtDecode(token) : null;
        return decode ? decode.user_name : null;
    }
};

export const oAuthAPI = {
    login(username, password) {
        return axios.request({
            url: "/oauth/token",
            method: "POST",
            baseURL: "http://localhost:8080/news-manager",
            headers: {
                "Content-Type": "application/x-www-form-urlencoded"
            },
            auth: {
                username: 'clientCredentials',
                password: 'credentials'
            },
            data: Querystring.stringify({
                grant_type: "password",
                username: username,
                password: password
            })
        })
            .then(response => {
                if (response.status === 200) {
                    tokenProvider.setToken(response.data);
                }
                return response;
            });
    },
    logout() {
        tokenProvider.setToken(null);
    },
    isLogin() {
        return !!tokenProvider.getToken();
    }
};

export const newsAPI = {
    getNews(currentPage = 1, pageSize = 10, authorsName = null, tagsName = null) {
        let authorSearch = Array.isArray(authorsName) && authorsName.length ? ('&authors_name=' + authorsName) : '';
        let tagSearch = Array.isArray(tagsName) && tagsName.length ? ('&tags_name=' + tagsName) : '';
        return getRequestInstance().get(`news?count=${pageSize}&page=${currentPage}` + authorSearch + tagSearch)
            .then(response => {
                return response.data;
            });
    },
    getNewsById(id) {
        return getRequestInstance().get('news/' + id)
            .then(response => {
                return response.data;
            });
    },
    postNews(news) {
        return getRequestInstance().post('news', news);
    },
    putNews(news) {
        return getRequestInstance().put('news', news);
    },
    deleteNews(id) {
        return getRequestInstance().delete('news/' + id);
    }
};

export const tagsAPI = {
    getTags() {
        return getRequestInstance().get('tags');
    },
    postTag(tag) {
        return getRequestInstance().post('tags', tag);
    },
    put(tag) {
        return getRequestInstance().put('tags', tag);
    }
};

export const authorAPI = {
    getAuthors() {
        return getRequestInstance().get('authors')
            .then(response => {
                return response.data;
            });
    },
    putAuthor(author) {
        return getRequestInstance().put('authors', author);
    },
    postAuthor(author) {
        return getRequestInstance().post('authors', author);
    }
};