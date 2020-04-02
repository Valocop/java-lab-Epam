import React from "react";
import style from './NewsContainer.module.css'
import SearchContainer from "../SearchBar/SearchContainer";
import Pagination from "../Paggination/Pagination";
import News from "./News";
import {compose} from "redux";
import {connect} from "react-redux";
import {
    requestAuthors,
    requestNews,
    requestTags, resetSearch,
    setCurrentPage,
    setPageSize, setSelectedAuthors, setSelectedTags,
    setTotalNewsCount
} from "../../redux/news-reducer";

class NewsContainer extends React.Component {

    newsRequest = () => {
        this.props.requestNews(this.props.currentPage, this.props.pageSize, this.props.authorsNames, this.props.tagsNames);
    };

    componentDidMount() {
        this.newsRequest();
        this.props.requestTags();
        this.props.requestAuthors();
    };

    componentDidUpdate(prevProps, prevState, snapshot) {
        if (this.props.currentPage !== prevProps.currentPage ||
            this.props.pageSize !== prevProps.pageSize) {
            this.newsRequest();
        }
    };

    resetNewsSearch = () => {
        this.props.resetSearch();
        this.props.requestNews(1, this.props.pageSize, null, null);
    };

    render() {
        return (
            <div className={style.newsBar}>
                <SearchContainer
                    {...this.props}
                    resetSearch={this.resetNewsSearch}
                />
                <div className={style.newsList}>
                    <News news={this.props.news}
                          fullMode={false}/>
                </div>
                <Pagination
                    pageSize={this.props.pageSize}
                    currentPage={this.props.currentPage}
                    totalNewsCount={this.props.totalNewsCount}
                    onPageSizeChanged={this.props.setPageSize}
                    onCurrentPageChanged={this.props.setCurrentPage}
                />
            </div>
        )
    }
}

let mapStateToProps = (state) => {
    return {
        news: state.newsPage.news,
        pageSize: state.newsPage.pageSize,
        currentPage: state.newsPage.currentPage,
        totalNewsCount: state.newsPage.totalNewsCount,
        tags: state.newsPage.tags,
        authors: state.newsPage.authors,
        selectedTags: state.newsPage.selectedTags,
        selectedAuthors: state.newsPage.selectedAuthors,
        tagsNames: state.newsPage.tagsNames,
        authorsNames: state.newsPage.authorsNames
    }
};

export default compose(
    connect(mapStateToProps, {
        setCurrentPage,
        setPageSize,
        requestNews,
        setTotalNewsCount,
        requestTags,
        requestAuthors,
        setSelectedTags,
        setSelectedAuthors,
        resetSearch
    })(NewsContainer)
);