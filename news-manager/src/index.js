import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./index.css";

class Tag extends React.Component {
	// render() {
	//     return(
	//     )
	// }
}

class User extends React.Component {}

class NewsManaget extends React.Component {}

function Footer() {
	return (
		<h5>
			Copyright EPAM 2020. All Rights Reserved
		</h5>
	);
}

class Login extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			login: "",
			password: ""
		};

		this.handleLoginChange = this.handleLoginChange.bind(
			this
		);
		this.handlePasswordChange = this.handlePasswordChange.bind(
			this
		);
		this.handleSubmit = this.handleSubmit.bind(
			this
		);
		this.handleCancel = this.handleCancel.bind(
			this
		);
	}

	handleLoginChange(e) {
		this.setState({ login: e.target.value });
	}

	handlePasswordChange(e) {
		this.setState({ password: e.target.value });
	}

	handleSubmit(e) {
		alert(this.state.login);
	}

	handleCancel(e) {
		this.setState({
			login: "",
			password: ""
		});
	}

	render() {
		return (
			<div className="login-container">
				<div>
					<h1>Login</h1>
				</div>
				<div>
					<form>
						<label for="flogin">Login:</label>
						<input
							type="text"
							id="flogin"
							value={this.state.login}
							placeholder="Input your login"
							onChange={this.handleLoginChange}
						></input>
						<label for="fpass">Password:</label>
						<input
							type="password"
							id="fpass"
							value={this.state.password}
							placeholder="Input your password"
							onChange={this.handlePasswordChange}
						></input>
					</form>
				</div>
				<button onClick={this.handleSubmit}>
					Login
				</button>
				<button onClick={this.handleCancel}>
					Cancel
				</button>
			</div>
		);
	}
}

class Main extends React.Component {
	render() {
		return (
			<div>
				<header>Header</header>
				<div className="main">
					<Login />
					{/* <article>
						<Login />
					</article> */}
				</div>
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
