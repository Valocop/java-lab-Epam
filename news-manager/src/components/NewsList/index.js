import React, { Component } from "react";
import "./style.css";
import SearchBar from "../SearchBar";
import News from "../News";
import Paginate from "../Paginate";
import PageCounter from "../PageCounter";
import DashBoard from "../DashBoard";

class NewsList extends React.Component {
	constructor(props) {
		super(props);
		this.multiselectTags = React.createRef();
		this.multiselectAuthors = React.createRef();
		this.state = {
			newsList: [],
			tagsList: [],
			authorsList: [],
			isSearch: false,
			totalCount: 0,
			pageCount: 0,
			offset: 0,
			selected: 0,
			limit: 2,
			forcePage: 0,
			newsError: null,
			tagsError: null,
			authorsError: null,
			singleMode: false,
			addEditNewsMode: false,
			lastState: null
		};
		this.resetSearch = this.resetSearch.bind(this);
		this.searchNews = this.searchNews.bind(this);
		this.handleChangeLimit = this.handleChangeLimit.bind(this);
		this.handlePageClick = this.handlePageClick.bind(this);
		this.handleNewsClick = this.handleNewsClick.bind(this);
		this.handleNewsClose = this.handleNewsClose.bind(this);
		this.handleSearchNews = this.handleSearchNews.bind(this);
		this.handleDeleteNews = this.handleDeleteNews.bind(this);
		this.handleEditNews = this.handleEditNews.bind(this);
	}

	getUnique(arr, comp) {
		const unique = arr
			.map(e => e[comp])
			.map((e, i, final) => final.indexOf(e) === i && i)
			.filter(e => arr[e])
			.map(e => arr[e]);
		return unique;
	}

	getAuthors() {
		fetch("http://localhost:8080/news-manager/authors")
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						authorsList: this.getUnique(result, "name")
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

	getNews() {
		console.log(this.state.isSearch);
		if (this.state.isSearch) {
			this.searchNews();
		} else {
			fetch(
				"http://localhost:8080/news-manager/news?" +
					"limit=" +
					this.state.limit +
					"&offset=" +
					this.state.offset
			)
				.then(res => res.json())
				.then(
					result => {
						this.setState({
							newsList: result.news,
							totalCount: result.count,
							pageCount: Math.ceil(result.count / this.state.limit)
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
	}

	getTags() {
		fetch("http://localhost:8080/news-manager/tags")
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						tagsList: this.getUnique(result, "name")
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
		this.setState(
			{
				isSearch: false,
				selected: 0,
				offset: 0,
				forcePage: 0
			},
			() => this.getNews()
		);
	}

	handleSearchNews() {
		this.setState(
			{
				selected: 0,
				offset: 0,
				forcePage: 0
			},
			() => this.searchNews()
		);
	}

	searchNews() {
		this.setState({
			isSearch: true
		});
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
				tags +
				"&limit=" +
				this.state.limit +
				"&offset=" +
				this.state.offset
		)
			.then(res => res.json())
			.then(
				result => {
					this.setState({
						newsList: result.news,
						totalCount: result.count,
						pageCount: Math.ceil(result.count / this.state.limit)
					});
				},
				error => {
					alert("searchNews error: " + error);
					this.setState({
						newsError: error
					});
				}
			);
	}

	handlePageClick(data) {
		this.setState({
			forcePage: data.selected,
			selected: data.selected
		});
		let selected = data.selected;
		let offset = Math.ceil(selected * this.state.limit);

		this.setState({ offset: offset }, () => {
			this.getNews();
		});
	}

	handleChangeLimit(event) {
		this.setState(
			{
				selected: 0,
				offset: 0,
				forcePage: 0,
				limit: event.target.value
			},
			() => this.getNews()
		);
	}

	handleNewsClick(news) {
		this.setState(state => ({
			lastState: {
				limit: state.limit,
				offset: state.offset,
				selected: state.selected,
				forcePage: state.forcePage
			}
		}));
		this.setState(
			{
				limit: 1,
				offset: news.offset,
				selected: news.offset,
				forcePage: news.offset,
				singleMode: true
			},
			() => this.getNews()
		);
	}

	handleNewsClose() {
		const lastState = this.state.lastState;
		this.setState(
			{
				singleMode: false,
				selected: lastState.selected,
				offset: lastState.offset,
				forcePage: lastState.forcePage,
				limit: lastState.limit
			},
			() => this.getNews()
		);
	}

	handleDeleteNews(news) {
		var status;
		alert("Must delete news: " + news.id);
		fetch("http://localhost:8080/news-manager/news/" + news.id, {
			method: "DELETE"
		}).then(
			response => {
				alert("response status: " + response.status);
				status = response.status;
			},
			error => {
				alert("searchNews error: " + error);
				this.setState({
					newsError: error
				});
			}
		);
		if (status.ok) {
			this.getNews();
		} else {
			alert("status fail");
		}
	}

	handleEditNews(news) {
		alert("edit news :" + news.id);
		this.setState({
			addEditNewsMode: true
		});
	}

	render() {
		const news = this.state.newsList.map(news => (
			<div key={news.id}>
				<News
					id={news.id}
					newsTitle={news.title}
					newsAuthor={news.author}
					newsCreationDate={news.creationDate}
					newsModificationDate={news.modificationDate}
					newsShortDescription={news.shortText}
					newsFullDescription={news.fullText}
					newsTags={news.tags}
					singleMode={this.state.singleMode}
					handleNewsClick={this.handleNewsClick}
					selected={this.state.selected}
					limit={this.state.limit}
					position={this.state.newsList.indexOf(news)}
					handleNewsClose={this.handleNewsClose}
					isLogin={this.props.isLogin}
					handleDeleteNews={this.handleDeleteNews}
					handleEditNews={this.handleEditNews}
				/>
			</div>
		));

		return (
			<div className="main">
				<div className="side-bar">
					{this.props.isLogin && <DashBoard />}
				</div>
				<div className="news-bar">
					<div
						className={
							this.state.singleMode || this.state.addEditNewsMode
								? "search-bar-hidden"
								: "search-bar"
						}
					>
						<div className="search">
							<div className="multi-search">
								<SearchBar
									data={this.state.tagsList}
									multiselectRef={this.multiselectTags}
								/>
							</div>
							<button onClick={this.handleSearchNews}>search</button>
						</div>
						<div className="search">
							<div className="multi-search">
								<SearchBar
									data={this.state.authorsList}
									multiselectRef={this.multiselectAuthors}
								/>
							</div>
							<button onClick={this.resetSearch}>reset</button>
						</div>
					</div>
					{!this.state.addEditNewsMode && (
						<div className="news-list">{news}</div>
					)}
					{!this.state.addEditNewsMode && (
						<div className="paggination-bar">
							{!this.state.singleMode && (
								<PageCounter
									limit={this.state.limit}
									handleChangeLimit={this.handleChangeLimit}
								/>
							)}
							<Paginate
								pageCount={this.state.pageCount}
								handlePageClick={this.handlePageClick}
								forcePage={this.state.forcePage}
								singleMode={this.state.singleMode}
							/>
						</div>
					)}
				</div>
			</div>
		);
	}
}

export default NewsList;
