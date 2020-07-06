import React from "react";
import ReactPaginate from 'react-paginate';
import style from './Pagination.module.css'

const Pagination = (props) => {

    const totalCount = Math.ceil(props.totalNewsCount / props.pageSize);

    let onChangedPageSize = (e) => {
        props.onPageSizeChanged(e.currentTarget.value);
    };

    return (
        <div className={style.paginationWrapper}>
            <div className={style.selectWrapper}>
                <select value={props.pageSize} className={style.select}
                        onChange={onChangedPageSize}>
                    <option value={2}>2</option>
                    <option value={5}>5</option>
                    <option value={10}>10</option>
                    <option value={20}>20</option>
                </select>
            </div>
            <div>
                <ReactPaginate
                    onPageChange={(data) => props.onCurrentPageChanged(data.selected + 1)}
                    forcePage={props.currentPage - 1}
                    previousLabel={'previous'}
                    nextLabel={'next'}
                    breakLabel={'...'}
                    pageCount={totalCount}
                    marginPagesDisplayed={2}
                    pageRangeDisplayed={10}
                    containerClassName={style.pagination}
                    subContainerClassName={'pages pagination'}
                    activeClassName={style.active}
                    breakClassName={'break-me'}
                />
            </div>
        </div>
    )
};

export default Pagination;