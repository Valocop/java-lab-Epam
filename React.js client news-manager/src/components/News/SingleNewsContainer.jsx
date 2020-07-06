import React, {Component} from "react";
import style from './NewsContainer.module.css'
import {compose} from "redux";
import {connect} from "react-redux";
import {deleteNewsById, requestNewsById} from "../../redux/news-reducer";
import {withRouter} from "react-router-dom";
import News from "./News";

class SingleNewsContainer extends Component {

    componentDidMount() {
        let newsId = this.props.match.params.newsId;
        this.props.requestNewsById(newsId);
    }

    onDeleteNews = (newsId) => {
        this.props.deleteNewsById(newsId, this.props.history);
    };

    render() {
        return (
            <div className={style.newsBar}>
                <News
                    news={[this.props.newsPage]}
                    fullMode={true}
                    onDeleteNews={this.onDeleteNews}
                />
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        newsPage: state.newsPage.newsPage
    }
};

export default compose(connect(mapStateToProps, {requestNewsById, deleteNewsById}), withRouter)(SingleNewsContainer);