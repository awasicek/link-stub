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

const linkStubService = {
    createLinkStub,
};

const linkStubConstants = {
    endpoints,
};

export { linkStubService as default, linkStubConstants };
