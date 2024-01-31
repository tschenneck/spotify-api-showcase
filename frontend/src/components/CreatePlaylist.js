import React from "react";
import { Button } from '@material-ui/core';

import { withRouter } from "react-router-dom";
import { connect } from "react-redux";

function CreatePlaylist() {

  const createPlaylist = () => {
    fetch("http://localhost:8080/api/create-top-playlist")
    .then((response) => response.text())
    .then(response => {
      window.location.replace(response);
    })
  }

  return (
    <div>
      <div style={{display: "flex", justifyContent: "center"}}>
        <h1>Click here to create top playlist!</h1>
      </div>
      <div style={{display: "flex", justifyContent: "center", margin: "1rem"}}>
        <Button variant = "contained" color = "primary" onClick = {createPlaylist}>Sign In</Button>
      </div>


    </div>

  );
}

export default withRouter(CreatePlaylist);
