import React from 'react';
import { Pagination } from "react-bootstrap";

function CustomerPagination({ page, setPage, onPageChange }) {
    const handlePrevious = () => {
        if (page > 0) {
            setPage(page - 1);
            onPageChange();
        }
    };

    const handleNext = () => {
        setPage(page + 1);
        onPageChange();
    };

    return (
        <Pagination className='justify-content-center'>
            <Pagination.Prev 
                disabled={page === 0} 
                onClick={handlePrevious} 
            />
            <Pagination.Item active>{page + 1}</Pagination.Item>
            <Pagination.Next 
                onClick={handleNext}
            />
        </Pagination>
    );
}

export default CustomerPagination;