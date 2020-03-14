import React, { Component } from "react";
import "./style.css";

class News extends React.Component {
	render() {
		const tags = this.props.newsTags.map(tag => (
			<div key={tag.id} className="news-tag">
				{tag.name}
			</div>
		));

		return (
			<div className="news-item">
				<div className="news-header">
					<div className="news-title">
						{this.props.newsTitle}
					</div>
					<div className="news-info">
						<div className="news-author">
							{this.props.newsAuthor.name + this.props.newsAuthor.surname}
						</div>
						<div className="news-date">
							{this.props.newsCreationDate}
						</div>
					</div>
				</div>
				<div className="news-short-description">
					{this.props.newsShortDescription}
				</div>
				{!this.props.isShort && (
					<div className="news-full-description">
						{this.props.newsFullDescription}
					</div>
				)}
				<div className="news-tags">{tags}</div>
			</div>
		);
	}
}

export default News;
