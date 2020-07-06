
const setToken = (token) => {
    debugger;
    if (token) {
        localStorage.setItem('REACT_TOKEN_AUTH', JSON.stringify(token));
    } else {
        localStorage.removeItem('REACT_TOKEN_AUTH');
    }
};

const getToken = () => {
    const token = JSON.parse(localStorage.getItem('REACT_TOKEN_AUTH'));

    return token ? token.access_token : null;
};

export const tokenProvider = {
    getToken() {
        return getToken();
    },
    setToken(token) {
        setToken(token);
    },
};