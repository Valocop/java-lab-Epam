import React, {Component} from "react";
import style from './Author.module.css';
import {compose} from "redux";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {getAuthors} from "../../redux/authors-reducer";
import Author from "./Author";

class AuthorContainer extends Component {

    componentDidMount() {
        this.props.getAuthors();
    }

    render() {
        return (
            <div className={style.authorsContainer}>
                <div>
                    <h2>Add/Edit tags</h2>
                </div>
                {/*{this.props.authors.map(author => (*/}
                {/*    <div key={author.id} className={style.authorWrapper}>*/}
                {/*        <Author*/}
                {/*            author={author}*/}
                {/*        />*/}
                {/*    </div>*/}
                {/*))}*/}

                <Author authors={this.props.authors}/>

                <div className={style.addAuthor}>
                    <h4 className={style.author}>Add author:</h4>
                    <input type={"text"} placeholder={"Name"} className={style.name}/>
                    <input type={"text"} placeholder={"Surname"} className={style.surname}/>
                    <button>Add</button>
                </div>
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        authors: state.authorsPage.authors
    }
};

export default compose(connect(mapStateToProps, {getAuthors})(withRouter(AuthorContainer)));