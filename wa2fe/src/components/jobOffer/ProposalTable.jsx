import React, {useCallback, useEffect, useMemo, useState} from "react";
import {AgGridReact} from "ag-grid-react";
import useProposals from "../../hooks/useProposals.js";
import {getJobOfferInterviews, getProposalsFromJobOffer} from "../../api/joboffers.js";

const ProposalTable = (props) => {
    const {proposals, setProposals} = props.proposals;
    const {selectedProposal, setSelectedProposal} = props.selectedProposalsProp;

    const columnDefs = [
        { headerName: 'Id', field: "id" },
        {headerName: 'Description', field: "description"},
        { headerName: 'Status', field: "status"}
    ]
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
            setSelectedProposal(event.data)
        else
            setSelectedProposal(null);
    }, [window])

    return (
        <div style={{width: '100%', height: '470px'}} className="mb-2">
            <div style={{width: '100%', height: '100%'}}
                 className={ "ag-theme-quartz" } >
                <AgGridReact
                    rowData={proposals}
                    columnDefs={columnDefs}
                    autoSizeStrategy={autoSizeStrategy}
                    rowSelection={rowSelection}
                    onRowSelected={onRowSelected}
                />
            </div>
        </div>
    )
}

export default ProposalTable;