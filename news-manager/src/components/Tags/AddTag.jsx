import React, {useState} from "react";
import style from "../Authors/Author.module.css";

const AddTag = (props) => {

    let [name, setName] = useState('');
    let [nameError, setNameError] = useState(" ");

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

    return (
        <div className={style.addAuthor}>
            <div className={style.fieldWrapper}>
                <div>
                    <h4 className={style.author}>Add tag:</h4>
                </div>
                <div>
                    <div>
                        <input type={"text"}
                               value={name}
                               onChange={onNameChanged}
                               placeholder={"Name"}
                               className={style.name}/>
                    </div>
                    <div className={style.error}>
                        {nameError}
                    </div>
                </div>
                <div>
                    <button onClick={() => {
                        props.onTagAdd({id: 0, name: name});
                        setName("");
                        setNameError(" ");
                    }}
                            disabled={nameError}>
                        Add
                    </button>
                </div>
            </div>
        </div>
    );
};

export default AddTag;