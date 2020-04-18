import {applyMiddleware, combineReducers, createStore} from "redux";
import thunkMiddleware from "redux-thunk";
import newsReducer from "./news-reducer";
import {reducer as formReducer} from 'redux-form'
import authorsReducer from "./authors-reducer";
import tagsReducer from "./tags-reducer";
import oAuthReducer from "./oAuth-reducer";
import appReducer from "./app-reducer";
import {connectRouter} from 'connected-react-router'
import {createBrowserHistory} from 'history'
import {routerMiddleware} from 'connected-react-router'

export const history = createBrowserHistory();

let reducers = combineReducers({
    newsPage: newsReducer,
    authorsPage: authorsReducer,
    tagsPage: tagsReducer,
    oAuth: oAuthReducer,
    app: appReducer,
    form: formReducer,
    router: connectRouter(history)
});

let store = createStore(reducers, applyMiddleware(thunkMiddleware, routerMiddleware(history)));

window.store = store;

export default store;