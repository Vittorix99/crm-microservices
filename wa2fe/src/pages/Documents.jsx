import React, { useState, useEffect } from 'react';
import { useDocuments } from '../hooks/useDocuments';
import { Table, Button, Modal, Form, Spinner, InputGroup } from 'react-bootstrap';
import { Download, Trash, PencilSquare, Upload, Search } from 'react-bootstrap-icons';
import { ErrorMessage } from '../components/ErrorMessage';
import { getDocumentContentById, getDocumentMetadataById } from '../api/documents';

export default function DocumentsPage() {
  const { 
    documents, 
    loading, 
    error, 
    deleteDocument, 
    addDocument, 
    updateDocumentFile 
  } = useDocuments();

  const [showUploadModal, setShowUploadModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [selectedDocument, setSelectedDocument] = useState(null);
  const [file, setFile] = useState(null);
  const [downloadError, setDownloadError] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [filteredDocuments, setFilteredDocuments] = useState([]);

  useEffect(() => {
    setFilteredDocuments(
      documents.filter(doc => 
        doc.file_name.toLowerCase().includes(searchQuery.toLowerCase())
      )
    );
  }, [documents, searchQuery]);

  const handleFileChange = (event) => {
    setFile(event.target.files[0]);
  };

  const handleUpload = async () => {
    console.log(file);

    // print file content
    const reader = new FileReader();
    reader.onload = function(e) {
      console.log(e.target.result);
    };
  
    if (file) {
      await addDocument(file);
      setShowUploadModal(false);
      setFile(null);
    }
  };

  const handleUpdate = async () => {
    if (file && selectedDocument) {
      await updateDocumentFile(selectedDocument.metadata_id, file);
      setShowUpdateModal(false);
      setFile(null);
      setSelectedDocument(null);
    }
  };

  const handleDownload = async (doc) => {
    try {
      setDownloadError(null);
      const metadata = await getDocumentMetadataById(doc.metadata_id);
      const data = await getDocumentContentById(doc.metadata_id);
      
      const blob = new Blob([data], { type: metadata.contentType });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = doc.file_name || metadata.fileName || 'document';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);

      // Clean up the URL object after the download starts
      setTimeout(() => window.URL.revokeObjectURL(url), 100);
    } catch (error) {
      console.error('Error downloading document:', error);
      setDownloadError('Failed to download the document. Please try again.');
    }
  };

  const handleSearch = (event) => {
    setSearchQuery(event.target.value);
  };

  if (loading) return <Spinner animation="border" />;
  if (error) return <ErrorMessage error={error} />;

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Documents</h1>
      <div className="d-flex justify-content-between align-items-center mb-3">
        <InputGroup className="w-50">
          <InputGroup.Text id="search-addon">
            <Search />
          </InputGroup.Text>
          <Form.Control
            type="text"
            placeholder="Search documents by name"
            aria-label="Search"
            aria-describedby="search-addon"
            value={searchQuery}
            onChange={handleSearch}
          />
        </InputGroup>
        <Button variant="primary" onClick={() => setShowUploadModal(true)}>
          <Upload className="me-2" />
          Upload New Document
        </Button>
      </div>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>Name</th>
            <th>Size</th>
            <th>Content Type</th>
            <th>Created</th>
            <th>Last Updated</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredDocuments.map(doc => (
            <tr key={doc.metadata_id}>
              <td>{doc.file_name}</td>
              <td>{doc.file_size} bytes</td>
              <td>{doc.content_type}</td>
              <td>{new Date(doc.createdTimestamp).toLocaleString()}</td>
              <td>{doc.lastUpdate ? new Date(doc.lastUpdate).toLocaleString() : 'N/A'}</td>
              <td>
                <Button variant="outline-primary" className="me-2" onClick={() => handleDownload(doc)}>
                  <Download />
                </Button>
                <Button variant="outline-danger" className="me-2" onClick={() => deleteDocument(doc.metadata_id)}>
                  <Trash />
                </Button>
                <Button variant="outline-warning" onClick={() => {
                  setSelectedDocument(doc);
                  setShowUpdateModal(true);
                }}>
                  <PencilSquare />
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      <Modal show={showUploadModal} onHide={() => setShowUploadModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Upload New Document</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group controlId="formFile" className="mb-3">
            <Form.Label>Choose file</Form.Label>
            <Form.Control type="file" onChange={handleFileChange} />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowUploadModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={handleUpload}>
            Upload
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showUpdateModal} onHide={() => setShowUpdateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Document</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <Form.Group controlId="formFile" className="mb-3">
            <Form.Label>Choose new file</Form.Label>
            <Form.Control type="file" onChange={handleFileChange} />
          </Form.Group>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowUpdateModal(false)}>
            Close
          </Button>
          <Button variant="primary" onClick={handleUpdate}>
            Update
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}