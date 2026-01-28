import {useEffect, useState} from "react";
import {addNoteToCustomer, deleteCustomerNote, getCustomerNotes, updateCustomerNote} from "../api/customers.js";
import {
    addNoteToProfessional,
    deleteProfessional,
    getProfessionalNotes, removeNoteFromProfessional,
    updateProfessionalNote
} from "../api/professional.js";

const useContactNotes = ({selectedContact, modalShow}) => {
    const [notes, setNotes] = useState([])
    const [notesLoading, setNotesLoading] = useState(false)
    const [newNote, setNewNote] = useState({ title: '', description: '', contactId: selectedContact?.id });
    const [editingNoteId, setEditingNoteId] = useState(null);
    const [apis, setApis] = useState()

    const handleSubmitNewNote = async () => {
        setNotesLoading(true)

        if (newNote.title && newNote.description && typeof apis !== "undefined") {
            const noteData = { title: newNote.title, description: newNote.description, contactId: selectedContact?.id }
            const noteRes = await apis.addContactNote(selectedContact.id, noteData)
            setNotes(prevNotes => [...prevNotes, noteRes]); // Temporary ID
            setNewNote({ title: '', description: '', contactId: selectedContact?.id });
            setNotesLoading(false)
        }
    }

    const handleDeleteNote = async (id) => {
        await apis.deleteContactNote(selectedContact.id, id)
        setNotes(notes.filter(note => note.id !== id));
    };

    const handleUpdateNote = async () => {
        const noteData = { title: newNote.title, description: newNote.description, contactId: selectedContact?.id }
        const newNoteRes = await apis.updateContactNote(selectedContact.id, editingNoteId, noteData)
        setNotes(notes.map(note => (note.id === editingNoteId ? newNoteRes : note)));
        setEditingNoteId(null);
        setNewNote({ title: '', description: '', contactId: selectedContact?.id });
    };

    useEffect(() => {
        if(selectedContact) {
            setApis({
                addContactNote: selectedContact.category === 'CUSTOMER' ? addNoteToCustomer : addNoteToProfessional,
                fetchContactNotes: selectedContact.category === 'CUSTOMER' ? getCustomerNotes : getProfessionalNotes,
                updateContactNote: selectedContact.category === 'CUSTOMER' ? updateCustomerNote : updateProfessionalNote,
                deleteContactNote: selectedContact.category === 'CUSTOMER' ? deleteCustomerNote : removeNoteFromProfessional,
            })
        }
    }, [selectedContact]);

    useEffect(() => {
        const fetchNotes = async () => {
            return await apis.fetchContactNotes(selectedContact.id)
        }
        if((modalShow === true && typeof selectedContact !== "undefined")) {
            setNotesLoading(true)
            fetchNotes().then(res => {
                setNotes(res)
                setNotesLoading(false)
            })
        }
    }, [modalShow]);

    return [notes, notesLoading, newNote, setNewNote, handleSubmitNewNote, editingNoteId, setEditingNoteId, handleDeleteNote, handleUpdateNote]
}

export default useContactNotes