import React from "react";
import style from './Navbar.module.css'
import {NavLink} from "react-router-dom";

const NavBar = (props) => {
    return (
        <div className={style.navbar}>
            <div className={style.title}>Dashboard</div>
            <NavLink to={'/authors'}>
                <div className={style.item}>Add/Edit Authors</div>
            </NavLink>
            <NavLink to={'/addNews'}>
                <div className={style.item}>Add News</div>
            </NavLink>
            <NavLink to={'/tags'}>
                <div className={style.item}>Add/Edit Tags</div>
            </NavLink>
        </div>
    )
}

export default NavBar;