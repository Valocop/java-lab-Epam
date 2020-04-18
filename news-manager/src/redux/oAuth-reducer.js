import {oAuthAPI, userAPI} from "../api/api";
import {push} from 'react-router-redux';

const SET_USER_DATA = "SET_USER_DATA";

let initialState = {
    id: null,
    name: null,
    surname: null,
    username: null,
    roles: [],
    isAuth: false
};

const oAuthReducer = (state = initialState, action) => {
    switch (action.type) {
        case SET_USER_DATA: {
            return {...state, ...action.data};
        }
        default:
            return state;
    }
};

export const setAuthUserData = (id, name, surname, username, roles, isAuth) => ({
    type: SET_USER_DATA,
    data: {id, name, surname, username, roles, isAuth}
});

export const getAuthUserData = (username) => {
    return (dispatch) => {
        debugger;
        userAPI.getUserByUserName(username)
            .then(response => {
                if (response.status === 200) {
                    let {id, name, surname, username, roles} = response.data;
                    dispatch(setAuthUserData(id, name, surname, username, roles, true));
                }
            });
    };
};

export const getUserDataByToken = () => {
    return (dispatch) => {
        let username = userAPI.getUserDataByToken();
        if (username) {
            userAPI.getUserByUserName(username)
                .then(response => {
                    if (response.status === 200) {
                        let {id, name, surname, username, roles} = response.data;
                        dispatch(setAuthUserData(id, name, surname, username, roles, true));
                    }
                });
        }
    };
};

export const login = (username, password) => {
    return (dispatch) => {
        debugger;
        oAuthAPI.login(username, password)
            .then(response => {
                if (response.status === 200) {
                    debugger;
                    dispatch(getAuthUserData(username));
                    dispatch(push('/news'));
                } else {
                    dispatch(setAuthUserData(null, null, null, null, [], false));
                }
            });
    };
};

export const logout = () => {
    return (dispatch) => {
        oAuthAPI.logout();
        dispatch(setAuthUserData(null, null, null, null, [], false));
    };
};

export default oAuthReducer;