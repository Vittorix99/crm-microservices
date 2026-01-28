'use strict'
import {Pagination} from "react-bootstrap";
import React, {useEffect} from "react";


function ProfessionalPagination(props) {
    const {page, setPage} = props.pageProp;

    return (
        <Pagination className='justify-content-center '>
            <Pagination.Prev hidden={!(page > 0)} onClick={() => setPage(page-1)} />
            <Pagination.Item active className='pagination' >{page}</Pagination.Item>
            <Pagination.Next onClick={() => setPage(page+1)}/>
        </Pagination>
    )
}

export default ProfessionalPagination;