import React from "react";
import {Route, Switch} from "react-router-dom";
import LandingPage from "./components/LandingPage";
import TopArtists from "./components/TopArtists";
import CreatePlaylist from "./components/CreatePlaylist";
import "./App.css";
import Nav from "./components/Nav";


function App() {
  return (
    <div>
      <Nav/>
      <Switch>
        <Route exact path = "/" component = {LandingPage} />
        <Route exact path = "/top-artists" component = {TopArtists} />
        <Route exact path = "/generate-playlist" component = {CreatePlaylist} />
      </Switch>
    </div>
  );
}

export default App;
