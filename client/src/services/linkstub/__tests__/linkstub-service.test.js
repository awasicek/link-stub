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

    describe("processUrlForDefects", () => {
        it("should fix a www address missing the http/https protocol", () => {
            const MALFORMED_TEST_URL = "www.automation.com";
            const EXPECTED_FIXED_URL = "http://www.automation.com";

            const resultantUrl = linkStubService.processUrlForDefects(MALFORMED_TEST_URL);
            expect(resultantUrl).toEqual(EXPECTED_FIXED_URL);
        });

        it("should fix a 'plain' address missing the http/https protocol", () => {
            const MALFORMED_TEST_URL = "automation.com";
            const EXPECTED_FIXED_URL = "http://automation.com";

            const resultantUrl = linkStubService.processUrlForDefects(MALFORMED_TEST_URL);
            expect(resultantUrl).toEqual(EXPECTED_FIXED_URL);
        });

        it("should leave untouched a valid http address", () => {
            const VALID_TEST_URL = "http://test.edu";

            const resultantUrl = linkStubService.processUrlForDefects(VALID_TEST_URL);
            expect(resultantUrl).toEqual(VALID_TEST_URL);
        });

        it("should leave untouched a valid https address", () => {
            const VALID_TEST_URL = "https://test.edu";

            const resultantUrl = linkStubService.processUrlForDefects(VALID_TEST_URL);
            expect(resultantUrl).toEqual(VALID_TEST_URL);
        });
    });
});
