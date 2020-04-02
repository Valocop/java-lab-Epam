import React from "react";
import style from './Navbar.module.css'

const NavBar = (props) => {
    return (
        <div className={style.navbar}>
            <div className={style.title}>Dashboard</div>
            <div className={style.item}>Add/Edit Authors</div>
            <div className={style.item}>Add News</div>
            <div className={style.item}>Add/Edit Tags</div>
        </div>
    )
}

export default NavBar;