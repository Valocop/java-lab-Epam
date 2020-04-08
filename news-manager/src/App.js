import React, {Component} from "react";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import style from './App.module.css'
import NavBar from "./components/NavBar/NavBar";
import NewsContainer from "./components/News/NewsContainer";
import {Route, withRouter} from "react-router-dom";
import SingleNewsContainer from "./components/News/SingleNewsContainer";
import AddNewsContainer from "./components/AddNews/AddNewsContainer";
import EditNewsContainer from "./components/EditNews/EditNewsContainer";
import AuthorContainer from "./components/Authors/AuthorContainer";
import TagContainer from "./components/Tags/TagContainer";

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
                    <Route exact path='/addNews' render={() => <AddNewsContainer/>}/>
                    <Route path='/editNews/:newsId' render={() => <EditNewsContainer/>}/>
                    <Route path='/authors' render={() => <AuthorContainer/>}/>
                    <Route path={'/tags'} render={() => <TagContainer/>}/>
                </div>
            </div>
        );
    }
}

export default withRouter(App);