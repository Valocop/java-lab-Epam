import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";
import Header from "./components/Header";
import Login from "./components/Login";
import Footer from "./components/Footer";
import NewsList from "./components/NewsList";

class Main extends React.Component {
	constructor(props) {
		super(props);
		this.handleUserLogin = this.handleUserLogin.bind(
			this
		);
		this.handleUserLogout = this.handleUserLogout.bind(
			this
		);
		this.handleShowLogin = this.handleShowLogin.bind(
			this
		);
		this.handleHideLogin = this.handleHideLogin.bind(
			this
		);
		this.state = {
			isLogin: false,
			userName: "",
			showLoginComponent: false
		};
	}

	handleUserLogin(login, pas) {
		if (login && pas) {
			this.setState({
				userName: login,
				isLogin: true
			});
			this.handleHideLogin();
		} else {
			alert("Impossible to enter");
		}
	}

	handleUserLogout() {
		this.setState({
			isLogin: false,
			userName: ""
		});
		this.handleHideLogin();
	}

	handleHideLogin() {
		this.setState({
			showLoginComponent: false
		});
	}

	handleShowLogin() {
		this.setState({
			showLoginComponent: true
		});
	}

	render() {
		const isLogin = this.state.isLogin;
		const userName = this.state.userName;
		const showLoginComponent = this.state
			.showLoginComponent;

		let main = !showLoginComponent ? (
			<NewsList />
		) : (
			<Login
				handleSubmit={this.handleUserLogin}
				handleCancel={this.handleHideLogin}
			/>
		);

		return (
			<div>
				<Header
					handleLogOut={this.handleUserLogout}
					handleLogin={this.handleShowLogin}
					userName={userName}
					isLogin={isLogin}
				/>
				{main}
				<footer>
					<Footer />
				</footer>
			</div>
		);
	}
}

ReactDOM.render(
	<Main />,
	document.getElementById("root")
);
