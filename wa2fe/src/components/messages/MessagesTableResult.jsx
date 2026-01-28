import {AgGridReact} from "ag-grid-react";
import React, {useCallback, useMemo} from "react";

const MessagesTableResult = (props) => {
    const {messages, setSelectedMsg} = props.messagesProp;
    const {loading} = props.loadingProp;

    const columnDefs = [
        { headerName: 'Sender', field: 'sender' },
        { headerName: 'Subject', field: 'subject' },
        { headerName: 'Channel', field: 'channel' },
        { headerName: 'Status', field: 'status' },
        { headerName: 'Priority', field: 'priority' },
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
            enableClickSelection: true
        };
    }, []);

    const onRowSelected = useCallback((event) => {
            if(!!event.node.selected)
                setSelectedMsg(event.data);
        },
        [window],
    );

    return (
        <div style={{width: '100%', height: '470px'}} className="mb-2">
            <div style={{width: '100%', height: '100%'}}
                 className={ "ag-theme-quartz" } >
                <AgGridReact
                    rowData={messages}
                    columnDefs={columnDefs}
                    autoSizeStrategy={autoSizeStrategy}
                    rowSelection={rowSelection}
                    onRowSelected={onRowSelected}
                    loading={loading}
                />
            </div>
        </div>
    )
}

export default MessagesTableResult;