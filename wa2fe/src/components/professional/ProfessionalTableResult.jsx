'use strict';
import React, {useCallback, useEffect, useMemo} from 'react';
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-quartz.css";
import {Button} from "react-bootstrap";
import {Envelope} from "react-bootstrap-icons";

function ProfessionalTableResult(props) {
    const {professionals, setSelectedProfessional} = props.professionals;
    const {searchLoading} = props.loadingProps;

    const columnDefs = [
        { headerName: 'Professional', valueGetter: p => p.data.name + ' ' + p.data.surname , },
        { headerName: 'State', field: 'state' },
        { headerName: 'Daily rate', field: 'dailyRate' },
        { headerName: 'Location', field: 'location' },
        { headerName: 'SSN', field: 'ssnCode' },
    ];

    const autoSizeStrategy = useMemo(() => {
        return {
            type: "fitGridWidth",
            defaultMinWidth: 90,
        };
    }, []);

    const rowSelection = useMemo(() => {
        return {
            mode: 'singleRow',
            enableClickSelection: true,
        };
    }, []);

    const onRowSelected = useCallback((event) => {
            if(!!event.node.selected)
                setSelectedProfessional(event.data);
            else
                setSelectedProfessional(null);
        },
        [window],
    );

    return (
        <div style={{width: '100%', height: '470px'}} className="mb-2">
            <div style={{width: '100%', height: '100%'}}
                className={ "ag-theme-quartz" } >
                <AgGridReact
                    rowData={professionals}
                    columnDefs={columnDefs}
                    autoSizeStrategy={autoSizeStrategy}
                    rowSelection={rowSelection}
                    onRowSelected={onRowSelected}
                    loading={searchLoading}
                />
            </div>
        </div>
    )
}

export default ProfessionalTableResult;