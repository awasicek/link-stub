const endpoints = {
    CREATE_LINKSTUB: "linkstub",
};

const createLinkStub = async (linkStub) => {
    return await fetch(`${process.env.HOST}/${endpoints.CREATE_LINKSTUB}`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json;charset=utf-8",
        },
        body: JSON.stringify(linkStub),
    });
};

/**
 * Attempts to fix a malformed URL if it detects no proper protocol. Otherwise
 * returns the url as provided.
 * @param {string} url
 * @returns {string}
 */
const processUrlForDefects = (url) => {
    const protocolRegex = /^(https:\/\/)|(http:\/\/)/;
    if (!protocolRegex.test(url)) {
        return "http://" + url;
    } else {
        return url;
    }
};

const linkStubService = {
    createLinkStub,
    processUrlForDefects,
};

const linkStubConstants = {
    endpoints,
};

export { linkStubService as default, linkStubConstants };
