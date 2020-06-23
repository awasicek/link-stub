import React from "react";
import Home from "@components/home";
import Global from "@components/global";
import { StylesProvider } from "@material-ui/core/styles";

const App = () => (
    // allow styled components to override material-ui styling with injectFirst
    <StylesProvider injectFirst>
        <Global />
        <Home />
    </StylesProvider>
);

export default App;
