import React from "react";
import styled from "styled-components";
import LinkStubCreator from "@components/linkstub-creator";

const StyledHomeContainer = styled.div`
    height: 100vh;
    width: 100vw;
    overflow: scroll;
    background-color: lightgoldenrodyellow;
    font-family: Tahoma, Geneva, sans-serif;
`;

const Home = () => (
    <StyledHomeContainer data-testid="home-container">
        <h1>Link Stub Application</h1>
        <LinkStubCreator />
    </StyledHomeContainer>
);

export default Home;
