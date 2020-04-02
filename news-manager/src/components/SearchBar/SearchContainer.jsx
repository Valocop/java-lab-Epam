import React, {useState, useEffect} from 'react';
import SearchBar from "./SearchBar";

const SearchContainer = (props) => {
    let [selectedTags, setSelectedTags] = useState(props.selectedTags);
    let [selectedAuthors, setSelectedAuthors] = useState(props.selectedAuthors);

    useEffect(() => {
        setSelectedTags(props.selectedTags);
    }, [props.selectedTags]);

    useEffect(() => {
        setSelectedAuthors(props.selectedAuthors);
    }, [props.selectedAuthors]);

    const tags = props.tags.map(tag => ({
        id: tag.id,
        name: tag.name,
        value: tag.name,
        label: tag.name
    }));

    const authors = props.authors.map(author => ({
        id: author.id,
        name: author.name,
        surname: author.surname,
        value: author.name,
        label: author.name
    }));

    let onSelectedTagsChanged = (tags) => {
        setSelectedTags(tags ? tags : []);
    };

    let onSelectedAuthorsChanged = (authors) => {
        setSelectedAuthors(authors ? authors : []);
    };

    let onSearch = () => {
        let tagsNames = selectedTags.map(tag => tag.name);
        let authorsNames = selectedAuthors.map(author => author.name);
        props.setSelectedTags(selectedTags);
        props.setSelectedAuthors(selectedAuthors);
        props.setCurrentPage(1);
        props.requestNews(1, props.pageSize, authorsNames, tagsNames);
    };

    return (
        <SearchBar
            tags={tags}
            selectedTags={selectedTags}
            setSelectedTags={onSelectedTagsChanged}
            onSearch={onSearch}
            authors={authors}
            selectedAuthors={selectedAuthors}
            setSelectedAuthors={onSelectedAuthorsChanged}
            resetSearch={props.resetSearch}
        />
    )
};

export default SearchContainer;