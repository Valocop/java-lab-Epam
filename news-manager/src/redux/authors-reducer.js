import {authorAPI} from "../api/api";

const SET_AUTHORS = "SET_AUTHORS";

let initialState = {
    authors: []
};

const authorsReducer = (state = initialState, action) => {

    switch (action.type) {
        case SET_AUTHORS: {
            return {...state, authors: action.authors};
        }
        default:
            return state;
    }
};

export const setAuthors = (authors) => ({type: SET_AUTHORS, authors});

export const getAuthors = () => {
    return (dispatch) => {
        authorAPI.getAuthors()
            .then(data => {
                dispatch(setAuthors(data));
            })
    }
};

export default authorsReducer;