import React, { Component } from "react";
import "./style.css";

class Header extends React.Component {
	constructor(props) {
		super(props);
		this.handleLogOut = this.handleLogOut.bind(
			this
		);
		this.handleLogin = this.handleLogin.bind(
			this
		);
	}

	handleLogOut() {
		this.props.handleLogOut();
	}

	handleLogin() {
		this.props.handleLogin();
	}

	render() {
		const userName = this.props.userName;
		const isLogin = this.props.isLogin;

		let register = isLogin ? (
			<div>
				<div>
					<h4>Welcome back, {userName}</h4>
				</div>
				<div>
					<button onClick={this.handleLogOut}>
						Logout
					</button>
				</div>
			</div>
		) : (
			<div>
				<div>
					<button onClick={this.handleLogin}>
						Login
					</button>
				</div>
				<div>
					<button>Register</button>
				</div>
			</div>
		);

		return (
			<header>
				<div className="content">
					<div className="home">
						<a href="/">News portal</a>
					</div>
					<div className="lang-link">
						<a href="/">EN</a>
						<a href="/">RU</a>
						<a href="/">FR</a>
					</div>
					{register}
				</div>
			</header>
		);
	}
}

export default Header;
