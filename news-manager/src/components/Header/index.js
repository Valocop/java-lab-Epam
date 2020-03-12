import React, { Component } from "react";
import "./style.css";

class Header extends React.Component {
	constructor(props) {
		super(props);
		this.handleLogOut = this.handleLogOut.bind(this);
	}

	handleLogOut(userName, isLogin) {
		this.props.handleLogOut();
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
					<button
						onClick={this.handleLogOut}
					>
						Logout
					</button>
				</div>
			</div>
		) : (
			<div>
				<div>
					<button>Login</button>
				</div>
				<div>
					<button>Register</button>
				</div>
			</div>
		);

		return (
			<header>
				<div>
					<h2>NewsPortal</h2>
				</div>
				<div>
					<a href="/">EN</a>
					<a href="/">RU</a>
					<a href="/">FR</a>
				</div>
				{register}
			</header>
		);
	}
}

export default Header;
