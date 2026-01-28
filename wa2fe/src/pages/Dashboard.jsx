import React, {useEffect, useState} from 'react'
import { Pie } from 'react-chartjs-2';
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from 'chart.js';
import {color} from "chart.js/helpers";
import {getAllSkills} from "../api/analytics.js";
import {Card, Col, Row, Table} from "react-bootstrap";
import AnalyticsJobOfferGraph from "../components/analytics/AnalyticsJobOfferGraph.jsx";
import AnalyticsProposalGraph from "../components/analytics/AnalyticsProposalGraph.jsx";

ChartJS.register(ArcElement, Tooltip, Legend);

const Dashboard = () => {
    const [skills, setSkills] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchSkills = async () => {
            try {
                const data = await getAllSkills(); // Chiama la funzione getAllSkills
                setSkills(data); // Aggiorna lo stato con i dati ottenuti
            } catch (err) {
                setError('Errore durante il recupero delle skills');
                console.error(err);
            } finally {
                setLoading(false); // Ferma il caricamento
            }
        };

        fetchSkills(); // Esegui la chiamata all'API quando il componente è montato
    }, []); // L'array vuoto [] garantisce che la chiamata venga eseguita solo al montaggio

    if (loading) {
        return <div>Caricamento delle skills...</div>; // Mostra un indicatore di caricamento
    }

    if (error) {
        return <div>{error}</div>; // Mostra un messaggio di errore in caso di errore
    }

    // Mostra le skills recuperate dall'API
    return (
        <div>
            <h1>Analytics</h1>
            {/* Skills - Job Offer */}
            <Card className="shadow-sm mb-4">
                <Card.Body>
                    <Card.Title>Skills Required By Job Offers</Card.Title>
                    {
                        skills && skills.length> 0 ? (
                            <Table striped bordered hover>
                                <thead>
                                <tr>
                                    <th>Name</th>
                                    <th>Count</th>
                                </tr>
                                </thead>
                                <tbody>
                                {skills.sort((a,b) => {return b.count - a.count}
                                ).map((skill, index)=>(
                                    <tr key={index}>
                                        <td>{skill.name}</td>
                                        <td>{skill.count}</td>
                                    </tr>
                                ))}
                                </tbody>
                            </Table>
                        ) : (
                            <tr>Nessuna skill disponibile</tr>
                        )
                    }
                </Card.Body>
            </Card>
            <Row className="mb-3" >
                <Col>
                    <Card className="shadow-sm text-center d-flex justify-content-center align-items-center">
                        <Card.Body>
                            <Card.Title>
                                Job Offer Distribution
                            </Card.Title>
                            <AnalyticsJobOfferGraph />
                        </Card.Body>
                    </Card>
                </Col>
                <Col>
                    <Card className="shadow-sm text-center d-flex justify-content-center align-items-center">
                        <Card.Title>
                            Proposals Distribution
                        </Card.Title>
                        <AnalyticsProposalGraph />
                    </Card>
                </Col>
            </Row>

        </div>
    );
};
export default Dashboard;
