import React from "react";
import Home from "../Home";
import { render, screen } from "@testing-library/react";
import "@testing-library/jest-dom";

describe("Home component", function () {
    it("should render", () => {
        render(<Home/>);
        expect(screen.getByTestId("home-container")).toBeInTheDocument();
    });
});