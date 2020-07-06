import style from "./SearchBar.module.css";
import Select from "react-select";
import React from "react";

let Search = (props) => {
    return <div className={style.searchBar}>
        <div className={style.selectWrapper}>
            <Select
                isMulti
                name="colors"
                options={props.tags}
                className={style.select}
                classNamePrefix="select"
                placeholder="Select tags"
                value={props.selectedTags}
                onChange={props.setSelectedTags}
            />
            <button onClick={props.onSearch}>Search</button>
        </div>
        <div className={style.selectWrapper}>
            <Select
                isMulti
                name="colors"
                options={props.authors}
                className={style.select}
                classNamePrefix="select"
                placeholder="Select authors names"
                value={props.selectedAuthors}
                onChange={props.setSelectedAuthors}
            />
            <button onClick={props.resetSearch}>Reset</button>
        </div>
    </div>;
};

export default Search;