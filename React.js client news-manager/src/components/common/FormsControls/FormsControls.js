import React from "react";
import styles from "./FormsControls.module.css";

const Element = Element => ({input, meta, ...props}) => {
    const hasError = (meta.touched && meta.error) || (meta.active && meta.error);
    return (
        <div className={styles.formControl + " " + (hasError ? styles.error : "")}>
            <Element {...input} {...props} />
            {hasError && <div> {meta.error} </div>}
        </div>
    );
};

export const Textarea = Element("textarea");

export const Input = Element("input");