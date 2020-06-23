const endpoints = {
    CREATE_LINKSTUB: "api/linkstub",
};

const createLinkStub = async (linkStub) => {
    return await fetch(`http://localhost:8080/${endpoints.CREATE_LINKSTUB}`, {
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
