import React, { Component } from "react";
import "./style.css";
import SearchBar from "../SearchBar";
import News from "../News";

class NewsList extends React.Component {
	constructor(props) {
		super(props);
		this.state = {
			newsList: [],
			error: null
		};
	}

	componentDidMount() {
		fetch(
			"http://localhost:8080/news-manager/news"
		)
			.then(res => res.json())
			.then(
				result => {
					console.log(result);
					this.setState({
						newsList: result
					});
				},
				error => {
					alert(error);
					this.setState({
						error: error
					});
				}
			);
	}

	render() {
		const news = this.state.newsList.map(news => (
			<div key={news.id}>
				<News
					id={news.id}
					newsTitle={news.title}
					newsAuthor={news.author}
					newsCreationDate={news.creationDate}
					newsModificationDate={
						news.modificationDate
					}
					newsShortDescription={news.shortText}
					newsFullDescription={news.fullText}
					newsTags={news.tags}
					isShort={true}
				/>
			</div>
		));

		return (
			<div className="main">
				<div className="side-bar">Side bar</div>
				<div className="news-bar">
					<div className="search-bar">
						<div className="search">
							<div className="multi-search">
								<SearchBar />
							</div>
							<button>search</button>
						</div>
						<div className="search">
							<div className="multi-search">
								<SearchBar />
							</div>
							<button>reset</button>
						</div>
					</div>
					<div className="news-list">{news}</div>
					<div className="paggination-bar">
						paggination
					</div>
				</div>
			</div>
		);
	}
}

export default NewsList;
