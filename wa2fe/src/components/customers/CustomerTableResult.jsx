'use strict';
import React, { useCallback, useMemo } from 'react';
import { AgGridReact } from "ag-grid-react";
import "ag-grid-community/styles/ag-grid.css";
import "ag-grid-community/styles/ag-theme-quartz.css";

function CustomerTableResult(props) {
    const { customers, setSelectedCustomer } = props.customers;
    const { searchLoading } = props.loadingProps;

    const columnDefs = useMemo(() => [
        { 
            headerName: 'Customer', 
            valueGetter: p => `${p.data.name} ${p.data.surname}`,
            flex: 1
        },
        { headerName: 'SSN', field: 'ssnCode', flex: 1 },
        { headerName: 'Category', field: 'category', flex: 1 }
    ], []);

    const defaultColDef = useMemo(() => {
        return {
            resizable: true,
            sortable: true,
            filter: true,
        };
    }, []);

    const rowSelection = useMemo(() => {
        return {
            mode: 'singleRow',
            enableClickSelection: true,
        };
    }, []);

    const onRowSelected = useCallback((event) => {
        if (!!event.node.selected)
            setSelectedCustomer(event.data);
    }, [setSelectedCustomer]);

    return (
        <div style={{width: '100%', height: '470px'}} className="mb-2">
            <div style={{width: '100%', height: '100%'}}
                className={"ag-theme-quartz"} >
                <AgGridReact
                    rowData={customers}
                    columnDefs={columnDefs}
                    defaultColDef={defaultColDef}
                    rowSelection={rowSelection}
                    onRowSelected={onRowSelected}
                    loading={searchLoading}
                    overlayLoadingTemplate={
                        '<span class="ag-overlay-loading-center">Loading...</span>'
                    }
                    overlayNoRowsTemplate={
                        '<span class="ag-overlay-no-rows-center">No customers found</span>'
                    }
                />
            </div>
        </div>
    );
}

export default CustomerTableResult;