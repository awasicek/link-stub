import React from "react";
import styled from "styled-components";
import LinkStubCreator from "@components/linkstub-creator";
import NinjaSvg from "@assets/ninja.svg";

const StyledHomeContainer = styled.div`
    height: 100vh;
    width: 100vw;
    overflow: scroll;
    background: linear-gradient(120deg, #ffce73 50%, #412f4e 50%);
    font-family: Tahoma, Geneva, sans-serif;
    padding-top: 100px;
`;

const HeaderCreatorContainer = styled.div`
    margin: auto;
    width: fit-content;
    padding: 20px 40px;
    clip-path: polygon(7% 0, 90% 0, 95% 100%, 2% 100%);
    background-color: #efefef;
    min-height: 300px;
`;

const AppHeaderWrapper = styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
`;

const NinjaIcon = styled.div`
    background: url(${NinjaSvg});
    background-repeat: no-repeat;
    width: 100px;
    height: 100px;
    filter: opacity(0.85);
`;

const AppTitle = styled.h1`
    font-size: 2.25rem;
    font-family: "Roboto", "Helvetica", "Arial", sans-serif;
    font-weight: 500;
    font-variant: small-caps;
    letter-spacing: 0.7rem;
    width: min-content;
`;

const AppTitleWrapper = styled.div`
    margin-left: 20px;
`;

const Home = () => (
    <StyledHomeContainer data-testid="home-container">
        <HeaderCreatorContainer>
            <AppHeaderWrapper>
                <NinjaIcon />
                <AppTitleWrapper>
                    <AppTitle>Link Stub</AppTitle>
                </AppTitleWrapper>
            </AppHeaderWrapper>
            <LinkStubCreator />
        </HeaderCreatorContainer>
    </StyledHomeContainer>
);

export default Home;
