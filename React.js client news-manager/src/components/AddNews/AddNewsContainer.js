import React, {Component} from "react";
import style from './AddNewsContainer.module.css';
import AddEditNewsForm from "./AddEditNewsForm";
import {compose} from "redux";
import {connect} from "react-redux";
import {postNews, requestTags} from "../../redux/news-reducer";
import {withRouter} from "react-router-dom";
import Preloader from "../common/Preloader/Preloader";

class AddNewsContainer extends Component {

    componentDidMount() {
        this.props.requestTags();
    }

    onPostNews = (news) => {
        this.props.postNews(news, this.props.history);
    };

    render() {
        if (!this.props.tags) {
            return <Preloader/>
        }

        return (
            <div className={style.contentWrapper}>
                <h2>Add/Edit news</h2>
                <AddEditNewsForm
                    tags={this.props.tags}
                    handleSubmit={this.onPostNews}
                />
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        tags: state.newsPage.tags,
    }
};

export default compose(connect(mapStateToProps, {requestTags, postNews})(withRouter(AddNewsContainer)));