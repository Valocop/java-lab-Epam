import React, { Component } from "react";
import "./style.css";
import { Multiselect } from "multiselect-react-dropdown";

class SearchBar extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			selectedItems: []
		};
		this.onSearchSelected = this.onSearchSelected.bind(
			this
		);
		this.onSearchDeleted = this.onSearchDeleted.bind(
			this
		);
	}

	onSearchSelected(selectedList, selectedItem) {
		this.setState({
			selectedItems: selectedList
		});
		console.log(this.state.selectedItems);
	}

	onSearchDeleted(selectedList, removedItem) {
		this.setState({
			selectedItems: selectedList
		});
		console.log(this.state.selectedItems);
	}

	render() {
		return (
			<Multiselect
				options={this.props.data}
				selectedValues={this.state.selectedItems}
				onSelect={this.onSearchSelected}
				onRemove={this.onSearchDeleted}
				displayValue={"name"}
				ref={this.props.multiselectRef}
			/>
		);
	}
}

export default SearchBar;
