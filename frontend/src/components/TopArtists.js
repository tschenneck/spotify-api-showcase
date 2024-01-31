import {withRouter} from "react-router-dom";
// import {React, useEffect, useState} from "react";
import React, { useEffect, useState } from 'react'
import {Button} from "@material-ui/core";

function TopArtists() {

    const [userTopArtists, setUserTopArtists] = useState();

    useEffect(() => {
        fetch("http://localhost:8080/api/user-top-artists")
        .then(response => response.json())
        .then(data => {
            console.log(data)
            setUserTopArtists(data)
            })
    }, [])

    const createPlaylist = () => {
        fetch("http://localhost:8080/api/create-top-playlist")
            .then((response) => response.text())
            .then(response => {
                window.location.replace(response);
            })
    }

    return (
        <div>
            {userTopArtists ? (
                userTopArtists.map((artistResult) => {
                    return <h1 key= {artistResult.name}>{artistResult.name}</h1>
                })
            ):
            (
                <h1>LOADING...</h1>
    )}
            <div style={{display: "flex", justifyContent: "left", margin: "1rem", marginTop: "5rem"}}>
                <Button variant = "contained" color = "primary" onClick = {createPlaylist}>Create Playlist</Button>
            </div>
        </div>
    );
}

export default withRouter(TopArtists);
