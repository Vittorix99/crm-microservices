import {useEffect, useState} from "react";
import {getJobOffersByParams} from "../../api/joboffers.js";
import {CategoryScale} from "chart.js";
import {Pie} from "react-chartjs-2";

const AnalyticsJobOfferGraph = () => {
    
    const [chartData, setChartData] = useState({});
    const [loading, setLoading] = useState(true);
    
    useEffect(() => {
        
        const fetchJobOffer = async () => {
            try {
                const response = await getJobOffersByParams(null, null, 0, 100);

                const categoryCounts = response.reduce((acc, item) => {
                    acc[item.status] = (acc[item.status] || 0) + 1;
                    return acc;
                }, {});

                const categories = Object.keys(categoryCounts);
                const values = Object.values(categoryCounts);

                setChartData({
                    labels: categories,
                    datasets: [
                        {
                            label: 'Numero di elementi per categoria',
                            data: values,
                            backgroundColor: [
                                '#FF6384', '#36A2EB', '#FFCE56', '#4BC0C0', '#9966FF', '#FF9F40'
                            ]
                        }
                    ]
                });
                setLoading(false);
            } catch (error) {
                console.error('Errore nel recuperare i dati:', error);
                setLoading(false);
            }
        }

        fetchJobOffer();
    }, []);

    const options = {
        maintainAspectRatio: false,  // Rimuove il mantenimento dell'aspect ratio
        responsive: true,            // Fa sì che il grafico si adatti al contenitore
        plugins: {
            tooltip: {
                callbacks: {
                    label: function(tooltipItem) {
                        // Mostra la categoria e il valore nel tooltip
                        const label = chartData.labels[tooltipItem.dataIndex];
                        const value = chartData.datasets[0].data[tooltipItem.dataIndex];
                        return `${label}: ${value}`;
                    }
                }
            }
        }
    };

    return (
        <div style={{ width: '300px', height: '300px' }}>
            {loading ? (
                <p>Caricamento in corso...</p>
            ) : (
                <Pie data={chartData} options={options}/>
            )}
        </div>
    );

}

export default AnalyticsJobOfferGraph;