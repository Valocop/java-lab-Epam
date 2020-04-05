import {applyMiddleware, combineReducers, createStore} from "redux";
import thunkMiddleware from "redux-thunk";
import newsReducer from "./news-reducer";
import {reducer as formReducer} from 'redux-form'
import authorsReducer from "./authors-reducer";

let reducers = combineReducers({
    newsPage: newsReducer,
    authorsPage: authorsReducer,
    form: formReducer,
});

let store = createStore(reducers, applyMiddleware(thunkMiddleware));

window.store = store;

export default store;