import React, { useState } from "react";
import styled from "styled-components";
import _get from "lodash/get";
import { Container, Grid, Box, TextField, Link, Button } from "@material-ui/core";
import linkStubService from "@services/linkstub";

const ButtonWrapper = styled.div`
    display: inline-block;
    margin-top: 10px;
    padding: 0 10px;
`;

const LinkStubCreator = () => {
    const [url, setUrl] = useState("");
    const [urlHash, setUrlHash] = useState("");

    const clearStub = () => setUrlHash("");

    const handleChange = (e) => {
        if (urlHash.length > 0) clearStub();
        setUrl(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log(`submitted: ${url}`);
        try {
            const response = await linkStubService.createLinkStub({ originalUrl: url });
            if (response.status === 200 || response.status === 201) {
                const json = await response.json();
                setUrlHash(_get(json, "urlHash", ""));
            } else {
                const json = await response.json();
                // TODO -- handle error response in UI
                console.warn(`failure ${response.status}`, json);
            }
        } catch (err) {
            // TODO -- fetch only throws if API doesn't return a response etc (400/500 are still considered success)
            console.error(err);
        }
    };

    return (
        <Container>
            <Grid container direction="column" justify="center" alignItems="center">
                <Box m={3}>
                    <form onSubmit={handleSubmit}>
                        <TextField id="original-url-input" label="Enter Link" onChange={handleChange}></TextField>
                        <ButtonWrapper>
                            <Button type="submit" variant="contained" color="primary" size="medium">
                                Create
                            </Button>
                        </ButtonWrapper>
                    </form>
                </Box>
                {urlHash && (
                    <Box>
                        <span>Stub: </span>
                        <Link href={`http://localhost:8080/api/${urlHash}`} target="_blank" rel="noreferrer">
                            {`http://localhost:8080/api/${urlHash}`}
                        </Link>
                    </Box>
                )}
            </Grid>
        </Container>
    );
};

export default LinkStubCreator;
