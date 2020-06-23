import React, { useState } from "react";
import styled from "styled-components";
import _get from "lodash/get";
import { Container, Grid, Box, TextField, Link, Button } from "@material-ui/core";
import AddIcon from "@material-ui/icons/Add";
import { Alert } from "@material-ui/lab";
import linkStubService from "@services/linkstub";
import { isValidHttpUrl } from "@utils";

const ButtonWrapper = styled.div`
    display: inline-block;
    margin-top: 10px;
    padding: 0 10px;
`;

const StyledTextField = styled(TextField)`
    min-width: 250px;
`;

const LinkStubCreator = () => {
    const [url, setUrl] = useState("");
    const [urlHash, setUrlHash] = useState("");
    const [isUrlValid, setIsUrlValid] = useState(null);
    const [isError, setIsError] = useState(false);

    const clearStub = () => setUrlHash("");

    const handleChange = (e) => {
        // clear stub if user changes the input url
        if (urlHash.length > 0) clearStub();
        setUrl(e.target.value);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        console.log(`submitted: ${url}`);

        // validate on frontend to stop from making unnecessary bad requests
        if (isValidHttpUrl(url)) {
            setIsUrlValid(true);
            try {
                const response = await linkStubService.createLinkStub({ originalUrl: url });
                if (response.status === 200 || response.status === 201) {
                    setIsError(false);
                    const json = await response.json();
                    setUrlHash(_get(json, "urlHash", ""));
                } else {
                    setIsError(true);
                    const json = await response.json();
                    // TODO -- handle error response in UI
                    console.warn(`failure ${response.status}`, json);
                }
            } catch (err) {
                setIsError(true);
                // TODO -- fetch only throws if API doesn't return a response etc (400/500 are still considered success)
                console.error(err);
            }
        } else {
            setIsUrlValid(false);
        }
    };

    return (
        <Container>
            <Grid container direction="column" justify="center" alignItems="center">
                <Box m={3}>
                    <form onSubmit={handleSubmit}>
                        <StyledTextField
                            id="original-url-input"
                            label="Enter Link"
                            onChange={handleChange}
                            error={isUrlValid === false}
                            helperText={isUrlValid === false && "Invalid URL - must start with http: or https:"}
                        />
                        <ButtonWrapper>
                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                                size="medium"
                                startIcon={<AddIcon />}
                            >
                                Create
                            </Button>
                        </ButtonWrapper>
                    </form>
                </Box>
                {urlHash && (
                    <Box>
                        <span>Stub: </span>
                        <Link href={`${process.env.HOST}/api/${urlHash}`} target="_blank" rel="noreferrer">
                            {`${process.env.HOST}/api/${urlHash}`}
                        </Link>
                    </Box>
                )}
                {isError && <Alert severity="error">Oops, something went wrong.</Alert>}
            </Grid>
        </Container>
    );
};

export default LinkStubCreator;
