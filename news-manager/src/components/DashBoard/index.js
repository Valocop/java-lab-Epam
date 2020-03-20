import React, { Component } from "react";
import "./style.css";

class DashBoard extends React.Component {
	render() {
		return (
			<div className="dash-board">
				<span>DashBoard</span>
				<ul>
					<li>Add/Edit Authors</li>
					<li>Add News</li>
					<li>Add/Edit Tags</li>
				</ul>
			</div>
		);
	}
}

export default DashBoard;
