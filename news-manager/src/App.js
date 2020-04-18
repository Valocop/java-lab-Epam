import React, {Component} from "react";
import Header from "./components/Header/Header";
import Footer from "./components/Footer/Footer";
import style from './App.module.css'
import NavBar from "./components/NavBar/NavBar";
import NewsContainer from "./components/News/NewsContainer";
import {Route, withRouter, Switch} from "react-router-dom";
import SingleNewsContainer from "./components/News/SingleNewsContainer";
import AddNewsContainer from "./components/AddNews/AddNewsContainer";
import EditNewsContainer from "./components/EditNews/EditNewsContainer";
import AuthorContainer from "./components/Authors/AuthorContainer";
import TagContainer from "./components/Tags/TagContainer";
import Login from "./components/Login/Login";
import {compose} from "redux";
import {connect} from "react-redux";
import {initializeApp} from "./redux/app-reducer";
import Preloader from "./components/common/Preloader/Preloader";

// import {Route, Switch, withRouter} from 'react-router';

class App extends Component {

    componentDidMount() {
        this.props.initializeApp();
    }

    render() {
        if (!this.props.initialized) {
            return <Preloader/>
        } else {
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
                        <Route path={'/login'} render={() => <Login/>}/>
                    </div>
                </div>
            );
        }
    }
}

const mapStateToProps = (state) => ({
    initialized: state.app.initialized
});

export default compose(withRouter, connect(mapStateToProps, {initializeApp}))(App);