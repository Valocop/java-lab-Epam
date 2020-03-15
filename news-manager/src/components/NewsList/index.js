import React, { Component } from "react";
import "./style.css";
import SearchBar from "../SearchBar";
import News from "../News";

class NewsList extends React.Component {
	constructor(props) {
		super(props);
		this.multiselectTags = React.createRef();
		this.multiselectAuthors = React.createRef();
		this.state = {
			newsList: [],
			tagsList: [],
			authorsList: [],
			newsError: null,
			tagsError: null,
			authorsError: null
		};
		this.resetSearch = this.resetSearch.bind(
			this
		);
		this.searchNews = this.searchNews.bind(this);
	}

	getUnique(arr, comp) {
		const unique = arr
			.map(e => e[comp])
			.map(
				(e, i, final) =>
					final.indexOf(e) === i && i
			)
			.filter(e => arr[e])
			.map(e => arr[e]);
		return unique;
	}

	getAuthors(props) {
		fetch(
			"http://localhost:8080/news-manager/authors"
		)
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						authorsList: this.getUnique(
							result,
							"name"
						)
					});
				},
				error => {
					alert("getAuthors error: " + error);
					this.setState({
						authorsError: error
					});
				}
			);
	}

	getNews(props) {
		fetch(
			"http://localhost:8080/news-manager/news"
		)
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						newsList: result
					});
				},
				error => {
					alert("getNews error: " + error);
					this.setState({
						newsError: error
					});
				}
			);
	}

	getTags(props) {
		fetch(
			"http://localhost:8080/news-manager/tags"
		)
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						tagsList: this.getUnique(
							result,
							"name"
						)
					});
				},
				error => {
					alert("getTags error: " + error);
					this.setState({
						tagsError: error
					});
				}
			);
	}

	componentDidMount() {
		this.getNews();
		this.getTags();
		this.getAuthors();
	}

	resetSearch() {
		this.multiselectTags.current.resetSelectedValues();
		this.multiselectAuthors.current.resetSelectedValues();
		this.getNews();
	}

	searchNews() {
		const tags = this.multiselectTags.current
			.getSelectedItems()
			.map(tag => tag.name)
			.join(",");
		const authors = this.multiselectAuthors.current
			.getSelectedItems()
			.map(author => author.name)
			.join(",");
		fetch(
			"http://localhost:8080/news-manager/news?authors_name=" +
				authors +
				"&tags_name=" +
				tags
		)
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						newsList: result
					});
				},
				error => {
					alert("getNews error: " + error);
					this.setState({
						newsError: error
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
								<SearchBar
									data={this.state.tagsList}
									multiselectRef={
										this.multiselectTags
									}
								/>
							</div>
							<button onClick={this.searchNews}>
								search
							</button>
						</div>
						<div className="search">
							<div className="multi-search">
								<SearchBar
									data={this.state.authorsList}
									multiselectRef={
										this.multiselectAuthors
									}
								/>
							</div>
							<button onClick={this.resetSearch}>
								reset
							</button>
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
