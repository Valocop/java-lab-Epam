import style from "./Author.module.css";
import React, {useState} from "react";

const AddAuthor = (props) => {

    let [name, setName] = useState('');
    let [surname, setSurname] = useState('');
    let [nameError, setNameError] = useState(" ");
    let [surnameError, setSurnameError] = useState(" ");

    let onNameChanged = (e) => {
        let value = e.target.value;
        setName(value);
        setNameError(checkForError(value))
    };

    let onSurnameChanged = (e) => {
        let value = e.target.value;
        setSurname(value);
        setSurnameError(checkForError(value))
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
                    <h4 className={style.author}>Add author:</h4>
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
                    <div>
                        <input type={"text"}
                               value={surname}
                               onChange={onSurnameChanged}
                               placeholder={"Surname"}
                               className={style.surname}/>
                    </div>
                    <div className={style.error}>
                        {surnameError}
                    </div>
                </div>
                <div>
                    <button onClick={() => {
                        props.onAuthorAdd(name, surname);
                        setName("");
                        setSurname("");
                        setNameError(" ");
                        setSurnameError(" ");
                    }}
                            disabled={nameError || surnameError}>
                        Add
                    </button>
                </div>

            </div>
        </div>
    );
};

export default AddAuthor;