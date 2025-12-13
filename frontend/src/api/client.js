const API_BASE = import.meta.env.VITE_API_BASE_URL || '';

const request = async (url, options = {}) => {
  const response = await fetch(`${API_BASE}${url}`, {
    headers: {
      'Content-Type': 'application/json',
      ...options.headers,
    },
    ...options,
  });

  if (!response.ok) {
    const message = await response.text().catch(() => 'Request failed');
    throw new Error(message || `Request failed (${response.status})`);
  }

  if (response.status === 204) {
    return null;
  }

  const contentType = response.headers.get('content-type') || '';
  if (contentType.includes('application/json')) {
    return response.json();
  }

  return response.text();
};

export const fetchWorkers = () => request('/worker/all', { method: 'GET' });

export const createWorker = (worker) =>
  request('/worker/register', {
    method: 'POST',
    body: JSON.stringify(worker),
  });

export const editWorker = (workerId, worker) =>
  request(`/worker/${workerId}/edit`, {
    method: 'POST',
    body: JSON.stringify(worker),
  });

export const deleteWorker = (workerId) =>
  request(`/worker/${workerId}/delete`, { method: 'DELETE' });

export const fetchLeavesForWorker = (workerId) =>
  request(`/leave/${workerId}/worker`, { method: 'GET' });

export const searchWorkerByFullName = ({ firstName, lastName }) =>
  request('/worker/fullName', {
    method: 'GET',
    body: JSON.stringify({ firstName, lastName }),
  });

export const searchWorkerByFirstName = ({ firstName }) =>
  request('/worker/firsName', {
    method: 'GET',
    body: JSON.stringify({ firstName }),
  });

export const searchWorkerByLastName = ({ lastName }) =>
  request('/worker/surname', {
    method: 'GET',
    body: JSON.stringify({ lastName }),
  });

export const searchWorkerByPhone = ({ phoneNumber }) =>
  request('/worker/phoneNumber', {
    method: 'GET',
    body: JSON.stringify({ phoneNumber }),
  });

export const fetchTodaysLeaves = () => request('/leave/day', { method: 'GET' });

export const requestLeave = (payload) =>
  request('/leave/add', {
    method: 'POST',
    body: JSON.stringify(payload),
  });

export const editLeave = (payload) =>
  request('/leave/edit', {
    method: 'POST',
    body: JSON.stringify(payload),
  });

export const deleteLeave = (leaveId) =>
  request('/leave/delete', {
    method: 'DELETE',
    body: JSON.stringify({ leaveId }),
  });

// Products
export const fetchProducts = () => request('/product', { method: 'GET' });

export const createProduct = (payload) =>
  request('/product', { method: 'POST', body: JSON.stringify(payload) });

export const updateProduct = (productId, payload) =>
  request(`/product/${productId}`, { method: 'PUT', body: JSON.stringify(payload) });

export const deleteProduct = (productId) => request(`/product/${productId}`, { method: 'DELETE' });

// Customers
export const fetchCustomers = () => request('/customer', { method: 'GET' });

export const createCustomer = (payload) =>
  request('/customer', { method: 'POST', body: JSON.stringify(payload) });

export const updateCustomer = (customerId, payload) =>
  request(`/customer/${customerId}`, { method: 'PUT', body: JSON.stringify(payload) });

export const deleteCustomer = (customerId) => request(`/customer/${customerId}`, { method: 'DELETE' });

// Production
export const fetchProductions = () => request('/production', { method: 'GET' });

export const createProduction = (payload) =>
  request('/production', { method: 'POST', body: JSON.stringify(payload) });

export const deleteProduction = (productionId) =>
  request(`/production/${productionId}`, { method: 'DELETE' });

// Sales
export const fetchSales = () => request('/sale', { method: 'GET' });

export const createSale = (payload) => request('/sale', { method: 'POST', body: JSON.stringify(payload) });

export const deleteSale = (saleId) => request(`/sale/${saleId}`, { method: 'DELETE' });

// Returned wax
export const fetchReturns = () => request('/returned-wax', { method: 'GET' });

export const createReturn = (payload) =>
  request('/returned-wax', { method: 'POST', body: JSON.stringify(payload) });

export const deleteReturn = (returnedWaxId) => request(`/returned-wax/${returnedWaxId}`, { method: 'DELETE' });
