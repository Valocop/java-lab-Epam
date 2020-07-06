import React, {Component} from "react";
import {compose} from "redux";
import {connect} from "react-redux";
import {withRouter} from "react-router-dom";
import {createTag, getTags, putTag} from "../../redux/tags-reducer";
import style from "../Authors/Author.module.css";
import Tag from "./Tag";
import AddTag from "./AddTag";

class TagContainer extends Component {

    componentDidMount() {
        this.props.getTags();
    }

    render() {
        return (
            <div className={style.authorsContainer}>
                <div>
                    <h2>Add/Edit tags</h2>
                </div>
                {this.props.tags.map(tag => (
                    <div key={tag.id} className={style.authorWrapper}>
                        <Tag
                            tag={tag}
                            updateTag={this.props.putTag}
                        />
                    </div>
                ))}
                <AddTag
                    onTagAdd={this.props.createTag}
                />
            </div>
        );
    }
}

let mapStateToProps = (state) => {
    return {
        tags: state.tagsPage.tags
    }
};

export default compose(connect(mapStateToProps, {createTag, getTags, putTag})(withRouter(TagContainer)));