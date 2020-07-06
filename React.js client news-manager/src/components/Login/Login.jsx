import React, {useState} from "react";
import style from "./Login.module.css";
import {Field, reduxForm} from "redux-form";
import {maxLength30, required} from "../common/validators/validators";
import {Input} from "../common/FormsControls/FormsControls";
import {withRouter} from "react-router-dom";
import {compose} from "redux";
import {connect} from "react-redux";
import {login} from "../../redux/oAuth-reducer";

const Login = (props) => {

    let [username, setUsername] = useState("");
    let [password, setPassword] = useState("");

    const onLoginSubmit = (event) => {
        event.preventDefault();
        props.login(username, password);
    };

    return (
        <div className={style.loginContainer}>
            <form onSubmit={onLoginSubmit}>
                <div>Login</div>
                <div>
                    <div>Username:</div>
                    <Field placeholder={"Username"}
                           type={"text"}
                           name={"username"}
                           value={username}
                           onChange={(e) => setUsername(e.currentTarget.value)}
                           validate={[required, maxLength30]}
                           component={Input}/>
                </div>
                <div>
                    <div>Password:</div>
                    <Field placeholder={"Password"}
                           type={"password"}
                           name={"password"}
                           value={password}
                           onChange={(e) => setPassword(e.currentTarget.value)}
                           validate={[required, maxLength30]}
                           component={Input}/>
                </div>
                <div>
                    <button disabled={!props.valid}>Login</button>
                    <button type={"button"}>Cancel</button>
                </div>
            </form>
        </div>
    )
};

export default compose(connect(null, {login}), reduxForm({form: "LoginForm"}))(withRouter(Login));