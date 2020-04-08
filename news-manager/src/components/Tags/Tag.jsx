import style from "../Authors/Author.module.css";
import React, {useEffect, useState} from "react";

const Tag = (props) => {

    let [initialName, setInitialName] = useState(props.tag.name);
    let [name, setName] = useState(props.tag.name);
    let [isEdit, setEdit] = useState(false);
    let [nameError, setNameError] = useState(null);

    useEffect(() => {
        debugger;
        setInitialName(props.tag.name);
        setName(props.tag.name);
    }, [props.tag]);

    let onNameChanged = (e) => {
        let value = e.target.value;
        setName(value);
        setNameError(checkForError(value))
    };

    let checkForError = (value) => {
        if (value.length === 0) {
            return "Field is empty";
        } else if (value.length > 30) {
            return "Field length is more 30";
        } else {
            return null;
        }
    };

    let discardChanges = () => {
        setName(initialName);
        setEdit(false);
        setNameError(null);
    };

    let saveChanges = () => {
        debugger;
        props.updateTag({id: props.tag.id, name: name});
        setEdit(false);
    };

    return (
        <div className={style.fieldWrapper}>
            <div>
                <h4 className={style.author}>Tag:</h4>
            </div>
            <div>
                <div>
                    <input
                        type={"text"}
                        value={name}
                        onChange={onNameChanged}
                        disabled={!isEdit}
                        className={style.name}/>
                </div>
                <div className={style.error}>
                    {nameError}
                </div>
            </div>
            <div>
                {!isEdit && <button onClick={() => setEdit(true)}>Edit</button>}
                {isEdit && <button disabled={nameError} onClick={saveChanges}
                >Save changes</button>}
                {isEdit && <button onClick={discardChanges}>Discard changes</button>}
            </div>
        </div>
    );
};

export default Tag;