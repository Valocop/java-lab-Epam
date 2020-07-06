import {BrowserRouter, Switch} from "react-router-dom";
import {Provider} from "react-redux";
import App from "./App";
import React from "react";
import store from "./redux/redux-store";
import ReactDOM from 'react-dom';
import {ConnectedRouter} from 'connected-react-router';
import {history} from "./redux/redux-store";

ReactDOM.render(
    <Provider store={store}>
        <ConnectedRouter history={history}>
            {/*<BrowserRouter>*/}
            <Switch>
                <App/>
            </Switch>
            {/*</BrowserRouter>*/}
        </ConnectedRouter>
    </Provider>, document.getElementById('root')
);