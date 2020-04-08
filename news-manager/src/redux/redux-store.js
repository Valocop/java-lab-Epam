import {applyMiddleware, combineReducers, createStore} from "redux";
import thunkMiddleware from "redux-thunk";
import newsReducer from "./news-reducer";
import {reducer as formReducer} from 'redux-form'
import authorsReducer from "./authors-reducer";
import tagsReducer from "./tags-reducer";

let reducers = combineReducers({
    newsPage: newsReducer,
    authorsPage: authorsReducer,
    tagsPage: tagsReducer,
    form: formReducer,
});

let store = createStore(reducers, applyMiddleware(thunkMiddleware));

window.store = store;

export default store;