import React, {Component} from "react";
import style from './Author.module.css';
import {compose} from "redux";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {createAuthor, getAuthors, putAuthor} from "../../redux/authors-reducer";
import Author from "./Author";
import AddAuthor from "./AddAuthor";

class AuthorContainer extends Component {

    componentDidMount() {
        this.props.getAuthors();
    }

    onAddAuthor = (name, surname) => {
        this.props.createAuthor({id: 0, name: name, surname: surname});
    };

    render() {
        return (
            <div className={style.authorsContainer}>
                <div>
                    <h2>Add/Edit Authors</h2>
                </div>
                {this.props.authors.map(author => (
                    <div key={author.id} className={style.authorWrapper}>
                        <Author
                            author={author}
                            updateAuthor={this.props.putAuthor}
                        />
                    </div>
                ))}
                <AddAuthor
                    onAuthorAdd={this.onAddAuthor}
                />
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        authors: state.authorsPage.authors
    }
};

export default compose(connect(mapStateToProps, {getAuthors, putAuthor, createAuthor})(withRouter(AuthorContainer)));