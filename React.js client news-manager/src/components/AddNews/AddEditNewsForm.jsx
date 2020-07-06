import {Field, reduxForm} from "redux-form";
import {maxLength100, maxLength2000, maxLength30, required} from "../common/validators/validators";
import {Input, Textarea} from "../common/FormsControls/FormsControls";
import React, {useEffect, useState} from "react";
import style from './AddEditNewsForm.module.css'
import {NavLink} from "react-router-dom";

let AddEditNewsPostForm = (props) => {
    let [selectedTags, setSelectedTags] = useState(props.news ? props.news.tags : []);
    let [tag, setTag] = useState('');
    let [tittle, setTitle] = useState(props.news ? props.news.title : '');
    let [shortText, setShortText] = useState(props.news ? props.news.shortText : '');
    let [fullText, setFullText] = useState(props.news ? props.news.fullText : '');

    useEffect(() => {
        setSelectedTags(props.news ? props.news.tags : []);
        setTitle(props.news ? props.news.title : '');
        setShortText(props.news ? props.news.shortText : '');
        setFullText(props.news ? props.news.fullText : '');
    }, [props.news]);

    let onTagSelected = () => {
        if (!!tag.trim() && tag.length <= 30) {
            let findTag = props.tags.find(value => value.name === tag);

            if (findTag) {
                setSelectedTags([...selectedTags, {id: findTag.id, name: findTag.name}]);
            } else {
                setSelectedTags([...selectedTags, {id: 0, name: tag}])
            }
            setTag('');
        }
    };

    let onTagDeleted = (tag) => {
        setSelectedTags([...selectedTags.filter(value => value.name !== tag.name)]);
    };

    let onSubmitForm = (event) => {
        event.preventDefault();
        let news = {
            id: props.news ? props.news.id : 0,
            title: tittle,
            shortText: shortText,
            fullText: fullText,
            author: {
                id: 25,
                name: "Author HardCode",
                surname: "Author HardCode"
            },
            tags: selectedTags
        };
        props.handleSubmit(news);
    };

    return (
        <form className={style.form} onSubmit={onSubmitForm}>
            <div>
                <div>News title:</div>
                <Field placeholder={"News title"}
                       type={"text"}
                       name={"title"}
                       onChange={(e) => setTitle(e.currentTarget.value)}
                       validate={[required, maxLength30]}
                       component={Input}/>
            </div>
            <div>
                <div>News short text:</div>
                <Field placeholder={"News short text"}
                       name={"shortText"}
                       onChange={(e) => setShortText(e.currentTarget.value)}
                       validate={[required, maxLength100]}
                       component={Textarea}/>
            </div>
            <div>
                <div>News full text:</div>
                <Field placeholder={"News full text"}
                       name={"fullText"}
                       onChange={(e) => setFullText(e.currentTarget.value)}
                       validate={[required, maxLength2000]}
                       component={Textarea}/>
            </div>
            <div>
                <div className={style.tagsWrapper}>
                    <div>
                        <Field placeholder={"SelectTag"}
                               type={"text"}
                               value={tag}
                               name={"selectTags"}
                               list="suggestedTag"
                               component={Input}
                               onChange={(e) => setTag(e.currentTarget.value)}
                               validate={[maxLength30]}
                        />
                        <datalist id="suggestedTag">
                            {props.tags.map((tag) =>
                                <option key={tag.id} id={tag.id} value={tag.name}/>
                            )}
                        </datalist>
                    </div>
                    <div>
                        <button type={"button"} onClick={onTagSelected}>Add Tag</button>
                    </div>
                </div>
                <div className={style.tagsListWrapper}>
                    {selectedTags.map((value, index) =>
                        <div className={style.tag}
                             key={index}>{value.name}
                            <button className={style.tagButton}
                                    onClick={() => onTagDeleted(value)}
                                    type={"button"}>
                                x
                            </button>
                        </div>)}
                </div>
            </div>
            <div>
                <button disabled={!props.valid} type={"submit"}
                >Add
                </button>
                <NavLink to={"/news"}>
                    <button type={"button"}>Cancel</button>
                </NavLink>
            </div>
        </form>
    )
};

export default reduxForm({form: "AddEditPostForm"})(AddEditNewsPostForm);