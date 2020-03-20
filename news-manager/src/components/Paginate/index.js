import React, { Component } from "react";
import "./style.css";
import ReactPaginate from "react-paginate";

class Paginate extends React.Component {
	render() {
		return (
			<div className="commentBox">
				<ReactPaginate
					previousLabel={"previous"}
					nextLabel={"next"}
					breakLabel={"..."}
					breakClassName={"break-me"}
					pageCount={this.props.pageCount}
					marginPagesDisplayed={2}
					pageRangeDisplayed={5}
					onPageChange={this.props.handlePageClick}
					containerClassName={"pagination"}
					subContainerClassName={"pages pagination"}
					activeClassName={"active"}
					initialPage={0}
					forcePage={this.props.forcePage}
					pageClassName={
						this.props.singleMode ? "hide-li" : null
					}
				/>
			</div>
		);
	}
}

export default Paginate;
