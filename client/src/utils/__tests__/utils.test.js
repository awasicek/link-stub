import { isValidHttpUrl } from "../utils";

describe("utility isValidHttpUrl", function () {
    it("should return true for a valid http URL", () => {
        expect(isValidHttpUrl("http://test.com")).toBe(true);
    });

    it("should return true for a valid https URL", () => {
        expect(isValidHttpUrl("https://test.com")).toBe(true);
    });

    it("should return false for a www website without http/https", () => {
        expect(isValidHttpUrl("www.test.com")).toBe(false);
    });

    it("should return false for a domain-name only site without http/https", () => {
        expect(isValidHttpUrl("test.com")).toBe(false);
    });

    it("should return false for an otherwise valid non-http URL", () => {
        expect(isValidHttpUrl("javascript:void(0)")).toBe(false);
    });
});
