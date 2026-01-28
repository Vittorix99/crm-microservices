
import { useState, useEffect, useCallback} from "react";
import { getAllDocuments, getDocumentContentById, deleteDocumentById, uploadDocument, updateDocument } from '../api/documents';





export const useDocuments = () => {
    const [documents, setDocuments] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    const fetchDocuments = useCallback(async(pageSize=0, limit=20) => {
        try {
            setLoading(true);
            const data = await getAllDocuments({pageSize, limit});
            setDocuments(data);
            setError(null);

        }catch (error) {
            setError("Failed to fetch documents: " + error);

        } finally {
            setLoading(false);
        }
    }

    , []);


    useEffect(() => {
        fetchDocuments();
    }, [fetchDocuments]);

    const getDocumentContent = async (metadataId) => {
        try {
            return await getDocumentContentById(metadataId);
        } catch (error) {
            setError("Failed to fetch document content: " + error);
            console.error("Failed to fetch document content: ", error);

        }
    }

    const deleteDocument = async (metadataId) => {
        try {
            await deleteDocumentById(metadataId);
            setDocuments(documents.filter(doc => doc.id !== metadataId));
            setError(null);
        } catch (error) {
            setError("Failed to delete document: " + error);
            console.error("Failed to delete document: ", error);
        }

    }

    const addDocument = async (document) => {
        try {
            const response = await uploadDocument(document);
            setDocuments([...documents, response.data]);
            setError(null);
        } catch (error) {
            setError("Failed to upload document: " + error);
            console.error("Failed to upload document: ", error);
        }
    }


    const updateDocument = async (metadataId, document) => {
        try {
            const response = await updateDocument(metadataId, document);
            setDocuments(documents.map(doc => doc.id === metadataId ? response.data : doc));
            setError(null);
        } catch (error) {
            setError("Failed to update document: " + error);
            console.error("Failed to update document: ", error);
        }
    }

    return {documents, loading, error, getDocumentContent, deleteDocument, addDocument, updateDocument};




}

export default useDocuments;
