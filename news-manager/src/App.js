import React, {Component} from "react";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import style from './App.module.css'
import NavBar from "./components/NavBar/NavBar";
import NewsContainer from "./components/News/NewsContainer";
import {Route, withRouter} from "react-router-dom";
import SingleNewsContainer from "./components/News/SingleNewsContainer";

class App extends Component {

    render() {
        return (
            <div>
                <Header/>
                <Footer/>
                <div className={style.contentWrapper}>
                    <NavBar/>
                    <Route exact path='/news' render={() => <NewsContainer/>}/>
                    <Route path='/news/:newsId' render={() => <SingleNewsContainer/>}/>
                </div>
            </div>
        );
    }
}

export default withRouter(App);