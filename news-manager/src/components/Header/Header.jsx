import React from "react";
import style from './Header.module.css'
import {compose} from "redux";
import {connect} from "react-redux";
import {NavLink, withRouter} from "react-router-dom";
import {logout} from "../../redux/oAuth-reducer";

const Header = (props) => {
    return (
        <header className={style.header}>
            <div className={style.container}>
                <div>News Portal</div>
                <div className={style.langContainer}>
                    <div>EN</div>
                    <div>RU</div>
                    <div>FR</div>
                </div>
                {!props.isAuth &&
                <div className={style.choiceContainer}>
                    <NavLink to={'/login'}>
                        <span>Login</span>
                    </NavLink>
                    <span>Register</span>
                </div>}
                {props.isAuth &&
                <div className={style.choiceContainer}>
                    <span>Hello {props.name}</span>
                    <span onClick={props.logout}>Logout</span>
                </div>}
            </div>
        </header>
    )
};

let mapStateToProps = (state) => {
    return {
        name: state.oAuth.name,
        isAuth: state.oAuth.isAuth
    }
};

export default compose(connect(mapStateToProps, {logout})(withRouter(Header)));