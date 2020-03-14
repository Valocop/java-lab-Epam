import React, { Component } from "react";
import "./style.css";
import { Multiselect } from "multiselect-react-dropdown";

class SearchBar extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			options: [
				{ name: "Srigar", id: 1 },
				{ name: "Sam", id: 2 },
				{ name: "Srigar", id: 1 },
				{ name: "Sam", id: 2 },
				{ name: "Srigar", id: 1 },
				{ name: "Sam", id: 2 }
			]
		};
	}

	render() {
		return (
			<Multiselect
				options={this.state.options}
				selectedValues={this.state.selectedValue}
				// onSelect={this.onSelect}
				// onRemove={this.onRemove}
				displayValue="name"
			/>
		);
	}
}

export default SearchBar;
