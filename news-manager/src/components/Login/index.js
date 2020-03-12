import React, { Component } from "react";
import ReactDOM from "react-dom";
import "./style.css";

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
		this.handleSubmit = this.handleSubmit.bind(this);
		this.handleCancel = this.handleCancel.bind(this);
	}

	handleLoginChange(e) {
		this.setState({ login: e.target.value });
	}

	handlePasswordChange(e) {
		this.setState({ password: e.target.value });
	}

	handleSubmit() {
		this.props.handleSubmit(
			this.state.login,
			this.state.password
		);
	}

	handleCancel() {
		this.setState({
			login: "",
			password: ""
		});
	}

	render() {
		const login = this.state.login;
		const password = this.state.password;

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
							value={login}
							placeholder="Input your login"
							onChange={this.handleLoginChange}
						></input>
						<label for="fpass">Password:</label>
						<input
							type="password"
							id="fpass"
							value={password}
							placeholder="Input your password"
							onChange={this.handlePasswordChange}
						></input>
					</form>
				</div>
				<button onClick={this.handleSubmit}>Login</button>
				<button onClick={this.handleCancel}>Cancel</button>
			</div>
		);
	}
}

export default Login;
