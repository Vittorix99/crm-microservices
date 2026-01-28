import {useEffect, useState} from "react";
import {getAllMessages, getHistoryMessage, getMessageById} from "../api/messages.js";

export const useMessages = ({setError, modalShow}) => {
    const [messages, setMessages] = useState([])
    const [selectedMsg, setSelectedMsg] = useState()
    const [msgStatusFilter, setMsgStatusFilter] = useState([])
    const [msgPriorityFilter, setMsgPriorityFilter] = useState([])
    const [msgHistory, setMshHistory] = useState([])
    const [loading, setLoading] = useState(true)
    const [page, setPage] = useState(0)
    const [update, setUpdate] = useState()

    useEffect(() => {

        const params = {
            msgStatusFilter: msgStatusFilter.map(it => it.label).join(','),
            msgPriorityFilter: msgPriorityFilter.map(it => it.label).join(','),
            page
        }

        if(msgStatusFilter.length === 0)
            delete params.msgStatusFilter

        if(msgPriorityFilter.length === 0)
            delete params.msgPriorityFilter


        const fetchMessages = async () => {
            const msgRes = await getAllMessages(params)
            if(!!msgRes && msgRes.status === 200)
                return msgRes.data
        }
        if(update !== false) {
            setLoading(true)
            fetchMessages()
                .then((res) => {
                    setMessages(res)
                    setLoading(false)
                    setUpdate(false)})
                .catch((e) => {
                    setError()
                    setLoading(false)
                    setUpdate(false)
                })
        }
    }, [page, update]);

    useEffect(() => {
        const fetchMessage = async () => {
            const historyMsg = await getHistoryMessage(selectedMsg.id)
            if(!!historyMsg && historyMsg.status === 200)
                return historyMsg.data
        }

        if(selectedMsg && modalShow) {
            setLoading(true)
            fetchMessage()
                .then((res) => {
                    setMshHistory(res)
                    setLoading(false)})
                .catch((e) => {
                    setError()
                    setLoading(false)
                })
        }
    }, [modalShow]);

    return [messages, selectedMsg, msgHistory, setMsgStatusFilter, setMsgPriorityFilter, loading, setSelectedMsg, setPage, setUpdate]
}