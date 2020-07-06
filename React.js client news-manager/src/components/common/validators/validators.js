export const required = value => {
    // debugger;
    if (value) return undefined;

    return "Field is required";
};

export const maxLengthCreator = (maxLength) => (value) => {
    // debugger;
    if (value && value.length > maxLength) return `Max length is ${maxLength} symbols`;
    return undefined;
};

export const maxLength30 = maxLengthCreator(30);

export const maxLength100 = maxLengthCreator(100);

export const maxLength2000 = maxLengthCreator(2000);