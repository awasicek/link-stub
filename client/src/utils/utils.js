export const isValidHttpUrl = (string) => {
    let isValid;
    let url;

    try {
        url = new URL(string);
        isValid = url.protocol === "http:" || url.protocol === "https:";
    } catch (err) {
        isValid = false;
    }

    return isValid;
};
