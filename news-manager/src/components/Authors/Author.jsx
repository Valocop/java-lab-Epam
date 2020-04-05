import React from "react";
import style from './Author.module.css';
import {Field, FieldArray, reduxForm} from "redux-form";
import {maxLength30, required} from "../common/validators/validators";
import {Input} from "../common/FormsControls/FormsControls";

const RenderAuthors = ({fields, meta: {error, submitFailed}, authors}) => {

    let addAuthors = () => {
        authors.forEach(author => fields.push(author));
    };



    return (
        <div>
            <button type="button" onClick={() => addAuthors()}>
                Add Member
            </button>
            {fields.map((member, index) => (
                <div key={index}>
                    <Field
                        // placeholder={member.name}
                        type={"text"}
                        name={`${member}.name`}
                        // className={style.name}
                        // onChange={(e) => setTitle(e.currentTarget.value)}
                        validate={[required, maxLength30]}
                        component={Input}/>
                    <Field
                        // placeholder={member.name}
                        type={"text"}
                        name={`${member}.surname`}
                        // className={style.name}
                        // onChange={(e) => setTitle(e.currentTarget.value)}
                        validate={[required, maxLength30]}
                        component={Input}/>
                </div>
            ))}
        </div>
    );
};

const Author = (props) => {
    return (
        <div>
            <FieldArray name={"authors"} component={RenderAuthors} authors={props.authors}/>
        </div>
    );
};

export default reduxForm({form: "AddEditAuthor"})(Author);