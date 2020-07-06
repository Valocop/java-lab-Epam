import React, {Component} from "react";
import {compose} from "redux";
import {connect} from "react-redux";
import {requestNewsById, requestTags, updateNews} from "../../redux/news-reducer";
import {withRouter} from "react-router-dom";
import style from "../AddNews/AddNewsContainer.module.css";
import AddEditNewsForm from "../AddNews/AddEditNewsForm";
import Preloader from "../common/Preloader/Preloader";

class EditNewsContainer extends Component {

    componentDidMount() {
        this.props.requestTags();
        let newsId = this.props.match.params.newsId;
        this.props.requestNewsById(newsId);
    }

    onPutNews = (news) => {
        debugger;
        this.props.updateNews(news, this.props.history);
    };

    render() {
        if (!this.props.newsPage) {
            return <Preloader/>
        }

        return (
            <div className={style.contentWrapper}>
                <h2>Add/Edit news</h2>
                <AddEditNewsForm
                    tags={this.props.tags}
                    handleSubmit={this.onPutNews}
                    initialValues={this.props.newsPage}
                    news={this.props.newsPage}
                />
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        newsPage: state.newsPage.newsPage,
        tags: state.newsPage.tags
    }
};

export default compose(connect(mapStateToProps, {
    requestTags,
    requestNewsById,
    updateNews
}))(withRouter(EditNewsContainer));