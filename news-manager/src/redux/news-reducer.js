import {authorAPI, newsAPI, tagsAPI} from "../api/api";
import {getUnique} from "./redux-util";
import React from "react";

const SET_NEWS = "SET_NEWS";
const SET_CURRENT_PAGE = "SET_CURRENT_PAGE";
const SET_TOTAL_NEWS_COUNT = "SET_TOTAL_NEWS_COUNT";
const SET_PAGE_SIZE = "SET_PAGE_SIZE";
const SET_TAGS = "SET_TAGS";
const SET_AUTHORS = "SET_AUTHORS";
const SET_SELECTED_TAGS = "SET_SELECTED_TAGS";
const SET_SELECTED_AUTHORS = "SET_SELECTED_AUTHORS";
const RESET_SEARCH = "RESET_SEARCH";
const SET_NEWS_PAGE = "SET_NEWS_PAGE";

let initialState = {
    news: [],
    pageSize: 2,
    totalNewsCount: 0,
    currentPage: 1,
    tags: [],
    authors: [],
    selectedTags: [],
    selectedAuthors: [],
    tagsNames: [],
    authorsNames: [],
    newsPage: null
};

const newsReducer = (state = initialState, action) => {

    switch (action.type) {
        case SET_NEWS: {
            return {...state, news: action.news}
        }
        case SET_PAGE_SIZE: {
            // requestNews(state.currentPage, action.pageSize, state.authorsNames, state.tagsNames);
            return {...state, pageSize: action.pageSize, currentPage: 1}
        }
        case SET_CURRENT_PAGE: {
            // requestNews(action.currentPage, state.pageSize, state.authorsNames, state.tagsNames);
            return {...state, currentPage: action.currentPage}
        }
        case SET_TOTAL_NEWS_COUNT: {
            return {...state, totalNewsCount: action.totalNewsCount}
        }
        case SET_TAGS: {
            return {...state, tags: action.tags}
        }
        case SET_AUTHORS: {
            return {...state, authors: action.authors}
        }
        case SET_SELECTED_TAGS: {
            return {...state, selectedTags: action.tags, tagsNames: action.tags.map(tag => tag.name)}
        }
        case SET_SELECTED_AUTHORS: {
            return {...state, selectedAuthors: action.authors, authorsNames: action.authors.map(author => author.name)}
        }
        case RESET_SEARCH: {
            return {...state, selectedAuthors: [], selectedTags: [], tagsNames: [], authorsNames: [], currentPage: 1};
        }
        case SET_NEWS_PAGE: {
            return {...state, newsPage: action.news};
        }
        default:
            return state;
    }
};

export const setNews = (news) => ({type: SET_NEWS, news});
export const setCurrentPage = (currentPage) => ({type: SET_CURRENT_PAGE, currentPage});
export const setPageSize = (pageSize) => ({type: SET_PAGE_SIZE, pageSize});
export const setTotalNewsCount = (totalNewsCount) => ({type: SET_TOTAL_NEWS_COUNT, totalNewsCount});
export const setNewsTags = (tags) => ({type: SET_TAGS, tags});
export const setNewsAuthors = (authors) => ({type: SET_AUTHORS, authors});
export const setSelectedTags = (tags) => ({type: SET_SELECTED_TAGS, tags});
export const setSelectedAuthors = (authors) => ({type: SET_SELECTED_AUTHORS, authors});
export const resetSearch = () => ({type: RESET_SEARCH});
export const setNewsPage = (news) => ({type: SET_NEWS_PAGE, news});

export const requestTags = () => {
    return (dispatch) => {
        tagsAPI.getTags()
            .then(data => {
                const tags = getUnique(data.data, "name");
                dispatch(setNewsTags(tags));
            })
    }
};

export const requestAuthors = () => {
    return (dispatch) => {
        authorAPI.getAuthors()
            .then(data => {
                const authors = getUnique(data, "name");
                dispatch(setNewsAuthors(authors));
            })
    }
};

export const updateNews = (news, history) => {
    debugger;
    return (dispatch) => {
        newsAPI.putNews(news)
            .then(response => {
                if (response.status === 200) {
                    history.push("/news/" + response.data.id);
                }
            });
    }
};

export const postNews = (news, history) => {
    return (dispatch) => {
        newsAPI.postNews(news)
            .then(response => {
                if (response.status === 201) {
                    history.push("/news/" + response.data.id);
                }
            });
    }
};

export const deleteNewsById = (id, history) => {
    return (dispatch) => {
        newsAPI.deleteNews(id)
            .then(response => {
                debugger;
                if (response.status === 200) {
                    dispatch(setNews([]));
                    history.replace("/news");
                }
            });
    }
};

export const requestNewsById = (newsId) => {
    return (dispatch) => {
        dispatch(setNewsPage(null));
        newsAPI.getNewsById(newsId)
            .then(data => {
                dispatch(setNewsPage(data));
            });
    }
};

export const requestNews = (page, pageSize, authorsName = null, tagsName = null) => {
    return (dispatch) => {
        newsAPI.getNews(page, pageSize, authorsName, tagsName)
            .then(data => {
                dispatch(setNews(data.news));
                dispatch(setTotalNewsCount(data.count))
            });
    }
};

export default newsReducer;