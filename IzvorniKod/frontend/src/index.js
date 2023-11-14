import React from "react";
import ReactDOM from "react-dom/client";

import App from "./App";
import Reports from "./Report";

const root = ReactDOM.createRoot(document.getElementById("root"));
root.render(
  <React.StrictMode>
    <Reports />
    <App></App>
  </React.StrictMode>
);
