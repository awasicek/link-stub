import linkStubService, { linkStubConstants } from "../linkstub-service";
import { enableFetchMocks } from "jest-fetch-mock";

enableFetchMocks();

describe("linkStubService", function () {
    const { endpoints } = linkStubConstants;

    beforeEach(() => {
        fetch.resetMocks();
    });

    describe("createLinkStub", () => {
        it("should call the server to create a link stub", async () => {
            const requestedLinkStub = {
                originalUrl: "http://test.com",
            };
            const mockSuccessResponse = {
                originalUrl: "http://test.com",
                urlHash: "58f3ae21",
                createdOn: "2020-06-21T05:44:27.475195Z",
            };
            fetch.mockResponseOnce(JSON.stringify(mockSuccessResponse));

            const result = await linkStubService.createLinkStub(requestedLinkStub);
            const resultBody = await result.json();

            expect(fetch.mock.calls).toHaveLength(1);
            const [urlArg, optionsArg] = fetch.mock.calls[0];
            expect(urlArg).toEqual(`${process.env.HOST}/${endpoints.CREATE_LINKSTUB}`);
            expect(optionsArg.method).toEqual("POST");
            expect(JSON.parse(optionsArg.body)).toEqual(requestedLinkStub);
            expect(resultBody).toEqual(mockSuccessResponse);
        });
    });
});
