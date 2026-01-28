import React, {useCallback, useEffect, useMemo, useState} from "react";
import {AgGridReact} from "ag-grid-react";
import {getJobOfferInterviews} from "../../api/joboffers.js";
import {Card, Row} from "react-bootstrap";

const InterviewTable = (props) => {
    const { interviews, setInterviews } = props.interviewProp;

    useEffect(() => {
        getJobOfferInterviews(props.jobOffersId).then((res) => {
            setInterviews(res); // Fetch all interviews
        });
    }, []);

    const columnDefs = [
        { headerName: 'Id', field: 'id' },
        { headerName: 'Feedback', field: 'feedback' },
        { headerName: 'Date', field: 'date' },
    ];

    const autoSizeStrategy = useMemo(() => {
        return {
            type: 'fitGridWidth',
            defaultMinWidth: 90,
        };
    }, []);

    return (
        <div style={{ width: '100%', height: '470px' }} className="mb-2">
            <div style={{ width: '100%', height: '100%' }} className="ag-theme-quartz">
                <AgGridReact
                    rowData={interviews}
                    columnDefs={columnDefs}
                    autoSizeStrategy={autoSizeStrategy}
                />
            </div>
        </div>
    );
};

export default InterviewTable;
