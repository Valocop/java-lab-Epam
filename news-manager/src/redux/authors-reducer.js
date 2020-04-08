import {authorAPI} from "../api/api";

const SET_AUTHORS = "SET_AUTHORS";
const UPDATE_AUTHOR = "UPDATE_AUTHOR";
const ADD_AUTHOR = "ADD_AUTHOR";

let initialState = {
    authors: []
};

const authorsReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_AUTHORS: {
            return {...state, authors: action.authors};
        }
        case UPDATE_AUTHOR: {
            return {
                ...state, authors: state.authors.map(author => {
                    if (author.id === action.author.id) {
                        return action.author;
                    }
                    return author;
                })
            };
        }
        case ADD_AUTHOR: {
            debugger;
            return {...state, authors: [...state.authors, action.author]}
        }
        default:
            return state;
    }
};

export const setAuthors = (authors) => ({type: SET_AUTHORS, authors});
export const updateAuthor = (author) => ({type: UPDATE_AUTHOR, author});
export const addNewAuthor = (author) => ({type: ADD_AUTHOR, author});

export const getAuthors = () => {
    return (dispatch) => {
        authorAPI.getAuthors()
            .then(data => {
                dispatch(setAuthors(data));
            })
    }
};

export const createAuthor = (author) => {
    return (dispatch) => {
        authorAPI.postAuthor(author)
            .then(response => {
                debugger;
                if (response.status === 201) {
                    dispatch(addNewAuthor(response.data));
                }
            })
    }
};

export const putAuthor = (author) => {
    return (dispatch) => {
        authorAPI.putAuthor(author)
            .then(data => {
                if (data.status === 200) {
                    debugger;
                    dispatch(updateAuthor(data.data));
                }
            })
    }
};

export default authorsReducer;