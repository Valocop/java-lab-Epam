import React, {useEffect, useState} from "react";
import style from "./Author.module.css";

const Author = (props) => {

    let [initialName, setInitialName] = useState(props.author.name);
    let [initialSurname, setInitialSurname] = useState(props.author.surname);
    let [name, setName] = useState(props.author.name);
    let [surname, setSurname] = useState(props.author.surname);
    let [isEdit, setEdit] = useState(false);
    let [nameError, setNameError] = useState(null);
    let [surnameError, setSurnameError] = useState(null);

    useEffect(() => {
        debugger;
        setInitialName(props.author.name);
        setInitialSurname(props.author.surname);
        setName(props.author.name);
        setSurname(props.author.surname);
    }, [props.author]);

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

    let discardChanges = () => {
        setName(initialName);
        setSurname(initialSurname);
        setEdit(false);
        setNameError(null);
        setSurnameError(null);
    };

    let saveChanges = () => {
        props.updateAuthor({id: props.author.id, name: name, surname: surname});
        setEdit(false);
    };

    return (
        <div className={style.fieldWrapper}>
            <div>
                <h4 className={style.author}>Author:</h4>
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
                <div>
                    <input
                        type={"text"}
                        value={surname}
                        disabled={!isEdit}
                        onChange={onSurnameChanged}
                        className={style.surname}/>
                </div>
                <div className={style.error}>
                    {surnameError}
                </div>
            </div>
            <div>
                {!isEdit && <button onClick={() => setEdit(true)}>Edit</button>}
                {isEdit && <button disabled={nameError || surnameError} onClick={saveChanges}
                >Save changes</button>}
                {isEdit && <button onClick={discardChanges}>Discard changes</button>}
            </div>
        </div>
    );
};

export default Author;