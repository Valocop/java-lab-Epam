import {authorAPI, tagsAPI} from "../api/api";

const SET_TAGS = "SET_TAGS";
const UPDATE_TAG = "UPDATE_TAG";
const ADD_TAG = "ADD_TAG";

let initialState = {
    tags: []
};

const tagsReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_TAGS: {
            debugger;
            return {...state, tags: action.tags};
        }
        case UPDATE_TAG: {
            return {
                ...state, tags: state.tags.map(tag => {
                    if (tag.id === action.tag.id) {
                        return action.tag;
                    }
                    return tag;
                })
            };
        }
        case ADD_TAG: {
            debugger;
            return {...state, tags: [...state.tags, action.tag]}
        }
        default:
            return state;
    }
};

export const setTags = (tags) => ({type: SET_TAGS, tags});
export const updateTag = (tag) => ({type: UPDATE_TAG, tag});
export const addNewTag = (tag) => ({type: ADD_TAG, tag});

export const getTags = () => {
    return (dispatch) => {
        tagsAPI.getTags()
            .then(response => {
                dispatch(setTags(response.data));
            })
    }
};

export const createTag = (tag) => {
    return (dispatch) => {
        tagsAPI.postTag(tag)
            .then(response => {
                debugger;
                if (response.status === 201) {
                    dispatch(addNewTag(response.data));
                }
            })
    }
};

export const putTag = (tag) => {
    return (dispatch) => {
        tagsAPI.put(tag)
            .then(data => {
                if (data.status === 200) {
                    debugger;
                    dispatch(updateTag(data.data));
                }
            })
    }
};

export default tagsReducer;