import React, { Component } from "react";
import "./style.css";

class News extends React.Component {
	constructor(props) {
		super(props);
		this.handleNewsClick = this.handleNewsClick.bind(this);
		this.handleNewsClose = this.handleNewsClose.bind(this);
		this.handleDeleteNews = this.handleDeleteNews.bind(this);
		this.handleEditNews = this.handleEditNews.bind(this);
	}

	handleNewsClick(news) {
		this.props.handleNewsClick(news);
	}

	handleNewsClose() {
		this.props.handleNewsClose();
	}

	handleDeleteNews(news) {
		this.props.handleDeleteNews(news);
	}

	handleEditNews(news) {
		this.props.handleEditNews(news);
	}

	render() {
		const news = {
			id: this.props.id,
			title: this.props.newsTitle,
			limit: this.props.limit,
			selected: this.props.selected,
			offset: Math.ceil(
				this.props.selected * this.props.limit + this.props.position
			)
		};
		const tags = this.props.newsTags.map(tag => (
			<div key={tag.id} className="news-tag">
				{tag.name}
			</div>
		));
		const style = this.props.singleMode
			? "news-item-single"
			: "news-item";

		return (
			<div className={style}>
				<div className="news-header">
					<div className="news-title-close">
						<div
							className="news-title"
							onClick={() => this.handleNewsClick(news)}
						>
							{this.props.newsTitle + "  id: " + this.props.id}
						</div>
						{this.props.singleMode && (
							<div className="close-but">
								<button onClick={this.handleNewsClose}>close</button>
							</div>
						)}
					</div>
					<div className="news-info">
						<div className="news-author">
							{this.props.newsAuthor.name +
								" " +
								this.props.newsAuthor.surname}
						</div>
						<div className="news-date">
							{this.props.newsCreationDate}
						</div>
					</div>
				</div>
				<div className="news-short-description">
					{this.props.newsShortDescription}
				</div>
				{this.props.singleMode && (
					<div className="news-full-description">
						{this.props.newsFullDescription}
					</div>
				)}
				<div className="news-tags">{tags}</div>
				{this.props.isLogin && (
					<div className="control-news">
						<button onClick={() => this.handleEditNews(news)}>
							EDIT
						</button>
						<button onClick={() => this.handleDeleteNews(news)}>
							DELETE
						</button>
					</div>
				)}
			</div>
		);
	}
}

export default News;
