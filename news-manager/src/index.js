import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";
import Header from "./components/Header";
import Login from "./components/Login";

function Footer() {
	return <h5>Copyright EPAM 2020. All Rights Reserved</h5>;
}

class Main extends React.Component {
	constructor(props) {
		super(props);
		this.handleUserLogin = this.handleUserLogin.bind(this);
		this.handleLogout = this.handleLogout.bind(this);
		this.state = { isLogin: false, userName: "" };
	}

	handleUserLogin(login, pas) {
		if (login && pas) {
			this.setState({ userName: login, isLogin: true });
		} else {
			alert("Impossible to enter");
		}
	}

	handleLogout(userName, isLogin) {
		alert(userName + "out" + isLogin);
	}

	render() {
		const isLogin = this.state.isLogin;
		const userName = this.state.userName;

		let main = isLogin ? (
			<article>Hello {userName}</article>
		) : (
			<Login handleSubmit={this.handleUserLogin} />
		);

		return (
			<div>
				<Header
					userName={userName}
					isLogin={isLogin}
					handleLogout={this.handleLogout}
				/>
				<div className="main">{main}</div>
				<footer>
					<Footer />
				</footer>
			</div>
		);
	}
}

ReactDOM.render(<Main />, document.getElementById("root"));
