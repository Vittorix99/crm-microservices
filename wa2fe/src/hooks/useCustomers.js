import { useState, useCallback, useEffect } from 'react';
import { getCustomers, createCustomer, updateCustomer, deleteCustomer } from '../api/customers';

const useCustomers = (initialPage = 0, initialLimit = 15) => {
  const [customers, setCustomers] = useState([]);
  const [selectedCustomer, setSelectedCustomer] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [page, setPage] = useState(initialPage);
  const [limit, setLimit] = useState(initialLimit);

  const fetchCustomers = useCallback(async (searchParams = {}) => {
    setLoading(true);
    setError(null);
    try {

        console.log("The search params are: ", searchParams)
      const response = await getCustomers({ ...searchParams, page, limit });
      console.log("The response is: ", response)
      setCustomers(response);
    } catch (err) {
      setError('Failed to fetch customers');
      console.error('Error fetching customers:', err);
    } finally {
      setLoading(false);
    }
  }, [page, limit]);

  useEffect(() => {
    fetchCustomers();
  }, [fetchCustomers]);

  const handleCreateCustomer = async (customerData) => {
    setLoading(true);
    setError(null);
    try {
      const newCustomer = await createCustomer(customerData);
      console.log(newCustomer)
      setCustomers(prevCustomers => [...prevCustomers, newCustomer]);
      return newCustomer;
    } catch (err) {
      setError('Failed to create customer');
      console.error('Error creating customer:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleUpdateCustomer = async (customerId, customerData) => {
    setLoading(true);
    setError(null);
    try {


      const updatedCustomer = await updateCustomer(customerId, customerData);
      setCustomers(prevCustomers => 
        prevCustomers.map(customer => 
          customer.id === customerId ? updatedCustomer : customer
        )
      );
      return updatedCustomer;
    } catch (err) {
      setError('Failed to update customer');
      console.error('Error updating customer:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteCustomer = async (customerId) => {
    setLoading(true);
    setError(null);
    try {
      await deleteCustomer(customerId);
      setCustomers(prevCustomers => 
        prevCustomers.filter(customer => customer.id !== customerId)
      );
      setSelectedCustomer(null);
    } catch (err) {
      setError('Failed to delete customer');
      console.error('Error deleting customer:', err);
    } finally {
      setLoading(false);
    }
  };

  return {
    customers,
    setCustomers,
    selectedCustomer,
    setSelectedCustomer,
    loading,
    error,
    page,
    setPage,
    limit,
    setLimit,
    fetchCustomers,
    handleCreateCustomer,
    handleUpdateCustomer,
    handleDeleteCustomer
  };
};

export default useCustomers;