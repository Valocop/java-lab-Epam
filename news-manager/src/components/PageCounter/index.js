import React, { Component } from "react";
import "./style.css";

class PageCounter extends React.Component {
    
	render() {
		return (
			<div className="page-bar">
				<select
					className="select-count"
					value={this.props.limit}
					onChange={this.props.handleChangeLimit}
				>
					<option value="1">1</option>
					<option value="2">2</option>
					<option value="5">5</option>
					<option value="10">10</option>
					<option value="50">50</option>
				</select>
			</div>
		);
	}
}

export default PageCounter;
