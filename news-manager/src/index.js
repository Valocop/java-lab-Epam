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

		this.onSubmit = this.onSubmit.bind(this);
		this.on;
	}

	handleLoginChange(e) {
		this.setState({ login: e.target.value });
    }
    
    handlePasswordChange(e)

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
							// value={login}
							placeholder="Input your login"
							onChange={this.setLogin}
						></input>
						<label for="fpass">Password:</label>
						<input
							type="password"
							id="fpass"
							// value={password}
							placeholder="Input your password"
							onChange={this.setPassword}
						></input>
					</form>
				</div>
				<button onClick={this.onSubmit}>
					Login
				</button>
				<button onClick={this.clearLoginData}>
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
