import React from "react";
import {NavLink} from "react-router-dom";
import style from './News.module.css'

const News = (props) => {

    let newsStyle = props.fullMode ? style.fullWrapper : style.newsWrapper;

    return (
        props.news.map(news => (
            news && <div key={news.id} className={newsStyle}>
                <div className={style.title}>
                    <NavLink className={style.navLink} to={'/news/' + news.id}>
                        {news.title}
                    </NavLink>
                    {props.fullMode &&
                    <NavLink className={style.navLink} to={'/news'}>
                        <button>Close</button>
                    </NavLink>}
                </div>
                <div className={style.description}>
                    <div>{news.author.name}</div>
                    <div>{news.creationDate}</div>
                </div>
                <div>{news.shortText}</div>
                {props.fullMode && <div>{news.fullText}</div>}
                <div className={style.tagContainer}>
                    {news.tags.map(tag =>
                        <div key={tag.id}>{tag.name}</div>
                    )}
                </div>
                <div>
                    <NavLink to={'/editNews/' + news.id}>
                        <button>Edit</button>
                    </NavLink>
                    <button onClick={() => props.onDeleteNews(news.id)}>Delete</button>
                </div>
            </div>
        ))
    )
};

export default News;
